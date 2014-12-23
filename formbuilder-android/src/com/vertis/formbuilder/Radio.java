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
	boolean otherRequired = true;
	String otherHeading = "";
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
	int positionSelected = 0;
	@Expose
	String other;

	public Radio(FieldConfig fcg){
		this.config=fcg;
	}

	public void createForm(Activity context) {
		options.add("male");
		options.add("female");
		otherHeading="other";
		
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
			addButton(i, context);
		}
		if (otherRequired) {
			addButton(i, context);
			otherTextBox = new EditText(context);
			otherTextBox.setText(other);
			subForm.addView(otherTextBox);
		}
	}

	void addButton(int i , Activity context){
		RadioButton button = new RadioButton(context);
		ViewLookup.mapField(this.config.getCid()+"_1_1_"+Integer.toString(i), radioGroup);
		button.setText(options.get(i));
		radioGroup.addView(button);
		if (i == positionSelected)
			radioGroup.check(button.getId());
		if(i==options.size()){
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
		return true;
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
		
		for(int i=0 ; i < options.size() ; i++ ){
			if(options.get(i).equals(optionSelected)){
				positionSelected=i;break;
				}
		}
	}


	public void errorMessage(String message){
		if(headingText==null)return;

		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		headingText.setText(headingText.getText() + " " + message);
		headingText.setTextColor(-65536);
	}

	public void noErrorMessage(){
		if(headingText==null)return;

		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		headingText.setTextColor(-1);
	}
	@Override
	public void clearViews() {
		setValues();

		subForm=null;
		headingText=null;
		radioGroup=null;
		otherTextBox=null;
	}
};	