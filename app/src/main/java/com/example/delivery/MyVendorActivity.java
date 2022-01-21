package com.example.delivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyVendorActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    JSONObject server_responce;
    OrderVendorAdapter adapter;
    List<OrderVendorModel> list;
    UserInfo userInfo;
    String user_id,order_id,status,vendor_id,cphone,cstore,cfname,ccity,cadress;
    ProgressDialog progressDialog;
    ImageButton button;
    TextView city,storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_vendor);

        progressDialog = new ProgressDialog(MyVendorActivity.this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView = findViewById(R.id.ordersvendors);
        button = findViewById(R.id.customerdetails);
        city = findViewById(R.id.aa);
        storename = findViewById(R.id.aa1);

        list = new ArrayList<>();

        Intent intent = getIntent();
        order_id = intent.getStringExtra("ordrid");
        status = intent.getStringExtra("status");
        vendor_id = intent.getStringExtra("vendor_id");


        userInfo = new UserInfo(getApplicationContext());
        user_id = userInfo.getKeyId();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

         progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.ordervendor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responseorders", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            //we have the array named pkwholesales inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = obj.getJSONArray("pkwholesales");

                            Log.d("1212", "onResponse: " + jsonArray.length());


                            for (int i = 0; i < jsonArray.length(); i++) {


                                server_responce = jsonArray.getJSONObject(i);


                                String error = server_responce.getString("error");


                                //Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();

                                if (error.equals("true")) {
                                    progressDialog.dismiss();


                                    Toast.makeText(getApplicationContext(), "You Have No Orders Yet", Toast.LENGTH_LONG).show();


                                } else {
                                    progressDialog.dismiss();


                                    String vid = server_responce.getString("vendor_id");
                                    String vname = server_responce.getString("name");
                                    String orderstatus = server_responce.getString("status");
                                    cphone = server_responce.getString("cphone");
                                    cstore = server_responce.getString("cstorename");
                                    cfname = server_responce.getString("cfname");
                                    ccity = server_responce.getString("city");
                                    cadress = server_responce.getString("cadress");
                                    city.setText(ccity);
                                    storename.setText(cstore);




                                    //Toast.makeText(getActivity(),image,Toast.LENGTH_LONG).show();


                                    OrderVendorModel listData = new OrderVendorModel(vname,vid,orderstatus,
                                            cphone,cstore,cfname,ccity,cadress);
                                    list.add(listData);
                                }

                            }
                            Collections.reverse(list);
                            adapter = new OrderVendorAdapter(list, getApplicationContext(),order_id,status,vendor_id);

                            recyclerView.setAdapter(adapter);

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
                        Log.d("verror", "onErrorResponse: "+error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("order_id", order_id);
                params.put("deliveryman_id", user_id);
                return params;


            }

        };
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent1 = new Intent(getApplicationContext(),CustomerDetailsActivity.class);
            intent1.putExtra("cfname",cfname);
            intent1.putExtra("cphone",cphone);
            intent1.putExtra("cadress",cadress);
            intent1.putExtra("cstore",cstore);
            intent1.putExtra("ccity",ccity);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);

            }
        });
    }
}
