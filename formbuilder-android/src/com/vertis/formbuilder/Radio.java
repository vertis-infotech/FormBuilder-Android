package com.vertis.formbuilder;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

class Radio implements IField {
	private FieldConfig config;
	// Default Values 
	boolean otherRequired = true;
	String otherHeading = "";
	// Views
	LinearLayout subForm;
	TextView headingText;
	RadioGroup radioGroup;

	//Values
	@Expose
	String cid;
	@Expose
	String optionSelected = "";
	@Expose
	String other;

	public Radio(FieldConfig fcg){
		this.config=fcg;
	}

	public void createForm(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		subForm=(LinearLayout) inflater.inflate(R.layout.radio,null);
		headingText = (TextView) subForm.findViewById(R.id.tvRadio);
		otherHeading="other";	
		radioGroup = new RadioGroup(context);
		ViewLookup.mapField(this.config.getCid()+"_1_1", radioGroup);
		subForm.addView(radioGroup);
		radioGroup.clearCheck();
		subForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subForm.setFocusableInTouchMode(true);
				subForm.setFocusable(true);
				subForm.requestFocus();
			}
		});
		int i = 0;
		for (i = 0; i < this.config.getField_options().getOptions().size(); i++) {
			addButton(i, context);
		}		
		mapView();
		setViewValues();
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1",subForm);
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			ViewLookup.mapField(this.config.getCid()+"_1_"+i+1, radioGroup.getChildAt(i));
		}
	}

	private void setViewValues(){
		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":""));
		headingText.setTextColor(Color.BLACK);
	}

	void addButton(int i , Activity context){
		RadioButton button = new RadioButton(context);
		button.setId(i);
		ViewLookup.mapField(this.config.getCid()+"_1_1_"+Integer.toString(i), radioGroup);
		if(i < this.config.getField_options().getOptions().size()){
			button.setText(this.config.getField_options().getOptions().get(i).getLabel());
		}
		button.setButtonDrawable(R.drawable.radio_custom);
		radioGroup.addView(button);
		if(optionSelected!= null && !TextUtils.isEmpty(optionSelected)&& this.config.getField_options().getOptions().get(i).getLabel().equals(optionSelected))
			radioGroup.check(button.getId());
	}

	public boolean validate() {
		boolean valid;
		if(TextUtils.isEmpty(optionSelected)){
			valid=false;
			errorMessage(" Pick one!");
		} else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	public ViewGroup getView() {
		return subForm;
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(subForm!=null){
			RadioButton selectedButton=(RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
			if(selectedButton != null){
				optionSelected=selectedButton.getText().toString();
			}
		}
		validate();
	}

	@SuppressLint("ResourceAsColor")
	public void errorMessage(String message){
		if(headingText==null)return;
		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		headingText.setText(headingText.getText() + " " + message);
		headingText.setTextColor(R.color.ErrorMessage);
	}

	@SuppressLint("ResourceAsColor")
	public void noErrorMessage(){
		if(headingText==null)return;
		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		headingText.setTextColor(R.color.TextViewNormal);
	}
	@Override
	public void clearViews() {
		setValues();
		subForm=null;
		headingText=null;
		radioGroup=null;
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(subForm!=null){
			subForm.setVisibility(View.GONE);
			subForm.invalidate();
		}
	}

	@Override
	public void showField() {
		if(subForm!=null){
			subForm.setVisibility(View.VISIBLE);
			subForm.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(optionSelected.toLowerCase().equals(value.toLowerCase()) || optionSelected.equals("")){
				return true;
			}
			return false;
		}
		return true;
	}

    public boolean isHidden(){
        if(subForm!=null) {
            return !subForm.isShown();
        } else {
            return false;
        }
    }
};