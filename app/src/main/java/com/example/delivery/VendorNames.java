package com.example.delivery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class VendorNames extends Fragment {

    RecyclerView recyclerView;
    JSONObject server_responce;
    OrderDetailAdapter adapter;
    List<OrderDetailModel> list;
    UserInfo userInfo;
    String user_id;
    ProgressDialog progressDialog;
    TextView ready, delivered, collected, pendingtxt, quantity, item;
    String total;
    int pos;
    AlertDialog.Builder builder;
    String vid,vendorstatus,orderid;
    Context context;


    public VendorNames(String vid, String vendorstatus, String orderid, Context applicationContext, int pos) {
        this.context = applicationContext;
        this.vid = vid;
        this.vendorstatus = vendorstatus;
        this.orderid = orderid;
        this.pos = pos;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_names, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        builder = new AlertDialog.Builder(getActivity());
        recyclerView = view.findViewById(R.id.rviewmyorders1);
        ready = view.findViewById(R.id.collected);
        collected = view.findViewById(R.id.collected);
        delivered = view.findViewById(R.id.delivered);
        pendingtxt = view.findViewById(R.id.pendingtxt);
        quantity = view.findViewById(R.id.quantitynum);
        item = view.findViewById(R.id.itemnumber12);
        list = new ArrayList<>();



        if (vendorstatus.equals("Ready")) {
            delivered.setVisibility(View.GONE);

        } else if (vendorstatus.equals("Dispatch")) {
            delivered.setVisibility(View.VISIBLE);
            collected.setVisibility(View.GONE);


        } else if (vendorstatus.equals("Pending")) {
            collected.setVisibility(View.GONE);
            delivered.setVisibility(View.GONE);
            pendingtxt.setVisibility(View.VISIBLE);
        } else {
            collected.setVisibility(View.GONE);
            delivered.setVisibility(View.GONE);
            pendingtxt.setVisibility(View.GONE);
        }


        userInfo = new UserInfo(getActivity());
        user_id = userInfo.getKeyId();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                            adapter = new OrderDetailAdapter(list, getActivity());

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
                params.put("order_id", orderid);
                params.put("vendor_id", vid);
                return params;


            }

        };
        Singleton.getInstance(getActivity()).addToRequestQueue(postRequest);

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

                                        Toast.makeText(getActivity(), error_msg, Toast.LENGTH_LONG).show();


                                    } else {

                                        progressDialog.dismiss();


                                        Toast.makeText(getActivity(), "Collected", Toast.LENGTH_SHORT).show();

                                        collected.setVisibility(View.GONE);
                                        delivered.setVisibility(View.VISIBLE);


                                    }

                                } catch (JSONException e) {
                                    progressDialog.hide();
                                    // JSON error
                                    e.printStackTrace();


                                    Toast.makeText(getActivity(), "Internet Error", Toast.LENGTH_SHORT).show();
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


                                params.put("order_id", orderid);
                                params.put("vendor_id", vid);
                                params.put("status", "Dispatch");

                                return params;
                            }

                        };
                        Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

                                        Toast.makeText(getActivity(), error_msg, Toast.LENGTH_LONG).show();


                                    } else {

                                        progressDialog.dismiss();


                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(getActivity(), DispatchActivity.class);
                                        intent1.putExtra("order_id", orderid);
                                        intent1.putExtra("pos", pos);
                                        startActivity(intent1);
                                        getActivity().finish();

                                    }

                                } catch (JSONException e) {
                                    progressDialog.hide();
                                    // JSON error
                                    e.printStackTrace();


                                    Toast.makeText(getActivity(), "Internet Error", Toast.LENGTH_SHORT).show();
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


                                params.put("order_id", orderid);
                                params.put("vendor_id", vid);
                                // params.put("rupes", total);
                                params.put("status", "Delivered");


                                return params;


                            }

                        };
                        Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

        return view;
    }
}