package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

public class Email implements IField{

	private FieldConfig config;
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
	//Views
	LinearLayout em;
	TextView emailTextBox;
	EditText emailEditBox;
	ArrayList Conditions;

	//Values
	@Expose
	String cid;
	@Expose
	String emailid="";
	private Typeface font;

	public Email(FieldConfig fcg){
		this.config=fcg;
	}	

	@SuppressLint("ResourceAsColor")
	@Override
	public void createForm(Activity context) {
		font = new FormBuilderUtil().getFontFromRes(context);
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		em=(LinearLayout) inflater.inflate(R.layout.email,null);
		emailTextBox = (TextView) em.findViewById(R.id.textViewEmail);
		emailEditBox = (EditText) em.findViewById(R.id.editTextEmail);
		emailEditBox.setTypeface(font);
		emailTextBox.setTypeface(font);
		emailEditBox.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		emailTextBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		emailTextBox.setTextColor(R.color.TextViewNormal);
		
		emailEditBox.addTextChangedListener(new TextChangeListener(config));
		
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(emailTextBox==null)return;
		emailTextBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		emailTextBox.setTextColor(R.color.TextViewNormal);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", em);
		ViewLookup.mapField(this.config.getCid()+"_1_1", emailEditBox);
	}

	private void setViewValues() {
		emailTextBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		emailEditBox.setText(emailid);
		emailTextBox.setTextColor(-1);
	}

	private void defineViewSettings(Activity context) {
		emailEditBox.setOnFocusChangeListener( new OnFocusChangeListener() {
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
		return em;
	}

	@Override
	public boolean validate() {
		boolean valid;
		String emailString = emailid;
		if(config.getRequired() && emailString.equals("")){
			valid=false;
			errorMessage(" Required");
		} else if(emailValidation(emailString)){
			valid=false;
			errorMessage(" Invalid entry");
		} else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	private boolean emailValidation(String emailCheck) { 
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(emailCheck);
		return !matcher.matches();
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessage(String message) {
		if(emailTextBox==null)return;
		emailTextBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		emailTextBox.setText(emailTextBox.getText() + message);
		emailTextBox.setTextColor(R.color.ErrorMessage);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(em!=null){
			emailid=emailEditBox.getText().toString();
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		em=null;
		emailTextBox=null;
		emailEditBox=null;
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(em!=null){
			em.setVisibility(View.GONE);
			em.invalidate();
		}
	}

	@Override
	public void showField() {
		if(em!=null){
			em.setVisibility(View.VISIBLE);
			em.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(emailid.equals(value) || emailid.equals("")){
				return true;
			}
			return false;
		}
		return true;
	}

    public boolean isHidden(){
        if(em!=null) {
            return !em.isShown();
        } else {
            return false;
        }
    }
}