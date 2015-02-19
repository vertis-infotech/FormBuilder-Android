package com.vertis.formbuilder;

import com.google.gson.annotations.Expose;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Price implements IField {

	LinearLayout llPrice;
	TextView tvPrice;
	EditText etDollars;
	EditText etCents;
	
	@Expose
	String cid;
	@Expose
	String dollars="";
	@Expose
	String cents="";
	
	
	
	@Override
	public void createForm(Activity context) {
		
	}

	@Override
	public ViewGroup getView() {
		return null;
	}

	@Override
	public boolean validate() {
		return false;
	}

	@Override
	public void setValues() {
		
	}

	@Override
	public void clearViews() {
		
	}
}
