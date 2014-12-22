package com.vertis.formbuilder;

import com.vertis.formbuilder.parser.FieldConfig;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class Section implements IField {
	Activity context;
	FieldConfig config;

	public Section(FieldConfig fcg){
		config=fcg;
	}

	@Override
	public void createForm(Activity context) {
	this.context=context;
	}

	@Override
	public ViewGroup getView() {
		LinearLayout emptyLayout = new LinearLayout(context);
		emptyLayout.setLayoutParams(new LayoutParams(0, 0));
		return emptyLayout;
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
