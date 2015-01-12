package com.vertis.formbuilder;

import android.app.Activity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;

public class Email implements IField{
	
	private FieldConfig config;
	
	//Views
	LinearLayout em;
	TextView emailTextBox;
	EditText emailEditBox;
	
	//Values
	@Expose
	String cid;
	@Expose
	String emailid="";
	
	public Email(FieldConfig fcg){
		this.config=fcg;
	}	

	@Override
	public void createForm(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		em=(LinearLayout) inflater.inflate(R.layout.email,null);
		emailTextBox = (TextView) em.findViewById(R.id.textViewEmail);
		emailEditBox = (EditText) em.findViewById(R.id.editTextEmail);
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	private void noErrorMessage() {
		if(emailTextBox==null)return;
		emailTextBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		emailTextBox.setTextColor(-1);
	}
	
	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", em);
		ViewLookup.mapField(this.config.getCid()+"_1_1",emailTextBox);
		ViewLookup.mapField(this.config.getCid()+"_1_2", emailEditBox);
	}

	private void setViewValues() {
		emailTextBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		emailEditBox.setText(emailid);
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
		String emailString = emailEditBox.getText().toString();
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
		 return ! (emailCheck.contains("@")&&emailCheck.contains("."));
	}
	
	
	private void errorMessage(String message) {
		if(emailTextBox==null)return;
		emailTextBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		emailTextBox.setText(emailTextBox.getText() + message);
		emailTextBox.setTextColor(-65536);
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
}