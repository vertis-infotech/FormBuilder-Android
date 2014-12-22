package com.vertis.formbuilder;

import com.vertis.formbuilder.parser.FieldConfig;

import android.app.Activity;
import android.view.ViewGroup;

public class Section implements IField {

	FieldConfig config;
	
	public Section(FieldConfig fcg){
		config=fcg;
	}
	
	@Override
	public void createForm(Activity context) {
	}

	@Override
	public ViewGroup getView() {
		return null;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void setValues() {
	}

	@Override
	public void clearViews() {
		
	}

}
