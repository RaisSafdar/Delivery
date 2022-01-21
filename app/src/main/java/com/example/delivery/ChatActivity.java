package com.example.delivery;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    EditText messagebox;
    TextView send;
    String message;
    RecyclerView recyclerView;
    UserInfo userInfo;
    String user_id;
    JSONObject server_responce;
    List<MessageModel> list;
    MessageAdapter adapter;
    FragmentManager fragmentManager;
    String Flag="False";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messagebox = findViewById(R.id.messagebox);
        progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        send = findViewById(R.id.send);
        recyclerView = findViewById(R.id.messagelistview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setSmoothScrollbarEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        userInfo = new UserInfo(getApplicationContext());
        user_id = userInfo.getKeyId();

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                LoadingData();
            }

        }, 0, 45000);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataSend();


            }
        });

    }
    void DataSend(){
        progressDialog.show();


        message= messagebox.getText().toString();

        if(message.isEmpty()){
            progressDialog.dismiss();

            Toast.makeText(getApplicationContext(),"First Type Message",Toast.LENGTH_LONG).show();
            return;

        }

        StringRequest stringRequest=new StringRequest ( Request.Method.POST ,
                Utils.Chat , new Response.Listener <String> () {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, "Response1: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");





                    if (!error) {

                        progressDialog.dismiss();
                        messagebox.setText(null);
                        Toast.makeText(getApplicationContext(), "Message Sent Successfull", Toast.LENGTH_SHORT).show();
                        LoadingData();



                    }


                    else {
                        // Error in login. Get the error message

                        String errorMsg = jObj.getString("error_msg" );
                        Toast.makeText ( getApplicationContext (),errorMsg,Toast.LENGTH_SHORT ).show ();
                        progressDialog.dismiss();

                    }
                } catch (JSONException e) {

                    // JSON error
                    e.printStackTrace();
                    Toast.makeText ( getApplicationContext (),"Json error: " + e.getMessage(),Toast.LENGTH_SHORT ).show ();
                    progressDialog.dismiss();


                }



            }
        } , new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText ( getApplicationContext (),"Error...",Toast.LENGTH_SHORT ).show ();
                error.printStackTrace ();
                progressDialog.dismiss();

            }

        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params=new HashMap<>();
                params.put ( "sender_id" ,user_id);
                params.put ( "message" ,message);








                return params;
            }
        };
        Singleton.getInstance (getApplicationContext ()).addToRequestQueue ( stringRequest );
    }
    private  void LoadingData(){
        list.clear();





        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.GetChat,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = obj.getJSONArray("pkwholesales");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                server_responce = jsonArray.getJSONObject(i);


                                String error = server_responce.getString("error");

                                //Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();

                                if (error.equals("True")) {

                                    //progressDialog.dismiss();
                                    progressDialog.dismiss();

                                    // Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();


                                } else {

                                    //progressDialog.hide();
                                    progressDialog.dismiss();

                                    String s_id = server_responce.getString("sender_id");
                                    String r_id = server_responce.getString("admin_id");
                                    String message = server_responce.getString("message");
                                    String time = server_responce.getString("message_time_message_date");






                                    MessageModel viewProjectsDetailsModel=new MessageModel(s_id,r_id,message,time);
                                    list.add(viewProjectsDetailsModel);
                                }

                            }
                            adapter = new MessageAdapter(getApplicationContext(), list, fragmentManager);

                            adapter.setHasStableIds(true);
                            recyclerView.setAdapter(adapter);





                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", (error.getMessage()));
                        progressDialog.dismiss();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params=new HashMap<>();
                params.put ( "sender_id" ,user_id);









                return params;
            }
        };


        Singleton.getInstance (getApplicationContext ()).addToRequestQueue (postRequest );


    }

}