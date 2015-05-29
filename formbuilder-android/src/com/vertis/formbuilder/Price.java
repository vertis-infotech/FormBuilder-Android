package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;
import com.vertis.formbuilder.util.FormBuilderUtil;

import Listeners.TextChangeListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Price implements IField {

	//config contains all data from json ie id,sid,label,field_options
	private FieldConfig config;
	private Typeface font;

	public Price(FieldConfig fcg){
		this.config=fcg;
	}

	//Views
	LinearLayout llPrice;
	TextView tvPrice;
	EditText etDollars;
	EditText etCents;

	//Default values
	String dollarsHint="Dollars";
	String centsHint="Cents";

	@Expose
	String cid;
	@Expose
	String dollars="";
	@Expose
	String cents="";
	@Expose
	String totalPrice;



	@Override
	public void createForm(Activity context) {
		font = new FormBuilderUtil().getFontFromRes(context);
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llPrice=(LinearLayout) inflater.inflate(R.layout.price,null);
		tvPrice = (TextView) llPrice.findViewById(R.id.textViewPrice);
		etDollars = (EditText) llPrice.findViewById(R.id.dollars);
		etCents = (EditText) llPrice.findViewById(R.id.cents);
		tvPrice.setTypeface(font);
		etDollars.setTypeface(font);
		etCents.setTypeface(font);
		tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		etDollars.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		etCents.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		
		addTextChangeListeners();
		
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	private void addTextChangeListeners() {
		etDollars.addTextChangedListener(new CustomTextChangeListener(config));
		etCents.addTextChangedListener(new CustomTextChangeListener(config));
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(tvPrice==null)return;
		tvPrice.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvPrice.setTextColor(R.color.TextViewNormal);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", llPrice);
		ViewLookup.mapField(this.config.getCid()+"_1_1", tvPrice);
		ViewLookup.mapField(this.config.getCid()+"_1_2", etDollars);
		ViewLookup.mapField(this.config.getCid()+"_1_3", etCents);
	}

	private void setViewValues() {
		tvPrice.setText(this.config.getLabel());
		etDollars.setText(dollars);
		etCents.setText(cents);
	}

	private void defineViewSettings(Activity context) {
		etDollars.setHint(dollarsHint);
		etDollars.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessage();
				else
					setValues();
			}
		});
		etCents.setHint(centsHint);
		etCents.setOnFocusChangeListener( new OnFocusChangeListener() {
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
		return llPrice;
	}

	@Override
	public boolean validate() {
		boolean valid;
		if(config.getRequired()&&dollars.equals("")){
			valid=false;
			errorMessage("Required");
		}
		//else if(Integer.parseInt(cents)>99){
		//errorMessage("Value of Cents cannot be more than 99");
		//valid=false;
		//}
		else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	private void errorMessage(String message) {
		if(tvPrice==null)return;
		tvPrice.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvPrice.setText(tvPrice.getText() + " " + message);
		tvPrice.setTextColor(-65536);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(llPrice!=null){
			dollars=etDollars.getText().toString();
			cents=etCents.getText().toString();
			totalPrice=dollars+"."+cents;
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		llPrice=null;
		tvPrice=null;
		etDollars=null;
		etCents=null;
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(llPrice!=null){
			llPrice.setVisibility(View.GONE);
			llPrice.invalidate();
		}
	}

	@Override
	public void showField() {
		if(llPrice!=null){
			llPrice.setVisibility(View.VISIBLE);
			llPrice.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(totalPrice.equals(value) || totalPrice.equals("")){
				return true;
			}
		} else if(condition.equals("is greater than")){
			if(Integer.parseInt(totalPrice)>Integer.parseInt(value) || totalPrice.equals("")){
				return true;
			}
		} else if(condition.equals("is less than")){
			if(Integer.parseInt(totalPrice)<Integer.parseInt(value) || totalPrice.equals("")){
				return true;
			}
		}
		return false;
	}

    public boolean isHidden(){
        if(llPrice!=null) {
            return !llPrice.isShown();
        } else {
            return false;
        }
    }
}