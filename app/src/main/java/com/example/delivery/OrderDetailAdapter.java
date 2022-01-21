package com.example.delivery;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.myviewholder>{
    List<OrderDetailModel> list;
    Context context;
    TextView quantity;
    int quantity_total=0;
    String id,s1;



    public OrderDetailAdapter(List<OrderDetailModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    UserInfo userInfo;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitems,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        OrderDetailModel orderDetailModel = list.get(position);
        holder.image = orderDetailModel.getProduct_image();
            Glide.with(context)
                    .load(holder.image)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(holder.pimage);

        holder.name.setText(orderDetailModel.getProduct_name());
        holder.total.setText(orderDetailModel.getQuantity());
        int quantitty = Integer.parseInt(orderDetailModel.getQuantity());
      //  holder.totals = orderDetailModel.getTotal();
        holder.stats = orderDetailModel.getPstatus();
//        quantity_total= quantity_total+Integer.parseInt(orderDetailModel.getQuantity());
//        quantity.setText(String.valueOf(quantity_total));
//        Log.d("quantitytotal", "onBindViewHolder: "+quantity_total);
//        if (holder.stats.equals("Pending")){
//
//            holder.pstat.setTextColor(Color.parseColor("#D17E00"));
//        }else if (holder.stats.equals("Cancelled")){
//            holder.pstat.setTextColor(Color.parseColor("#EB5757"));
//        }else if (holder.stats.equals("Delivered")){
//            holder.pstat.setTextColor(Color.parseColor("#27AE60"));
//        }else if (holder.stats.equals("Ready")){
//            holder.pstat.setTextColor(Color.parseColor("#CA00C2"));
//        }else{
//            holder.pstat.setTextColor(Color.parseColor("#16B0BA"));
//        }






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        TextView total;
        TextView name,vendor,pstat;
        ImageView pimage;
        String image,totals,stats;
        int newtotal;
        public myviewholder(View view) {
            super(view);
            total = itemView.findViewById(R.id.totalqtty);
            name = itemView.findViewById(R.id.productname);
            vendor = itemView.findViewById(R.id.vendorname);
            pimage = itemView.findViewById(R.id.productimage);




        }
    }
}

