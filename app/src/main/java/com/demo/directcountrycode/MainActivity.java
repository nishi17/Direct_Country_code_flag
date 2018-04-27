package com.demo.directcountrycode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Context mContext;

    private ImageView iv_countryFlag;

    private TextView tv_countryCode;

    private String dc;


    public List<Country> countryArrayList;
    public Commonclass commonclass;

    private String get_CountryDial_code, get_CountryCode, get_CountryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        commonclass = new Commonclass(mContext);

        tv_countryCode = (TextView) findViewById(R.id.tv_countryCode);

        iv_countryFlag = (ImageView) findViewById(R.id.iv_countryFlag);


        countryArrayList = commonclass.getCountryDetails(MainActivity.this);

        dc = GetCountryZipCode();
        if (dc != null && !dc.isEmpty()) {
            tv_countryCode.setText("+" + dc);
        }

        iv_countryFlag = (ImageView) findViewById(R.id.iv_countryFlag);
        Drawable im = getFlag();
        if (im != null) {
            iv_countryFlag.setImageDrawable(im);
        }
        iv_countryFlag.setOnClickListener(this);
        tv_countryCode.setOnClickListener(this);

    }


    // get country code with the help of TelephonyManager
    private String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    // get country flag from the country code
    private Drawable getFlag() {
        String CountryID = "";
        Drawable dp = null;
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        CountryID = manager.getSimCountryIso().toUpperCase();
        try {
            InputStream ins = this.getAssets().open(CountryID + ".png");
            dp = Drawable.createFromStream(ins, null);

        } catch (Exception e) {
        }
        return dp;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_countryFlag:
            case R.id.tv_countryCode:
                showCountryDetails();
                break;

        }
    }

    private void showCountryDetails() {
        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        int width, height;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y;
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        final Dialog dialog = new Dialog(MainActivity.this, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_country);

        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        dialog.getWindow().setAttributes(lp);


        EditText et_search = (EditText) dialog.findViewById(R.id.et_search);
        ListView list_country = (ListView) dialog.findViewById(R.id.list_country);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        list_country.setTextFilterEnabled(true);
        final CountryAdapter countryAdapter = new CountryAdapter(mContext, countryArrayList);
        /*Log.d("sizeof", String.valueOf(countryArrayList.size()));*/

        list_country.setAdapter(countryAdapter);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countryAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        list_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) countryAdapter.getItem(i);

                get_CountryDial_code = country.getCountryDial_code();
                get_CountryCode = country.getCountryCode();
                get_CountryName = country.getCountryName();

                getCountryFlag(get_CountryCode, iv_countryFlag);

                tv_countryCode.setText(get_CountryDial_code);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void getCountryFlag(String get_countryCode, ImageView iv_countryFlag) {
        try {
            InputStream ins = mContext.getAssets().open(get_countryCode + ".png");
            Drawable d = Drawable.createFromStream(ins, null);
            iv_countryFlag.setImageDrawable(d);
        } catch (Exception e) {
            iv_countryFlag.setImageDrawable(null);
        }
    }



}
