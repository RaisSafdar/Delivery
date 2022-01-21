package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ComplaintsActivity extends AppCompatActivity {
    EditText complaint;
    TextView submit;
    String complaints;
    ProgressDialog progressDialog;
    UserInfo userInfo;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Submitting Complaint");
        progressDialog.setMessage("Please Wait");
        userInfo = new UserInfo(getApplicationContext());
        user_id = userInfo.getKeyId();


        complaint = findViewById(R.id.etcom);
        submit = findViewById(R.id.cmplbtn);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                complaints = complaint.getText().toString();

                if (complaints.isEmpty()) {
                    complaint.setError("Field can't be empty");
                    return ;
                }


                progressDialog.show();


                StringRequest stringRequest=new StringRequest( Request.Method.POST ,


                        Utils.complaints, new Response.Listener <String> () {


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



                                Toast.makeText(getApplicationContext(), "Submitted Successfull", Toast.LENGTH_SHORT).show();
                                complaint.setText("");

                            }









                        } catch (JSONException e) {
                            progressDialog.hide();
                            // JSON error
                            e.printStackTrace();


                            Toast.makeText (getApplicationContext(),"Internet Error",Toast.LENGTH_SHORT ).show ();
                        }



                    }
                } , new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        //Log.e(TAG, "Login Error: " + error.getMessage());



                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {


                        // Posting parameters to login url


                        Map<String, String> params = new HashMap<>();


                        params.put("complaint", complaints);
                        params.put("user_id", user_id);



                        return params;


                    }

                };

                // Adding request to request queue
                Singleton.getInstance( getApplicationContext() ).addToRequestQueue(stringRequest);
            }
        });
    }
}