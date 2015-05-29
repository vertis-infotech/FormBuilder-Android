package com.vertis.formbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import Listeners.TextChangeListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.vertis.formbuilder.parser.FieldConfig;
import com.vertis.formbuilder.util.FormBuilderUtil;

public class Address implements IField {

	private FieldConfig config;

	//Views
	LinearLayout ad;
	EditText streetEditText;
	TextView streetTextView;
	EditText cityEditText;
	TextView cityTextView;
	EditText stateEditText;
	TextView stateTextView;
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
	
	String fullAddress="";
	private Typeface mFont;

	//constructor to populate config
	public Address(FieldConfig fcg){
		this.config=fcg;
	}

	@Override
	public void createForm(Activity context) {
		mFont = new FormBuilderUtil().getFontFromRes(context);
		getViesByID(context);
		setTextSizeAndTypeFace();
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
	}

	public void getViesByID(Activity context) {
		LayoutInflater inflater = context.getLayoutInflater();
		ad=(LinearLayout) inflater.inflate(R.layout.address,null);
		streetTextView = ( TextView) ad.findViewById(R.id.textViewStreet);
		cityTextView = (TextView) ad.findViewById(R.id.textViewCity);
		stateTextView = (TextView) ad.findViewById(R.id.textViewState);
		zipEditText = (EditText) ad.findViewById(R.id.editTextZip);
		countryTextView= (TextView) ad.findViewById(R.id.textViewCountry);
		title= (TextView) ad.findViewById(R.id.textViewAddress);
		streetEditText = (EditText) ad.findViewById(R.id.editTextStreet);
		cityEditText = (EditText) ad.findViewById(R.id.editTextCity);
		stateEditText = (EditText) ad.findViewById(R.id.editTextState);
		zipTextView = (TextView) ad.findViewById(R.id.textViewZip);
		countriesSpinner =(Spinner) ad.findViewById(R.id.Country);
	}

	private void setTextSizeAndTypeFace() {
		setTextSize(title, 14f);
		setTextSize(streetTextView, 14f);
		setTextSize(cityTextView, 14f);
		setTextSize(stateTextView, 14f);
		setTextSize(zipTextView, 14f);
		setTextSize(countryTextView, 14f);
		setTextSize(streetEditText, 12.5f);
		setTextSize(cityEditText, 12.5f);
		setTextSize(stateEditText, 12.5f);
		setTextSize(zipEditText, 12.5f);
	}

	private void addTextChangeListeners() {
		streetEditText.addTextChangedListener(new CustomTextChangeListener(config));
		cityEditText.addTextChangedListener(new CustomTextChangeListener(config));
		stateEditText.addTextChangedListener(new CustomTextChangeListener(config));
		zipEditText.addTextChangedListener(new CustomTextChangeListener(config));
	}

	public void setTextSize(TextView view, float size){
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		view.setTypeface(mFont);
	}

	private void defineViewSettings(Activity context) {
		streetEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					noErrorMessage(streetTextView);
				else
					setValues();
			}
		});
		cityEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					noErrorMessage(cityTextView);
				else
					setValues();
			}
		});
		stateEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					noErrorMessage(stateTextView);
				else
					setValues();
			}
		});
		zipEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					noErrorMessage(zipTextView);
				else
					setValues();
			}
		});
		ArrayAdapter<SelectElement> adapter = getAdapter(context);
		countriesSpinner.setAdapter(adapter);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid() + "_1", ad);
		ViewLookup.mapField(this.config.getCid() + "_1_1", streetEditText);
		ViewLookup.mapField(this.config.getCid() + "_1_2", cityEditText);
		ViewLookup.mapField(this.config.getCid() + "_1_3", stateEditText);
		ViewLookup.mapField(this.config.getCid() + "_1_4", zipEditText);
		ViewLookup.mapField(this.config.getCid() + "_1_5", countriesSpinner);
	}
	private void setViewValues() {
		title.setText(this.config.getLabel() + (this.config.getRequired() ? "*" : ""));
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
            valid = checkEmpty(streetTextView, street, " Street Required");
            valid = checkEmpty(cityTextView, city, " City Required");
            valid = checkEmpty(stateTextView, state,  " State Required");
            valid = checkEmpty(zipTextView, zip, " Zip Required");
            valid = checkEmpty(countryTextView, country, " Country Required");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}

	private boolean checkEmpty(TextView view, String street, String errorMessage) {
		if(config.getRequired() && street.equals("")){
			errorMessage(view, errorMessage);
			return false;
		}  else{
			noErrorMessage(view);
			return true;
		}
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessage(TextView view ,String message) {
		if(view==null)return;
		view.setText((this.config.getRequired()?"*":"") );
		view.setText(view.getText() + message);
		view.setTextColor(R.color.ErrorMessage);
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage(TextView view ) {
		if(view==null)return;
		view.setText(this.config.getRequired()?"Street*":"" );
		view.setTextColor(R.color.TextViewNormal);
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
			fullAddress=street+" "+city+" "+state+" "+zip+" "+country;
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		ad=null;
		streetEditText=null;
		streetTextView=null;
		cityEditText=null;
		cityTextView=null;
		stateEditText=null;
		stateTextView=null;
		zipEditText=null;
		zipTextView=null;
		countriesSpinner=null;
		countryTextView=null;
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	@Override
	public void hideField() {
		if(ad!=null){
			ad.setVisibility(View.GONE);
			ad.invalidate();
		}
	}

	@Override
	public void showField() {
		if(ad!=null){
			ad.setVisibility(View.VISIBLE);
			ad.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(fullAddress.toLowerCase().contains(value.toLowerCase()) || fullAddress.trim().equals("")){
				return true;
			}
			return false;
		}
		return true;
	}

    public boolean isHidden(){
        if(ad!=null) {
            return !ad.isShown();
        } else {
            return false;
        }
    }
}