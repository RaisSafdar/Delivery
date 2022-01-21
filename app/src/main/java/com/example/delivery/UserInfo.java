package com.example.delivery;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {
    private static final String PREF_NAME = "userinfo";
    private static final String KEY_MOBILE = "phone";
    private static final String KEY_ID = "ids";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String idd,phone;
    Context ctx;

    public UserInfo(Context ctx) {
        this.ctx = ctx;

        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }



    public void setPhone(String phone) {
        editor.putString ( KEY_MOBILE , phone );
        editor.apply ();
    }

    public void setId(String id) {


        idd=id;
        editor.putString ( KEY_ID, id );
        editor.apply ();

    }


    public void clearUserInfo() {
        editor.clear ();
        editor.commit ();
    }


    public String getKeyId() {
        return prefs.getString ( KEY_ID,idd);
    }
    public String getKeyMobile() {
        return prefs.getString ( KEY_MOBILE,phone);
    }

}
