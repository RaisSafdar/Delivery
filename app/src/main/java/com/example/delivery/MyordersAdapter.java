package com.example.delivery;

import static com.example.delivery.R.drawable.pending;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyordersAdapter extends RecyclerView.Adapter<MyordersAdapter.myviewholder> {
    List<MyOrdersModel> list;
    Context context;

    public MyordersAdapter(List<MyOrdersModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorderscard,parent,false);
        myviewholder viewHolder = new myviewholder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        MyOrdersModel myOrdersModel = list.get(position);
        holder.t1.setText(myOrdersModel.getDate());
        holder.orderid = myOrdersModel.getId();
        holder.t2.setText(myOrdersModel.getId());
        holder.s1 = myOrdersModel.getDelivery();
        holder.s2 = myOrdersModel.getVendor_id();

        if (holder.s1.equals("Delivered")){
            holder.t3.setText("Completed");
            holder.t3.setBackgroundResource(R.drawable.rell);
        }else if (holder.s1.equals("Cancelled")){
            holder.t3.setText("Completed");
            holder.t3.setBackgroundResource(R.drawable.rell);
        }else if (holder.s1.equals("Pending")){
            holder.t3.setText("Pending");
            holder.t3.setBackground(context.getDrawable(pending));
        }else if (holder.s1.equals("Ready")){
            holder.t3.setText("Pending");
            holder.t3.setBackgroundResource(pending);
        }else if (holder.s1.equals("Dispatch")){
            holder.t3.setText("Pending");
            holder.t3.setBackgroundResource(pending);
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MyVendorActivity.class);
                intent.putExtra("ordrid",holder.orderid);
                intent.putExtra("status",holder.t3.getText().toString());
                intent.putExtra("vendor_id",holder.s2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });




    } @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3;
        RelativeLayout btn;
        String s1,s2,orderid;
        ConstraintLayout constraintLayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.date);
            t2 = itemView.findViewById(R.id.oid);
            t3 = itemView.findViewById(R.id.status);
            btn = itemView.findViewById(R.id.rl);
            constraintLayout = itemView.findViewById(R.id.orderlayout);


        }
    }
}



