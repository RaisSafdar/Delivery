package com.example.delivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorsProductsAdapter extends RecyclerView.Adapter<VendorsProductsAdapter.myviewholder>{
    List<VendorsProductsModel> list;
    Context context;
    ProgressDialog progressDialog;

    public VendorsProductsAdapter(List<VendorsProductsModel> list, Context context, ProgressDialog progressDialog) {
        this.list = list;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @NonNull
    @Override
    public VendorsProductsAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendorproductscard,parent,false);
        VendorsProductsAdapter.myviewholder viewHolder = new VendorsProductsAdapter.myviewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VendorsProductsAdapter.myviewholder holder, int position) {
        VendorsProductsModel vendorsProductsModel = list.get(position);
        holder.name.setText(vendorsProductsModel.getProductname());
        holder.status.setText(vendorsProductsModel.getStatus());
        holder.image = vendorsProductsModel.getImage();
        holder.stat = vendorsProductsModel.getStatus();
        holder.product_id = vendorsProductsModel.getId();

        Glide.with(context)
                .load(holder.image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(holder.imageView);

        if (holder.stat.equals("Active")){
            holder.status.setBackgroundResource(R.drawable.rell);
        }else if (holder.stat.equals("InActive")){
            holder.status.setBackgroundResource(R.drawable.cancelled);
        }
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,


                        Utils.statusupdate, new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("errorss", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            String error_msg = jObj.getString("msg");


                            if (error) {

                                progressDialog.dismiss();

                                Toast.makeText(context, error_msg, Toast.LENGTH_LONG).show();


                            } else {

                                progressDialog.dismiss();



                                if (holder.status.getText().toString().equals("Active")){
                                    vendorsProductsModel.setStatus("InActive");
                                    notifyDataSetChanged();
                                    holder.status.setText("InActive");
                                    Toast.makeText(context, "Status InActive", Toast.LENGTH_SHORT).show();
                                    holder.status.setBackgroundResource(R.drawable.cancelled);
                                }else if(holder.status.getText().toString().equals("InActive")){
                                    vendorsProductsModel.setStatus("Active");
                                    notifyDataSetChanged();
                                    holder.status.setText("Active");

                                    Toast.makeText(context, "Status Active", Toast.LENGTH_SHORT).show();
                                    holder.status.setBackgroundResource(R.drawable.rell);
                                }


                            }

                        } catch (JSONException e) {
                            progressDialog.hide();
                            // JSON error
                            e.printStackTrace();


                            Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
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


                        params.put("product_id", holder.product_id);
                        if (holder.stat.equals("Active")) {
                            params.put("status", "InActive");
                        }else {
                            params.put("status", "Active");
                        }

                        return params;
                    }

                };
                Singleton.getInstance (context).addToRequestQueue (stringRequest );

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name,status;
        ImageView imageView;
        String image,stat,product_id;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemcarditemname);
            status = itemView.findViewById(R.id.buttonn);
            imageView = itemView.findViewById(R.id.itemcardimg);
        }
    }
}
