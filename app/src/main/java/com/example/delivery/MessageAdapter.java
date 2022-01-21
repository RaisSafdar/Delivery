package com.example.delivery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    UserInfo userInfo;
    String user_id;
    private Context mContext;
    private List<MessageModel> mChat;
    FragmentManager fragmentManager;



    public MessageAdapter(Context mContext, List<MessageModel> mChat, FragmentManager fragmentManager){
        this.mChat = mChat;
        this.mContext = mContext;
        this.mChat = mChat;
        this.fragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from ( mContext );
        View view = inflater.inflate ( R.layout.messageout , null );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        MessageModel chat = mChat.get(position);



        holder.Date=chat.getMessage_time();
        holder.date.setText(holder.Date);
        holder.Message=chat.getMessage();
        holder.Sender_id=chat.getSender_id();
        holder.admin = chat.getAdmin_id();

        Log.d("hello", "onBindViewHolder: "+"sender"+holder.Sender_id);
        Log.d("hello", "onBindViewHolder: "+"admin"+holder.admin);

        if(holder.Sender_id.equals(user_id)){



            holder.other_message.setVisibility(View.INVISIBLE);

            holder.my_message.setVisibility(View.VISIBLE);
            holder.my_message.setText(holder.Message);


        }
        else {




            holder.my_message.setVisibility(View.INVISIBLE);
            holder.other_message.setVisibility(View.VISIBLE);
            holder.other_message.setText(holder.Message);

        }
    }






    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView my_message,other_message,date;
        String admin,Message,Date,Sender_id;



        public ViewHolder(View itemView) {
            super(itemView);
            userInfo = new UserInfo(mContext);
            user_id = userInfo.getKeyId();
            my_message = itemView.findViewById(R.id.myMsg);
            other_message = itemView.findViewById(R.id.user_msg);
            date = itemView.findViewById(R.id.date);


        }
    }


}
