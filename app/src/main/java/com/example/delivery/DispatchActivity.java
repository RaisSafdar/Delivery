package com.example.delivery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DispatchActivity extends AppCompatActivity {
TextView recievedbtn,
        name,storename,mobnum,address;
    EditText builtynum,adda_drivername,drivernum,driveraddress;
    JSONObject server_responce;
    ProgressDialog progressDialog;
    String orderid,builty,namestr,numstr,addressstr,images,pos;
    int position;
    Bitmap bitmaps;
    private static int RESULT_LOAD_IMG;
    private static final int REQUEST_ONE = 1;
    Uri filepath;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

        name = findViewById(R.id.nametxtv);
        storename = findViewById(R.id.storetxttv);
        mobnum = findViewById(R.id.mobnumtxtxtv);
        address = findViewById(R.id.addresstxttv);
        builtynum = findViewById(R.id.bno);
        adda_drivername = findViewById(R.id.dnameet);
        drivernum = findViewById(R.id.mobnumber);
        driveraddress = findViewById(R.id.driveraddress);
        recievedbtn = findViewById(R.id.recieved);
        imageView = findViewById(R.id.bimage);
        progressDialog = new ProgressDialog(DispatchActivity.this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("order_id");
        position = intent.getIntExtra("pos",0);
        pos = String.valueOf(position);
        getcustomerdetails();




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, DispatchActivity.REQUEST_ONE);
            }
        });

        recievedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builty = builtynum.getText().toString();
                namestr = adda_drivername.getText().toString();
                numstr = drivernum.getText().toString();
                addressstr = driveraddress.getText().toString();



                progressDialog.show();


                StringRequest stringRequest=new StringRequest( Request.Method.POST ,


                        Utils.dispatch, new Response.Listener <String> () {


                    @Override
                    public void onResponse(String response) {
                        Log.d("testerror",response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            String error_msg = jObj.getString("msg");


                            if (error) {

                                progressDialog.hide();

                                Toast.makeText(getApplicationContext(),error_msg,Toast.LENGTH_LONG).show();


                            }else {

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(getApplicationContext(),VendorsTab.class);
                                    intent.putExtra("ordrid",orderid);
                                    intent.putExtra("pos",pos);
                                    startActivity(intent);
                                    finish();

                                }









                        } catch (JSONException e) {
                            progressDialog.hide();
                            // JSON error
                            e.printStackTrace();


                            Toast.makeText (getApplicationContext(),"Internet Error",Toast.LENGTH_SHORT ).show ();
                        }



                    }
                } , new Response.ErrorListener () {
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

                        params.put("order_id", orderid);
                        params.put("builty_num", builty);
                        params.put("driver_name", namestr);
                        params.put("driver_number", numstr);
                        params.put("address" , addressstr);
                        if (bitmaps!=null){
                            params.put("image" , imageToString(bitmaps));
                        }else {
                            params.put("image" , "null");
                        }


                        return params;


                    }

                };

                // Adding request to request queue
                Singleton.getInstance( getApplicationContext() ).addToRequestQueue(stringRequest);
            }
        });

    }

    // Image to string
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    //Showing Image In The ImageVIew
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Getting And Setting First Image to ImageView
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    filepath = data.getData();
                    final InputStream imageStream = getContentResolver()
                            .openInputStream(filepath);
                    bitmaps = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(bitmaps);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(DispatchActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    private void getcustomerdetails() {
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.customerdet,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

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



                                } else {
                                    progressDialog.dismiss();



                                    String names = server_responce.getString("fullname");
                                    String storenames = server_responce.getString("storename");
                                    String addresss = server_responce.getString("address");
                                    String phones = server_responce.getString("phone");

                                    name.setText(names);
                                    storename.setText(storenames);
                                    address.setText(addresss);
                                    mobnum.setText(phones);




                                    //Toast.makeText(getActivity(),image,Toast.LENGTH_LONG).show();

                                }

                            }


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
                params.put("order_id", orderid);
                return params;


            }

        };
        Singleton.getInstance (getApplicationContext()).addToRequestQueue (postRequest );

    }
}