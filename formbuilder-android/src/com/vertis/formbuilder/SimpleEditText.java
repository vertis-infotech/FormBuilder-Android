package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;

public class SimpleEditText implements IField{

	private FieldConfig config;
	//Views
	LinearLayout llEditText;
	TextView tvEditText;
	EditText etEditText;

	//Values
	@Expose
	String cid;
	@Expose
	String text="";

	public SimpleEditText(FieldConfig fcg){
		this.config=fcg;
	}	

	@SuppressLint("ResourceAsColor")
	@Override
	public void createForm(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llEditText=(LinearLayout) inflater.inflate(R.layout.simple_edit_text,null);
		tvEditText = (TextView) llEditText.findViewById(R.id.simpleTextView);
		etEditText = (EditText) llEditText.findViewById(R.id.simpleEditText);
		etEditText.setTypeface(getFontFromRes(R.raw.roboto, context));
		tvEditText.setTypeface(getFontFromRes(R.raw.roboto, context));
		etEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		tvEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tvEditText.setTextColor(R.color.TextViewNormal);
		
		etEditText.addTextChangedListener(new TextChangeListener(config));
		
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

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(tvEditText==null)return;
		tvEditText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvEditText.setTextColor(R.color.TextViewNormal);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", llEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_1", etEditText);
	}

	private void setViewValues() {
		tvEditText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		etEditText.setText(text);
		tvEditText.setTextColor(-1);
	}

	private void defineViewSettings(Activity context) {
		etEditText.setOnFocusChangeListener( new OnFocusChangeListener() {
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
		return llEditText;
	}

	@Override
	public boolean validate() {
		boolean valid;
		if(config.getRequired() && text.equals("")){
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
		if(tvEditText==null)return;
		tvEditText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvEditText.setText(tvEditText.getText() + message);
		tvEditText.setTextColor(R.color.ErrorMessage);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(llEditText!=null){
			text=etEditText.getText().toString();
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		llEditText=null;
		tvEditText=null;
		etEditText=null;
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(llEditText!=null){
			llEditText.setVisibility(View.GONE);
			llEditText.invalidate();
		}
	}

	@Override
	public void showField() {
		if(llEditText!=null){
			llEditText.setVisibility(View.VISIBLE);
			llEditText.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(text.toLowerCase().equals(value.toLowerCase()) || text.equals("")){
				return true;
			}
			return false;
		}
		return true;
	}

    public boolean isHidden(){
        if(llEditText!=null) {
            return !llEditText.isShown();
        } else {
            return false;
        }
    }
}