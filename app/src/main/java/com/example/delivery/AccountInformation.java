package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountInformation extends AppCompatActivity {
    EditText nametv,phonetv,citytv,passwordtv;
    TextView upbtn;
    JSONObject server_responce;
    ProgressDialog progressDialog;
    UserInfo userInfo;
    String user_id,name,phone,store,city,address,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        progressDialog = new ProgressDialog(AccountInformation.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");

        nametv = findViewById(R.id.nametv1);
        phonetv = findViewById(R.id.phonetv1);
        citytv = findViewById(R.id.storetv1);
        passwordtv = findViewById(R.id.addresstv1);

        upbtn = findViewById(R.id.upbtn);







        upbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nametv.getText().toString();
                phone = phonetv.getText().toString();
                city = citytv.getText().toString();
                password = passwordtv.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    nametv.setError("Field Required");
                    return;
                }
                if (phone.isEmpty()) {
                    phonetv.setError("Field can't be empty");
                    return;
                }
                else
                if (password.isEmpty()){
                    passwordtv.setError("Field Cannot Be Empty");
                } else
                if (city.isEmpty()){
                    citytv.setError("Field Cannot Be Empty");
                }

                updateprofile();
            }
        });

        userInfo = new UserInfo(getApplicationContext());
        user_id = userInfo.getKeyId();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.AccInfo,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = obj.getJSONArray("pkwholesales");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                server_responce = jsonArray.getJSONObject(i);


                                String error = server_responce.getString("error");

                                //Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();

                                if (error.equals("true")) {


                                    Toast.makeText(getApplicationContext(), "Internet Error", Toast.LENGTH_LONG).show();



                                } else {



                                    String id = server_responce.getString("id");
                                    String name = server_responce.getString("name");
                                    nametv.setText(name.toUpperCase());
                                    String phone = server_responce.getString("phone");
                                    phonetv.setText(phone);
                                    String city = server_responce.getString("city");
                                    citytv.setText(city.toUpperCase());
                                    String pasword = server_responce.getString("password");
                                    passwordtv.setText(pasword);

                                    //Toast.makeText(getActivity(),image,Toast.LENGTH_LONG).show();



                                }
                            }



                        } catch (JSONException e) {
                            progressDialog.hide();
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                       progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Internet Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                // Posting parameters to login url


                Map<String, String> params = new HashMap<>();

                UserInfo info = new UserInfo(getApplicationContext());

                params.put("id", user_id);




                return params;


            }


        };
        Singleton.getInstance (getApplicationContext()).addToRequestQueue (postRequest );

    }

    private void updateprofile() {
        progressDialog.show();
        StringRequest stringRequest=new StringRequest( Request.Method.POST ,


                Utils.Update, new Response.Listener <String> () {


            @Override
            public void onResponse(String response) {
                Log.d("testerror",response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String error_msg = jObj.getString("msg");


                    if (error) {

                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),error_msg,Toast.LENGTH_LONG).show();


                    }else {

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Profile Updated Successfull", Toast.LENGTH_SHORT).show();
                        userInfo.setPass(password);

                    }

                } catch (JSONException e) {
                    progressDialog.hide();
                    // JSON error


                    Toast.makeText (getApplicationContext(),"Internet Error",Toast.LENGTH_SHORT ).show ();
                }



            }
        } , new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText (getApplicationContext(),"Internet Error",Toast.LENGTH_SHORT ).show ();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {


                // Posting parameters to login url


                Map<String, String> params = new HashMap<>();

                params.put("phone", phone);
                params.put("name", name);
                params.put("city" , city);
                params.put("password" , password);
                params.put("deliveryman_id" , user_id);


                return params;


            }

        };
        Singleton.getInstance( getApplicationContext() ).addToRequestQueue(stringRequest);

    }
}