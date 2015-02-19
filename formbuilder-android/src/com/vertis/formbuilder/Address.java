package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.IField;
import com.vertis.formbuilder.parser.FieldConfig;

public class Address implements IField {

	private FieldConfig config;

	//Views
	LinearLayout ad;
	EditText streetEditText;
	TextView streetTextView;
	LinearLayout line2;
	EditText cityEditText;
	TextView cityTextView;
	EditText stateEditText;
	TextView stateTextView;
	LinearLayout line3;
	EditText zipEditText;
	TextView zipTextView;
	Spinner countriesSpinner;
	TextView countryTextView;
	TextView title;

	//Values
	@Expose
	String cid;
	@Expose
	String country="";
	int countryPosition=0;
	@Expose
	String street="";
	@Expose
	String city="";
	@Expose
	String state="";
	@Expose
	String zip="";

	//constructor to populate config
	public Address(FieldConfig fcg){
		this.config=fcg;
	}

	@Override
	public void createForm(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		ad=(LinearLayout) inflater.inflate(R.layout.address,null);
		streetTextView = ( TextView) ad.findViewById(R.id.textViewStreet);
		streetEditText = (EditText) ad.findViewById(R.id.editTextStreet);
		line2=(LinearLayout) inflater.inflate(R.layout.address,null);
		cityEditText = (EditText) ad.findViewById(R.id.editTextCity);
		cityTextView = (TextView) ad.findViewById(R.id.textViewCity);
		stateEditText = (EditText) ad.findViewById(R.id.editTextState);
		stateTextView = (TextView) ad.findViewById(R.id.textViewState);
		line3=(LinearLayout) inflater.inflate(R.layout.address,null);
		zipTextView = (TextView) ad.findViewById(R.id.textViewZip);
		zipEditText = (EditText) ad.findViewById(R.id.editTextZip);
		countriesSpinner =(Spinner) ad.findViewById(R.id.Country);
		countryTextView= (TextView) ad.findViewById(R.id.textViewCountry);
		title= (TextView) ad.findViewById(R.id.textViewAddress);
		title.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		title.setTypeface(getFontFromRes(R.raw.roboto, context));
		streetTextView.setTypeface(getFontFromRes(R.raw.roboto, context));
		streetEditText.setTypeface(getFontFromRes(R.raw.roboto, context));
		cityTextView.setTypeface(getFontFromRes(R.raw.roboto, context));
		cityEditText.setTypeface(getFontFromRes(R.raw.roboto, context));
		stateTextView.setTypeface(getFontFromRes(R.raw.roboto, context));
		stateEditText.setTypeface(getFontFromRes(R.raw.roboto, context));
		zipTextView.setTypeface(getFontFromRes(R.raw.roboto, context));
		zipEditText.setTypeface(getFontFromRes(R.raw.roboto, context));
		countryTextView.setTypeface(getFontFromRes(R.raw.roboto, context));
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		streetTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		cityTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		stateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		zipTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		countryTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		streetEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		cityEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		stateEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		zipEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessageStreet();
		noErrorMessageCity();
		noErrorMessageState();
		noErrorMessageZip();
		noErrorMessageCountry();
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
	
	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", ad);
		ViewLookup.mapField(this.config.getCid()+"_1_1",streetEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_2",cityEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_3",stateEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_4",zipEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_5",countriesSpinner);
	}
	private void setViewValues() {
		streetEditText.setText(street);
		cityEditText.setText(city);
		stateEditText.setText(state);
		zipEditText.setText(zip);
		countriesSpinner.setSelection(countryPosition);
	}




	private ArrayList<SelectElement> getCountryList() {
		Locale[] locale = Locale.getAvailableLocales();
		ArrayList<SelectElement> countries = new ArrayList<SelectElement>();
		String country;
		int i = 0;
		for (Locale loc : locale) {
			country = loc.getDisplayCountry();
			if (country.length() > 0 && !countries.contains(country)) {
				if (!countries.contains(new SelectElement(i, loc.getCountry(),
						loc.getDisplayCountry(), 1))) {
					countries.add(new SelectElement(i, loc.getCountry(), loc
							.getDisplayCountry(), 1));
				}
			}
			i = i++;
		}
		Collections.sort(countries);
		return countries;
	}

