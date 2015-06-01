package com.vertis.formbuilder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;
import com.vertis.formbuilder.util.FormBuilderUtil;

public class NumberField implements IField{

	private FieldConfig config;
	//Views
	LinearLayout llNumber;
	TextView tvNumber;
	EditText etNumber;

	//Values
	@Expose
	String cid;
	@Expose
	String number="";
	private Typeface font;

	public NumberField(FieldConfig fcg){
		this.config=fcg;
	}	

	@SuppressLint("ResourceAsColor")
	@Override
	public void createForm(Activity context) {
		font = new FormBuilderUtil().getFontFromRes(context);
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llNumber=(LinearLayout) inflater.inflate(R.layout.number_edit_text,null);
		tvNumber = (TextView) llNumber.findViewById(R.id.numberTextView);
		etNumber = (EditText) llNumber.findViewById(R.id.numberEditText);
		etNumber.setTypeface(font);
		tvNumber.setTypeface(font);
		etNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		tvNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tvNumber.setTextColor(R.color.TextViewNormal);
		etNumber.addTextChangedListener(new CustomTextChangeListener(config));

		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}
	
	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(tvNumber==null)return;
		tvNumber.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvNumber.setTextColor(R.color.TextViewNormal);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", llNumber);
		ViewLookup.mapField(this.config.getCid()+"_1_1", etNumber);
	}

	private void setViewValues() {
		tvNumber.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		etNumber.setText(number);
		tvNumber.setTextColor(-1);
	}

	private void defineViewSettings(Activity context) {
		etNumber.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessage();
				else
					setValues();
			}
		});
	}

	@Override
	public ViewGroup getView() {
		return llNumber;
	}

	@Override
	public boolean validate() {
		boolean valid;
		if(config.getRequired() && number.equals("")){
			valid=false;
			errorMessage(" Required");
		}
		else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessage(String message) {
		if(tvNumber==null)return;
		tvNumber.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvNumber.setText(tvNumber.getText() + message);
		tvNumber.setTextColor(R.color.ErrorMessage);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(llNumber!=null){
			number=etNumber.getText().toString();
		}
		validate();

	}

	@Override
	public void clearViews() {
		setValues();
		llNumber=null;
		tvNumber=null;
		etNumber=null;
	}

	@Override
	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(llNumber!=null){
			llNumber.setVisibility(View.GONE);
			llNumber.invalidate();
		}
	}

	@Override
	public void showField() {
		if(llNumber!=null){
			llNumber.setVisibility(View.VISIBLE);
			llNumber.invalidate();
		}
	}

	@Override
	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(number.equals(value) || number.equals("")){
				return true;
			}
		} else if(condition.equals("is greater than")){
			if(Integer.parseInt(number) > Integer.parseInt(value) || number.equals("")){
				return true;
			}
		} else if(condition.equals("is less than")){
			if(Integer.parseInt(number) < Integer.parseInt(value) || number.equals("")){
				return true;
			}
		}
		return false;
	}

    public boolean isHidden(){
        if(llNumber!=null) {
            return !llNumber.isShown();
        } else {
            return false;
        }
    }
}