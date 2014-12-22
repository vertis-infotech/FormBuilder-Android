package com.vertis.formbuilder;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

class Radio implements IField {
    private FieldConfig config;
	
	// Default Values 
	boolean otherRequired = false;
	String otherHeading = "";
	int defaultValueSelected = 0;
	ArrayList<String> options = new ArrayList<String>();

	// Views
	LinearLayout subForm;
	TextView headingText;
	RadioGroup radioGroup;
	EditText otherTextBox;

	@Expose
	String cid;
	@Expose
	String optionSelected;
	@Expose
	String other;

	public Radio(FieldConfig fcg){
		this.config=fcg;
	}

	public void createForm(Activity context) {
		subForm = new LinearLayout(context);
		ViewLookup.mapField(this.config.getCid()+"_1", subForm);
		subForm.setOrientation(LinearLayout.VERTICAL);
		headingText = new TextView(context);
		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		subForm.addView(headingText);
		radioGroup = new RadioGroup(context);
		ViewLookup.mapField(this.config.getCid()+"_1_1", radioGroup);
		subForm.addView(radioGroup);
		int i = 0;
		for (i = 0; i < options.size(); i++) {
			RadioButton button = new RadioButton(context);
			ViewLookup.mapField(this.config.getCid()+"_1_1_"+Integer.toString(i), radioGroup);
			button.setText(options.get(i));
			radioGroup.addView(button);
			if (i == defaultValueSelected)
				radioGroup.check(button.getId());

		}
		if (otherRequired) {
			otherTextBox = new EditText(context);
			subForm.addView(otherTextBox);
			RadioButton button = new RadioButton(context);
			ViewLookup.mapField(this.config.getCid()+"_1_1_"+Integer.toString(i), radioGroup);
			button.setText(otherHeading);
			radioGroup.addView(button);
			if (i == defaultValueSelected)
				radioGroup.check(button.getId());
			button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						otherTextBox.setVisibility(View.VISIBLE);
					} 
					else {
						otherTextBox.setVisibility(View.GONE);
					}
				}
			});
		}
	}

	public boolean validate() {
		boolean data = true;
		return data;
	}

	public ViewGroup getView() {
		return subForm;
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		RadioButton selectedButton=(RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
		optionSelected=selectedButton.getText().toString();

		if(otherRequired)
			other=otherTextBox.getText().toString();
		else
			other="";
	}

	@Override
	public void clearViews() {
		subForm=null;
		headingText=null;
		radioGroup=null;
		otherTextBox=null;
	}

};	