package com.example.delivery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.myviewholder>{
    List<HistoryModel> list;

    public HistoryAdapter(List<HistoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    Context context;
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historycard,parent,false);
        HistoryAdapter.myviewholder viewHolder = new HistoryAdapter.myviewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {


        HistoryModel myOrdersModel = list.get(position);
        holder.t1.setText(myOrdersModel.getId());
        holder.t2.setText(myOrdersModel.getDate());
        holder.t3.setText(myOrdersModel.getPrice());
        holder.t4.setText(myOrdersModel.getVendorname());
        holder.btn.setText(myOrdersModel.getDelivery());
        holder.deliverys = myOrdersModel.getDelivery();


        if (holder.deliverys.equals("Delivered")){
            holder.btn.setBackgroundResource(R.drawable.rell);
        }else if (holder.deliverys.equals("Cancelled")){
            holder.btn.setBackgroundResource(R.drawable.cancelled);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5;
        TextView btn;
        ConstraintLayout card;
        String deliverys,totals;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.orno);
            t2 = itemView.findViewById(R.id.date);
            t3 = itemView.findViewById(R.id.payment);
            t4 = itemView.findViewById(R.id.vname);
            t5 = itemView.findViewById(R.id.status);
            btn = itemView.findViewById(R.id.statusbtn);
            card = itemView.findViewById(R.id.ordercard);

        }
    }
}
