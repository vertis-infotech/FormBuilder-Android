package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Listeners.TextChangeListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
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

public class Contact implements IField{

	private FieldConfig config;
	private static final String contactPattern = "^[0-9]+$";
	//Views
	LinearLayout llContact;
	TextView tvContact;
	EditText etContact;

	//Values
	@Expose
	String cid;
	@Expose
	String contactNo="10";
	private Typeface font;

	public Contact(FieldConfig fcg){
		this.config=fcg;
	}	

	@SuppressLint("ResourceAsColor")
	@Override
	public void createForm(Activity context) {
		font = new FormBuilderUtil().getFontFromRes(context);
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llContact=(LinearLayout) inflater.inflate(R.layout.contact,null);
		tvContact = (TextView) llContact.findViewById(R.id.textViewContact);
		etContact = (EditText) llContact.findViewById(R.id.editTextContact);
		setTextTypefaseAndTextSize(etContact, 12.5f);
		setTextTypefaseAndTextSize(tvContact, 14);
		tvContact.setTextColor(R.color.TextViewNormal);
		
		etContact.addTextChangedListener(new TextChangeListener(config));
		
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	public void setTextTypefaseAndTextSize(TextView view, float size) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		view.setTypeface(font);
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(tvContact==null)return;
		tvContact.setText(this.config.getLabel() + (this.config.getRequired() ? "*" : ""));
		tvContact.setTextColor(R.color.TextViewNormal);
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessage(String message) {
		if(tvContact==null)return;
		tvContact.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvContact.setText(tvContact.getText() + message);
		tvContact.setTextColor(R.color.ErrorMessage);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid() + "_1", llContact);
		ViewLookup.mapField(this.config.getCid() + "_1_1", etContact);
	}

	private void setViewValues() {
		tvContact.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		etContact.setText(contactNo);
		tvContact.setTextColor(-1);
	}

	private void defineViewSettings(Activity context) {
		etContact.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					noErrorMessage();
				else
					setValues();
			}
		});
	}

	@Override
	public ViewGroup getView() {
		return llContact;
	}

	@Override
	public boolean validate() {
		boolean valid;
		String contactString = contactNo;
		if(config.getRequired() && contactString.equals("")){
			valid=false;
			errorMessage(" Required");
		} else if(contactValidation(contactString)){
			valid=false;
			errorMessage(" Invalid entry");
		} else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	private boolean contactValidation(String contactCheck) { 
		Pattern pattern = Pattern.compile(contactPattern);
		Matcher matcher = pattern.matcher(contactCheck);
		return !matcher.matches();
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(llContact!=null){
			contactNo=etContact.getText().toString();
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		llContact=null;
		tvContact=null;
		etContact=null;
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(llContact!=null){
			llContact.setVisibility(View.GONE);
			llContact.invalidate();
		}
	}

	@Override
	public void showField() {
		if(llContact!=null){
			llContact.setVisibility(View.VISIBLE);
			llContact.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(contactNo.equals(value) || contactNo.equals("")){
				return true;
			}
			return false;
		}
		return false;
	}

    public boolean isHidden(){
        if(llContact!=null) {
            return !llContact.isShown();
        } else {
            return false;
        }
    }
}