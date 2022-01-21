package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class VendorsProducts extends AppCompatActivity {
    RecyclerView recyclerView;
    JSONObject server_responce;
    VendorsProductsAdapter adapter;
    List<VendorsProductsModel> list;
    UserInfo userInfo;
    String user_id,name,status,image,vendor_id,vendorname,id;
    ProgressDialog progressDialog;
    TextView vname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_products);
        progressDialog = new ProgressDialog(VendorsProducts.this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView = findViewById(R.id.productsrec);
        vname = findViewById(R.id.ycompl);
        list = new ArrayList<>();
        userInfo = new UserInfo(getApplicationContext());
        user_id = userInfo.getKeyId();

        Intent intent = getIntent();
        vendor_id = intent.getStringExtra("vendor_id");
        vendorname = intent.getStringExtra("vendor_name");


        vname.setText(vendorname);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.myvendorsproducts,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responsezero1", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            //we have the array named pkwholesales inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = obj.getJSONArray("pkwholesales");

                            Log.d("1212", "onResponse: "+jsonArray.length());


                            for (int i = 0; i < jsonArray.length(); i++) {


                                server_responce = jsonArray.getJSONObject(i);


                                Boolean error = server_responce.getBoolean("error");


                                //Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();

                                if (error) {
                                    progressDialog.dismiss();
                                    Log.d("ee", "onResponse: "+error);



                                } else {
                                    progressDialog.dismiss();



                                    status = server_responce.getString("status");
                                    name = server_responce.getString("name");
                                    image = server_responce.getString("product_image");
                                    id = server_responce.getString("p_id");
                                    String product_image = Utils.image + image;





                                    VendorsProductsModel listData = new VendorsProductsModel(product_image,name,status,id);
                                    list.add(listData);
                                    Log.d("list", "onResponse: "+list);
                                }

                            }
                            adapter=new VendorsProductsAdapter(list,getApplicationContext(),progressDialog);

                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("Error.Response", Objects.requireNonNull(e.getMessage()));


                        }
                    }
                },
                new Response.ErrorListener()
                {
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
                params.put("vendor_id", vendor_id);
                return params;


            }

        };
        Singleton.getInstance (getApplicationContext()).addToRequestQueue (postRequest );
    }
}