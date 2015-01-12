package com.vertis.formbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
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
	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", ad);
		ViewLookup.mapField(this.config.getCid()+"_1_1",streetEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_2",streetTextView);
		ViewLookup.mapField(this.config.getCid()+"_1_3",cityEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_4",cityTextView);
		ViewLookup.mapField(this.config.getCid()+"_1_5",stateEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_6",stateTextView);
		ViewLookup.mapField(this.config.getCid()+"_1_7",zipEditText);
		ViewLookup.mapField(this.config.getCid()+"_1_8",zipTextView);
		ViewLookup.mapField(this.config.getCid()+"_1_9",countryTextView);
		ViewLookup.mapField(this.config.getCid()+"_1_10",countriesSpinner);
	}
	private void setViewValues() {
		//		streetTextView.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		streetEditText.setText(street);
		//		cityTextView.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		cityEditText.setText(city);
		//		stateTextView.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		stateEditText.setText(state);
		//		zipTextView.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
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
		boolean valid;
		String street = streetEditText.getText().toString();
		String city = cityEditText.getText().toString();
		String state = stateEditText.getText().toString();
		String zip = zipEditText.getText().toString();
		String country = countriesSpinner.getSelectedItem().toString();

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
		
		return valid;
	}

	private void errorMessageStreet(String message) {
		if(streetTextView==null)return;
		streetTextView.setText((this.config.getRequired()?"*":"") );
		streetTextView.setText(streetTextView.getText() + message);
		streetTextView.setTextColor(-65536);
	}

	private void errorMessageCity(String message) {
		if(cityTextView==null)return;
		cityTextView.setText((this.config.getRequired()?"*":"") );
		cityTextView.setText(cityTextView.getText() + message);
		cityTextView.setTextColor(-65536);
	}

	private void errorMessageState(String message) {
		if(stateTextView==null)return;
		stateTextView.setText((this.config.getRequired()?"*":"") );
		stateTextView.setText(stateTextView.getText() + message);
		stateTextView.setTextColor(-65536);
	}

	private void errorMessageZip(String message) {
		if(countryTextView==null)return;
		zipTextView.setText((this.config.getRequired()?"*":"") );
		zipTextView.setText(zipTextView.getText() + message);
		zipTextView.setTextColor(-65536);
	}

	private void errorMessageCountry(String message) {
		if(countryTextView==null)return;
		countryTextView.setText((this.config.getRequired()?"*":"") );
		countryTextView.setText(countryTextView.getText() + message);
		countryTextView.setTextColor(-65536);
	}

	private void noErrorMessageStreet() {
		if(streetTextView==null)return;
		streetTextView.setText(this.config.getRequired()?"Street*":"" );
		streetTextView.setTextColor(-1);
	}
	
	private void noErrorMessageCity() {
		if(cityTextView==null)return;
		cityTextView.setText(this.config.getRequired()?"City*":"" );
		cityTextView.setTextColor(-1);
	}
	
	private void noErrorMessageState() {
		if(stateTextView==null)return;
		stateTextView.setText(this.config.getRequired()?"State*":"" );
		stateTextView.setTextColor(-1);
	}
	
	private void noErrorMessageZip() {
		if(zipTextView==null)return;
		zipTextView.setText(this.config.getRequired()?"Zip*":"" );
		zipTextView.setTextColor(-1);
	}
	
	private void noErrorMessageCountry() {
		if(countryTextView==null)return;
		countryTextView.setText(this.config.getRequired()?"Country*":"" );
		countryTextView.setTextColor(-1);
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