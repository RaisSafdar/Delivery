package com.example.delivery;

import android.app.ProgressDialog;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    JSONObject server_responce;
    MyordersAdapter adapter;
    List<MyOrdersModel> list;
    UserInfo userInfo;
    String user_id;
    ProgressDialog progressDialog;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View b = inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView = b.findViewById(R.id.rviewmyorders);
        textView = b.findViewById(R.id.torder);
        list = new ArrayList<>();

        userInfo = new UserInfo(getActivity());
        user_id = userInfo.getKeyId();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.mydeliveries,
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
                                   // Toast.makeText(getActivity(), "You Have No Orders Yet", Toast.LENGTH_LONG).show();


                                } else {
                                    progressDialog.dismiss();


                                    String id = server_responce.getString("order_id");
                                    String date = server_responce.getString("date");
                                    String delivery_status = server_responce.getString("delivery_status");
                                    String vid = server_responce.getString("vendor_id");
                                    String vname = server_responce.getString("name");
                                   // textView.setText(assign);



                                    //Toast.makeText(getActivity(),image,Toast.LENGTH_LONG).show();


                                    MyOrdersModel listData = new MyOrdersModel(id,
                                            date,delivery_status,vid,vname);
                                    list.add(listData);
                                }

                            }

                            adapter = new MyordersAdapter(list, getActivity());

                            recyclerView.setAdapter(adapter);
                            String num = String.valueOf(recyclerView.getAdapter().getItemCount());
                            textView.setText(num);
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Log.d("Error.Response", Objects.requireNonNull(e.getMessage()));
                            Toast.makeText(getActivity(), "You Have No Orders Yet", Toast.LENGTH_LONG).show();



                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("verror", "onErrorResponse: "+error.getMessage());
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("deliveryman_id", user_id);
                return params;


            }

        };
        Singleton.getInstance(getActivity()).addToRequestQueue(postRequest);

        return b;
    }
}