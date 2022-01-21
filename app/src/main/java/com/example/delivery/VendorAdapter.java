package com.example.delivery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VendorAdapter  extends RecyclerView.Adapter<VendorAdapter.myviewholder>{
    List<VendorsModel> list;

    public VendorAdapter(List<VendorsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    Context context;
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myvendorsorders,parent,false);
        VendorAdapter.myviewholder viewHolder = new VendorAdapter.myviewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        VendorsModel myOrdersModel = list.get(position);
        holder.textView.setText(myOrdersModel.getName());
        holder.vendor_id = myOrdersModel.getStorename();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorsProducts.class);
                intent.putExtra("vendor_id",holder.vendor_id);
                intent.putExtra("vendor_name",holder.textView.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        String vendor_id;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.vendorname);
            imageView = itemView.findViewById(R.id.arrowv);
        }
    }
}
