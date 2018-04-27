package com.demo.directcountrycode;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nishi on 4/27/2018.
 */

public class Commonclass {

    public Context context;

    public Commonclass(Context context) {
        this.context = context;
    }

    public ArrayList<Country> getCountryDetails(Activity activity) {

        ArrayList<Country> Country = new ArrayList<>();
        try {

            String str = loadJSONFromAsset(activity);

            JSONArray m_jArry = new JSONArray(str);

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                // Log.d("Details-->", jo_inside.getString("formule"));
                String code = jo_inside.getString("code");
                String name = jo_inside.getString("name");
                String dial_code = String.valueOf("+" + jo_inside.getInt("dial_code"));

                Country country = new Country(dial_code, code, name);

                Country.add(country);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Country;
    }


    public String loadJSONFromAsset(Activity activity) {

        String json = null;
        try {
            InputStream is = activity.getAssets().open("countryCodes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
