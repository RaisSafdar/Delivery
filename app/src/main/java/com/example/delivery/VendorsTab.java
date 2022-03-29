package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorsTab extends AppCompatActivity {
    JSONObject server_responce;
    ViewPagerAdapter adapter1;
    ProgressDialog progressDialog;
    String vendorname,status,vendorstatus;
    String user_id,orderid,vid,position;
    int pos;
    UserInfo userInfo;
    ArrayList<VendorsModel> catlist;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_tab);
        progressDialog = new ProgressDialog(VendorsTab.this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        tabLayout = (TabLayout) findViewById(R.id.tablayout1);
        viewPager = (ViewPager) findViewById(R.id.vpager);
        oid =  findViewById(R.id.oid);
        tabLayout.setupWithViewPager(viewPager);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("ordrid");
        position = intent.getStringExtra("pos");
        pos = Integer.parseInt(position);
        userInfo = new UserInfo(getApplicationContext());
        user_id = userInfo.getKeyId();

        oid.setText(orderid);


        addTabs(viewPager);
        tabLayout.selectTab(tabLayout.getTabAt(pos));

    }

    private void addTabs(ViewPager viewPager) {

        adapter1 = new ViewPagerAdapter(getSupportFragmentManager());

        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.ordervendor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responsefgfgf", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = obj.getJSONArray("pkwholesales");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                server_responce = jsonArray.getJSONObject(i);


                                String error = server_responce.getString("error");

                                //Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();

                                if (error.equals("true")) {

                                    Log.d("note", "onResponse: " + error);

                                    Toast.makeText(getApplicationContext(), "Vendors Not Available", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();


                                } else {



                                    String vid = server_responce.getString("vendor_id");
                                    String vname = server_responce.getString("name");
                                    String orderstatus = server_responce.getString("status");
                                    tabLayout.addTab(tabLayout.newTab().setText(vname));

                                    adapter1.addFrag(new VendorNames(vid,orderstatus,orderid,getApplicationContext(),pos), vname);
                                }

                            }

                            viewPager.setAdapter(adapter1);
                            viewPager.setCurrentItem(pos);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                            viewPager.setOffscreenPageLimit(1);
                            progressDialog.hide();

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {

                                    viewPager.setCurrentItem(tab.getPosition());

                                    //viewPager.setAdapter(adapter1);



                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {
                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {
                                }
                            });



                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Internet Issue", Toast.LENGTH_SHORT).show();



                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Internet Issue", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("order_id", orderid);
                params.put("deliveryman_id", user_id);

                return params;


            }


        };
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);

    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();



        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}
