package com.example.delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderVendorAdapter extends RecyclerView.Adapter<OrderVendorAdapter.myviewholder>{
    List<OrderVendorModel> list;
    String order_id,status,vendor_id;

    public OrderVendorAdapter(List<OrderVendorModel> list, Context context,String order_id,String status, String vendor_id) {
        this.list = list;
        this.context = context;
        this.order_id = order_id;
        this.status = status;
        this.vendor_id = vendor_id;
    }

    Context context;
    @NonNull
    @Override
    public OrderVendorAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendorcard,parent,false);
        OrderVendorAdapter.myviewholder viewHolder = new OrderVendorAdapter.myviewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderVendorAdapter.myviewholder holder, @SuppressLint("RecyclerView") int position) {
        OrderVendorModel myOrdersModel = list.get(position);
        holder.textView.setText(myOrdersModel.getName());
        holder.vendor_id = myOrdersModel.getId();
        holder.s1 = myOrdersModel.getOrderstatus();
        holder.cphone = myOrdersModel.getCphone();
        holder.cstore = myOrdersModel.getCstore();
        holder.cadress = myOrdersModel.getCadress();
        holder.ccity = myOrdersModel.getCcity();
        holder.cfname = myOrdersModel.getCfname();
        holder.statuss.setText(myOrdersModel.getOrderstatus());


        if (holder.s1.equals("Delivered")){
            holder.relativeLayout.setBackgroundResource(R.drawable.rell);
        }else if (holder.s1.equals("Ready")){
            holder.relativeLayout.setBackgroundResource(R.drawable.readyimg);
        }else if (holder.s1.equals("Dispatch")){
            holder.relativeLayout.setBackgroundResource(R.drawable.dispatch);
        }else if (holder.s1.equals("Cancelled")){
            holder.relativeLayout.setBackgroundResource(R.drawable.cancelled);
        }else if (holder.s1.equals("Pending")){
            holder.relativeLayout.setBackgroundResource(R.drawable.pending);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorsTab.class);
//                intent.putExtra("vendor_id",holder.vendor_id);
//                intent.putExtra("status",holder.s1);
                intent.putExtra("ordrid",order_id);
                intent.putExtra("pos",String.valueOf(position));
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
        TextView textView,statuss;
        RelativeLayout relativeLayout;
        String vendor_id,s1,cphone,cstore,cfname,ccity,cadress,position;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.vendorname);
            statuss = itemView.findViewById(R.id.status);
            relativeLayout = itemView.findViewById(R.id.rl);
        }
    }
}
