package com.vertis.formbuilder;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CountriesArrayAdapter extends ArrayAdapter<SelectElement> {

	public CountriesArrayAdapter(Context context,ArrayList<SelectElement> CountryList) {
		super(context, android.R.layout.simple_spinner_dropdown_item, CountryList);
	}
}
