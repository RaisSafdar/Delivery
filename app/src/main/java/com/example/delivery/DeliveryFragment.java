package com.example.delivery;

import static com.android.volley.VolleyLog.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveryFragment extends Fragment {

    RecyclerView recyclerView;
    JSONObject server_responce;
    VendorAdapter adapter;
    List<VendorsModel> list;
    UserInfo userInfo;
    String user_id,name,vendor_id;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_vendors_list, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView = view.findViewById(R.id.vendorerec);
        list = new ArrayList<>();
        userInfo = new UserInfo(getActivity());
        user_id = userInfo.getKeyId();



        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.myvendors,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responsezero", response);

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



                                    vendor_id = server_responce.getString("vendor_id");
                                    name = server_responce.getString("name");





                                    VendorsModel listData = new VendorsModel(name,vendor_id);
                                    list.add(listData);
                                }

                            }
                            adapter=new VendorAdapter(list,getActivity());

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
                params.put("deliveryman_id", user_id);
                return params;


            }

        };
        Singleton.getInstance (getActivity()).addToRequestQueue (postRequest );

        return view;
    }


}