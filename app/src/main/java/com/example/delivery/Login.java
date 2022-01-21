package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView loginbtn,forgot;
    EditText phonenumber, passwrod;
    String phonenumbers, passwords;
    UserSession userSession;
    ProgressDialog progressDialog;
    String user_id;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        loginbtn = findViewById(R.id.loginbtn);
        forgot = findViewById(R.id.forgotpass);
        phonenumber = findViewById(R.id.pnomber);
        passwrod = findViewById(R.id.passwordedt);
        userSession = new UserSession(this);
        userInfo = new UserInfo(Login.this);
        user_id = userInfo.getKeyId();

        if (userSession.isUserLoggedin()) {
            checkstatus(user_id);
        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,ForgotPasswordActivity.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenumbers = phonenumber.getText().toString();
                passwords = passwrod.getText().toString();

                if (TextUtils.isEmpty(phonenumbers)) {
                    phonenumber.setError("Phone Required");
                    return;
                }

                if (TextUtils.isEmpty(passwords)) {
                    passwrod.setError("Password Required");
                    return;
                }
                progressDialog.show();


                StringRequest stringRequest = new StringRequest(Request.Method.POST,


                        Utils.Login, new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("testerror", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            String error_msg = jObj.getString("msg");




                            if (!error) {
                                String id = jObj.getString("id");





                                UserInfo info = new UserInfo(getApplicationContext());

                                userSession.setLoggedin(true);
                                info.setId(id);




                                progressDialog.dismiss();


                                Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                                finish();


                            } else {
                                // Error in login. Get the error message
                                progressDialog.dismiss();


                                Toast.makeText(getApplicationContext(), "Your Account Has Not Been Approved"+error_msg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Incorrect Phone Or Password Try Again", Toast.LENGTH_SHORT).show();

                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Log.e(TAG, "Login Error: " + error.getMessage());

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {


                        // Posting parameters to login url


                        Map<String, String> params = new HashMap<>();


                        params.put("phone", phonenumbers);
                        params.put("password", passwords);


                        return params;


                    }

                };

                // Adding request to request queue
                Singleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
    public void checkstatus(String ids){

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,


                Utils.Checkstatus, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("testerror1111", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //   String error_msg = jObj.getString("msg");

                    if (!error) {
                        String id = jObj.getString("status");
                        Log.d("gggg", "onResponse: "+id);

                        if (id.equals("Active")) {


                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);
                            finish();

                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Account Not Active", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        // Error in login. Get the error message
                        progressDialog.dismiss();

                        //  Toast.makeText(getApplicationContext(), "Your Account Has Not Been Approved", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    // JSON error
                    e.printStackTrace();

                    //Toast.makeText(getApplicationContext(), "Incorrect Phone Or Password Try Again", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                //Log.e(TAG, "Login Error: " + error.getMessage());

                //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<>();
                params.put("phone", ids);
                // params.put("password", passwords);
                return params;

            }
        };

        // Adding request to request queue
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}