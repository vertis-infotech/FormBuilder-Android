package com.vertis.formbuilder;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;
import com.vertis.formbuilder.util.FormBuilderUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Checkbox implements IField {

	private FieldConfig config;
	// Views
	LinearLayout llCheckBox;
	TextView tvCheckBox;
	EditText otherTextBox;
	ArrayList<String> checkedValues= new ArrayList<String>(); 
	ArrayList<CheckBox> cbValues = new ArrayList<CheckBox>();

	//Values
	@Expose
	String cid;
	@Expose
	String optionSelected;
	int checked = 0;
	@Expose
	String other;

	public Checkbox(FieldConfig fcg){
		this.config=fcg;
	}

	@Override
	public void createForm(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llCheckBox=(LinearLayout) inflater.inflate(R.layout.checkbox,null);
		tvCheckBox = (TextView) llCheckBox.findViewById(R.id.tvCheckbox);
		tvCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
		tvCheckBox.setTypeface(new FormBuilderUtil().getFontFromRes(context));
		int i = 0;
		if(cbValues != null)
			cbValues.clear();
		for (i = 0; i < this.config.getField_options().getOptions().size(); i++) {
			addBox(i, context);
		}
		llCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				llCheckBox.setFocusableInTouchMode(true);
				llCheckBox.setFocusable(true);
				llCheckBox.requestFocus();
			}
		});
		mapview();
		setViewValues();
	}

	private void mapview() {
		int j=1;
		ViewLookup.mapField(this.config.getCid()+"_1",llCheckBox);
		for (CheckBox i : cbValues) {
			ViewLookup.mapField(this.config.getCid()+"_1_"+j,i);
			j++;
		}
	}

	private void setViewValues() {
		tvCheckBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":""));
		tvCheckBox.setTextColor(Color.BLACK);
	}

	public void addBox(int i, Activity context) {
		CheckBox box = new CheckBox(context);
		ViewLookup.mapField(this.config.getCid()+"_1_1_"+Integer.toString(i), llCheckBox);
		if(i < this.config.getField_options().getOptions().size()){
			box.setText(this.config.getField_options().getOptions().get(i).getLabel());
			box.setChecked(this.config.getField_options().getOptions().get(i).getChecked());
		}
		if(checkedValues.contains(this.config.getField_options().getOptions().get(i).getLabel())){
			box.setChecked(true);
		}
		llCheckBox.addView(box);
		box.setButtonDrawable(R.drawable.check_custom);
		cbValues.add(box);
	}

	@Override
	public ViewGroup getView() {
		return llCheckBox;
	}

	@Override
	public boolean validate() {
		boolean valid;
		if(checkedValues.isEmpty()){
			valid=false;
			errorMessage(" Pick one!");
		} else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(tvCheckBox==null)return;
		tvCheckBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvCheckBox.setTextColor(R.color.TextViewNormal);		
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessage(String message) {
		if(tvCheckBox==null)return;
		tvCheckBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
//		tvCheckBox.setText(tvCheckBox.getText() + " " + message);
		tvCheckBox.setTextColor(R.color.ErrorMessage);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(llCheckBox!=null){
			checkedValues.clear();
			for (CheckBox tempCheck : cbValues) {
				if(tempCheck.isChecked()){
					checkedValues.add(tempCheck.getText().toString());
				}
			}
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		llCheckBox=null;
		tvCheckBox=null;		
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(llCheckBox!=null){
			llCheckBox.setVisibility(View.GONE);
			llCheckBox.invalidate();
		}
	}

	@Override
	public void showField() {
		if(llCheckBox!=null){
			llCheckBox.setVisibility(View.VISIBLE);
			llCheckBox.invalidate();
		}
	}

	@Override
	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			for (String checkedValue : checkedValues) {
				if(checkedValue.toLowerCase().equals(value.toLowerCase())){
				}
			}
			return false;
		}
		return true;
	}

    public boolean isHidden(){
        if(llCheckBox!=null) {
            return !llCheckBox.isShown();
        } else {
            return false;
        }
    }
}