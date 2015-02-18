package com.vertis.formbuilder;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CountriesArrayAdapter extends ArrayAdapter<SelectElement> {
	ArrayList<SelectElement> CountryList;
	public CountriesArrayAdapter(Context context,ArrayList<SelectElement> CountryList) {
		super(context, android.R.layout.simple_list_item_1, CountryList);
		this.CountryList = CountryList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v= convertView;
		if (v == null) {
		      LayoutInflater layoutInf = (LayoutInflater) getContext()
		          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      v = layoutInf.inflate(R.layout.spinner_textview, null);
		    }
		TextView tv = (TextView)v.findViewById(android.R.id.text1);
		tv.setText(this.CountryList.get(position).value);
		return v;
	}	
}
