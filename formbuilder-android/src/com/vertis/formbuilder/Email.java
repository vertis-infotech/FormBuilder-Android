package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
		emailEditBox.setTypeface(getFontFromRes(R.raw.roboto, context));
		emailTextBox.setTypeface(getFontFromRes(R.raw.roboto, context));
		emailEditBox.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		emailTextBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		emailTextBox.setTextColor(Color.BLACK);
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	private Typeface getFontFromRes(int resource, Context context)
	{ 
		Typeface tf = null;
		InputStream is = null;
		try {
			is = context.getResources().openRawResource(resource);
		}
		catch(NotFoundException e) {
		}
		String outPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis()+".raw";
		try
		{
			byte[] buffer = new byte[is.available()];
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));
			int l = 0;
			while((l = is.read(buffer)) > 0)
				bos.write(buffer, 0, l);
			bos.close();
			tf = Typeface.createFromFile(outPath);
			new File(outPath).delete();
		}
		catch (IOException e)
		{
			return null;
		}
		return tf;      
	}
	
	private void noErrorMessage() {
		if(emailTextBox==null)return;
		emailTextBox.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		emailTextBox.setTextColor(Color.BLACK);
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