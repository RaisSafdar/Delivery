package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomerDetailsActivity extends AppCompatActivity {
TextView cphone,cstore,cfname,ccity,cadress;
String cphones,cstores,cfnames,ccitys,cadresss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        cphone = findViewById(R.id.customerphone);
        cstore = findViewById(R.id.customerstorename);
        cfname = findViewById(R.id.customername);
        ccity = findViewById(R.id.customercity);
        cadress = findViewById(R.id.customeraddress);



        Intent intent = getIntent();
        cphones = intent.getStringExtra("cphone");
        cstores = intent.getStringExtra("cstore");
        cfnames = intent.getStringExtra("cfname");
        cadresss = intent.getStringExtra("cadress");
        ccitys = intent.getStringExtra("ccity");

        cphone.setText(cphones);
        cstore.setText(cstores);
        cfname.setText(cfnames);
        cadress.setText(cadresss);
        ccity.setText(ccitys);



    }
}