	private void defineViewSettings(Activity context) {
		streetEditText.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessageStreet();
				else
					setValues();
			}
		});		
		cityEditText.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessageCity();
				else
					setValues();
			}
		});
		stateEditText.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessageState();
				else
					setValues();
			}
		});
		zipEditText.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessageZip();
				else
					setValues();
			}
		});
		ArrayAdapter<SelectElement> adapter = getAdapter(context);
		countriesSpinner.setAdapter(adapter);
	}

	private ArrayAdapter<SelectElement> getAdapter(Context context) {
		return new CountriesArrayAdapter(context, getCountryList());
	}

	@Override
	public ViewGroup getView() {
		return ad;
	}
	
	@Override
	public boolean validate() {
		boolean valid = false;
		try {
			if(config.getRequired()&&street.equals("")){
				valid=false;
				errorMessageStreet(" Street Required");
			}  else{
				valid=true;
				noErrorMessageStreet();
			} 
			if(config.getRequired()&&city.equals("")) {
				valid=false;
				errorMessageCity(" City Required");
			}  else{
				valid=true;
				noErrorMessageCity();
			}
			if(config.getRequired()&&state.equals("")) {
				valid=false;
				errorMessageState(" State Required");
			}  else{
				valid=true;
				noErrorMessageState();
			}
			if(config.getRequired()&&zip.equals("")) {
				valid=false;
				errorMessageZip(" Zip Required");
			}  else{
				valid=true;
				noErrorMessageZip();
			}
			if(config.getRequired()&&country.equals("")) {
				valid=false;
				errorMessageCountry(" Country Required");
			} else{
				valid=true;
				noErrorMessageCountry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessageStreet(String message) {
		if(streetTextView==null)return;
		streetTextView.setText((this.config.getRequired()?"*":"") );
		streetTextView.setText(streetTextView.getText() + message);
		streetTextView.setTextColor(R.color.ErrorMessage);
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessageCity(String message) {
		if(cityTextView==null)return;
		cityTextView.setText((this.config.getRequired()?"*":"") );
		cityTextView.setText(cityTextView.getText() + message);
		cityTextView.setTextColor(R.color.ErrorMessage);
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessageState(String message) {
		if(stateTextView==null)return;
		stateTextView.setText((this.config.getRequired()?"*":"") );
		stateTextView.setText(stateTextView.getText() + message);
		stateTextView.setTextColor(R.color.ErrorMessage);
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessageZip(String message) {
		if(countryTextView==null)return;
		zipTextView.setText((this.config.getRequired()?"*":"") );
		zipTextView.setText(zipTextView.getText() + message);
		zipTextView.setTextColor(R.color.ErrorMessage);
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessageCountry(String message) {
		if(countryTextView==null)return;
		countryTextView.setText((this.config.getRequired()?"*":"") );
		countryTextView.setText(countryTextView.getText() + message);
		countryTextView.setTextColor(R.color.ErrorMessage);
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessageStreet() {
		if(streetTextView==null)return;
		streetTextView.setText(this.config.getRequired()?"Street*":"" );
		streetTextView.setTextColor(R.color.TextViewNormal);
	}
	
	@SuppressLint("ResourceAsColor")
	private void noErrorMessageCity() {
		if(cityTextView==null)return;
		cityTextView.setText(this.config.getRequired()?"City*":"" );
		cityTextView.setTextColor(R.color.TextViewNormal);
	}
	
	@SuppressLint("ResourceAsColor")
	private void noErrorMessageState() {
		if(stateTextView==null)return;
		stateTextView.setText(this.config.getRequired()?"State*":"" );
		stateTextView.setTextColor(R.color.TextViewNormal);
	}
	
	@SuppressLint("ResourceAsColor")
	private void noErrorMessageZip() {
		if(zipTextView==null)return;
		zipTextView.setText(this.config.getRequired()?"Zip*":"" );
		zipTextView.setTextColor(R.color.TextViewNormal);
	}
	
	@SuppressLint("ResourceAsColor")
	private void noErrorMessageCountry() {
		if(countryTextView==null)return;
		countryTextView.setText(this.config.getRequired()?"Country*":"" );
		countryTextView.setTextColor(R.color.TextViewNormal);
	}
	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(ad!=null){
			street=streetEditText.getText().toString();
			city=cityEditText.getText().toString();
			state=stateEditText.getText().toString();
			zip=zipEditText.getText().toString();
			countryPosition=countriesSpinner.getSelectedItemPosition();
			country=countriesSpinner.getSelectedItem().toString();
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		ad=null;
		streetEditText=null;
		streetTextView=null;
		line2=null;
		cityEditText=null;
		cityTextView=null;
		stateEditText=null;
		stateTextView=null;
		line3=null;
		zipEditText=null;
		zipTextView=null;
		countriesSpinner=null;
		countryTextView=null;
	}
}