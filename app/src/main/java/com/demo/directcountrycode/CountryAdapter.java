package com.demo.directcountrycode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nishi on 4/27/2018.
 */

public class CountryAdapter extends BaseAdapter implements Filterable {

    private List<Country> originalData = null;

    private List<Country> filteredData = null;

    private ItemFilter mFilter = new ItemFilter();

    private Context context;

    private LayoutInflater inflter;

    public CountryAdapter(Context context, List<Country> countries) {
        this.filteredData = countries;

        this.originalData = countries;

        this.context = context;

        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_country_list, null);

        Country cou = filteredData.get(i);

        ImageView countryFlag = (ImageView) view.findViewById(R.id.iv_rowOfList_countryFlag);
        TextView countryName = (TextView) view.findViewById(R.id.tv_rowOfList_countryName);
        TextView countryDialCode = (TextView) view.findViewById(R.id.tv_rowOfList_countryDialCode);

        getImageFromAssert(cou.getCountryCode(), countryFlag);
        countryDialCode.setText(cou.getCountryDial_code());
        countryName.setText(cou.getCountryName());


        return view;
    }


    @Override
    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Country> list = originalData;

            int count = list.size();
            final ArrayList<Country> nlist = new ArrayList<Country>(count);

            String filterableString;
            String filterableString1;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getCountryName();
                filterableString1 = list.get(i).getCountryDial_code();
                if (filterableString.toLowerCase().contains(filterString) || filterableString1.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Country>) results.values;
            notifyDataSetChanged();
        }

    }

    private void getImageFromAssert(String countryCode, ImageView countryFlag) {
        try {
            InputStream ins = context.getAssets().open(countryCode + ".png");
            Drawable d = Drawable.createFromStream(ins, null);
            countryFlag.setImageDrawable(d);
        } catch (Exception e) {
            countryFlag.setImageDrawable(null);
        }

    }
}