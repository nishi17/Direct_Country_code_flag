package com.demo.directcountrycode;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Nishi on 4/27/2018.
 */

public class CountryAdapter extends BaseAdapter implements Filterable, StickyListHeadersAdapter, SectionIndexer {

    private List<Country> originalData = null;
    private List<Country> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    private Context context;
    private LayoutInflater inflter;
    private String[] countri;
    HashMap<String, Integer> mapIndex;
    String[] sections;


    public CountryAdapter(Context context, List<Country> countries) {

        this.filteredData = countries;
        this.originalData = countries;
        this.context = context;
        inflter = (LayoutInflater.from(context));
        countri = context.getResources().getStringArray(R.array.countries);

        mapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < filteredData.size(); x++) {

            String fruit = filteredData.get(x).getCountryName();

            String ch = fruit.substring(0, 1);

            ch = ch.toUpperCase(Locale.US);

            // HashMap will prevent duplicates
            mapIndex.put(ch, x);
        }

        Set<String> sectionLetters = mapIndex.keySet();
        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

        Log.d("sectionList", sectionList.toString());

        Collections.sort(sectionList);

        sections = new String[sectionList.size()];

        sectionList.toArray(sections);

    }

    @Override
    public int getPositionForSection(int section) {
        Log.e("section", "" + section);
        if (section ==0)
        return mapIndex.get(sections[section ]);
        else
            return mapIndex.get(sections[section - 1 ]);
    }

    @Override
    public int getSectionForPosition(int position) {
        Log.e("position", "" + position + 1);
        return 0;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }



    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

//    @Override
//    public Object getItem(int i) {
//        return filteredData.get(i);
//    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        /*if (view == null) {*/
        view = inflter.inflate(R.layout.row_country_list, null);

        Country cou = filteredData.get(i);

        ImageView countryFlag = (ImageView) view.findViewById(R.id.iv_rowOfList_countryFlag);
        TextView countryName = (TextView) view.findViewById(R.id.tv_rowOfList_countryName);
        TextView countryDialCode = (TextView) view.findViewById(R.id.tv_rowOfList_countryDialCode);

        getImageFromAssert(cou.getCountryCode(), countryFlag);
        countryDialCode.setText(cou.getCountryDial_code());
        countryName.setText(cou.getCountryName());
    /*    } else {
            view = (View) view.getTag();
        }*/
        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;

        holder = new HeaderViewHolder();
        convertView = inflter.inflate(R.layout.header, parent, false);
        RelativeLayout rl_header = (RelativeLayout) convertView.findViewById(R.id.rl_header);

        holder.text = (TextView) convertView.findViewById(R.id.text1);
        // convertView.setTag(holder);
       /* } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }*/
        //set header text as first char in name
        String headerText = "" + countri[position].subSequence(0, 1).charAt(0);
        //    Log.e("name of starting  ", headerText);
        holder.text.setText(headerText);
        try {

            if (((MainActivity) context).search) {

                rl_header.setVisibility(View.GONE);

            } else {
                rl_header.setVisibility(View.VISIBLE);
                /*  if (convertView == null) {*/

            }
        } catch (Exception e) {
            return convertView;
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

        return countri[position].subSequence(0, 1).charAt(0);

    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
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