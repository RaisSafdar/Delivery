package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    JSONObject server_responce;
    OrderDetailAdapter adapter;
    List<OrderDetailModel> list;
    UserInfo userInfo;
    String user_id, ordrid, status, vendor_id;
    ProgressDialog progressDialog;
    TextView ready, delivered, collected, pendingtxt, quantity, item;
    String total;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        progressDialog = new ProgressDialog(OrderDetails.this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        builder = new AlertDialog.Builder(OrderDetails.this);
        Intent intent = getIntent();
        ordrid = intent.getStringExtra("ordrid");
        status = intent.getStringExtra("status");
        vendor_id = intent.getStringExtra("vendor_id");
        recyclerView = findViewById(R.id.rviewmyorders1);
        ready = findViewById(R.id.collected);
        collected = findViewById(R.id.collected);
        delivered = findViewById(R.id.delivered);
        pendingtxt = findViewById(R.id.pendingtxt);
        quantity = findViewById(R.id.quantitynum);
        item = findViewById(R.id.itemnumber12);
        list = new ArrayList<>();


        if (status.equals("Ready")) {
            delivered.setVisibility(View.GONE);

        } else if (status.equals("Dispatch")) {
            delivered.setVisibility(View.VISIBLE);
            collected.setVisibility(View.GONE);


        } else if (status.equals("Pending")) {
            collected.setVisibility(View.GONE);
            delivered.setVisibility(View.GONE);
            pendingtxt.setVisibility(View.VISIBLE);
        } else {
            collected.setVisibility(View.GONE);
            delivered.setVisibility(View.GONE);
            pendingtxt.setVisibility(View.GONE);
        }


        userInfo = new UserInfo(getApplicationContext());
        user_id = userInfo.getKeyId();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.myorders,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            //we have the array named pkwholesales inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = obj.getJSONArray("pkwholesales");

                            Log.d("1212", "onResponse: " + jsonArray.length());


                            for (int i = 0; i < jsonArray.length(); i++) {


                                server_responce = jsonArray.getJSONObject(i);


                                Boolean error = server_responce.getBoolean("error");


                                //Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();

                                if (error) {
                                    progressDialog.dismiss();
                                    Log.d("ee", "onResponse: " + error);


                                } else {
                                    progressDialog.dismiss();

                                    int x = jsonArray.length() - 1;

                                    if (i == x) {

                                        String quantitys = server_responce.getString("qty");


                                        quantity.setText(quantitys);
                                        //  Toast.makeText(getActivity(), "abc"+total, Toast.LENGTH_SHORT).show();

                                    }


                                    String id = server_responce.getString("id");
                                    String order_id = server_responce.getString("order_id");
                                    String product_name = server_responce.getString("product_name");
                                    String product_image = server_responce.getString("product_image");
                                    total = server_responce.getString("total");
                                    String purchase_price = server_responce.getString("purchase_price");
                                    String quantity = server_responce.getString("quantity");
                                    String image = Utils.image + product_image;
                                    String vendor = server_responce.getString("vender_name");
                                    String pstatus = server_responce.getString("delivery_status");


                                    OrderDetailModel listData = new OrderDetailModel(id, order_id, product_name, image, total, quantity
                                            , purchase_price, vendor, pstatus);
                                    list.add(listData);
                                }

                            }
                            adapter = new OrderDetailAdapter(list, getApplicationContext());

                            recyclerView.setAdapter(adapter);
                            String items = String.valueOf(recyclerView.getAdapter().getItemCount());
                            item.setText(items);

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("Error.Response", Objects.requireNonNull(e.getMessage()));


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("verror", "onErrorResponse: " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("order_id", ordrid);
                params.put("vendor_id", vendor_id);
                return params;


            }

        };
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);

        collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Are You Sure");
                builder.setTitle("Collect");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String coll = collected.getText().toString();


                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,


                                Utils.dispatchUpdate, new Response.Listener<String>() {


                            @Override
                            public void onResponse(String response) {
                                Log.d("errorss", response);
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean("error");
                                    String error_msg = jObj.getString("msg");


                                    if (error) {

                                        progressDialog.hide();

                                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();


                                    } else {

                                        progressDialog.dismiss();


                                        Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();

                                        Intent intent2 = new Intent(getApplicationContext(), Home.class);
                                        startActivity(intent2);
                                        finish();


                                    }

                                } catch (JSONException e) {
                                    progressDialog.hide();
                                    // JSON error
                                    e.printStackTrace();


                                    Toast.makeText(getApplicationContext(), "Internet Error", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, new Response.ErrorListener() {
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


                                params.put("order_id", ordrid);
                                params.put("vendor_id", vendor_id);
                                params.put("status", "Dispatch");

                                return params;
                            }

                        };
                        Singleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                builder.show();
            }
        });


        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Are You Sure");
                builder.setTitle("Deliver");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,


                                Utils.dispatchUpdate, new Response.Listener<String>() {


                            @Override
                            public void onResponse(String response) {
                                Log.d("errorss1", response);
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean("error");
                                    String error_msg = jObj.getString("msg");


                                    if (error) {

                                        progressDialog.hide();

                                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();


                                    } else {

                                        progressDialog.dismiss();


                                        Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(getApplicationContext(), DispatchActivity.class);
                                        intent1.putExtra("order_id", ordrid);
                                        startActivity(intent1);
                                        finish();

                                    }

                                } catch (JSONException e) {
                                    progressDialog.hide();
                                    // JSON error
                                    e.printStackTrace();


                                    Toast.makeText(getApplicationContext(), "Internet Error", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, new Response.ErrorListener() {
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


                                params.put("order_id", ordrid);
                                params.put("vendor_id", vendor_id);
                                // params.put("rupes", total);
                                params.put("status", "Delivered");


                                return params;


                            }

                        };
                        Singleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                builder.show();

            }

        });
    }
}



