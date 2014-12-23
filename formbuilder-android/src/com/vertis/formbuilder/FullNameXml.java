package com.vertis.formbuilder;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FullNameXml implements IField {

	//config contains all data from json ie id,sid,label,field_options
	private FieldConfig config;	

	// Default Values
	String firstNameHint = "first name";
	String lastNameHint = "last name";
	@SuppressWarnings("unused")
	private boolean includeOtherOption;


	// Views
	LinearLayout subForm;
	TextView headingText;
	LinearLayout nameField;
	Spinner prefixBox;
	EditText firstNameTextBox;
	EditText lastNameTextBox;

	//Values
	@Expose
	String cid;
	@Expose
	String prefix="";
	int prefixPosition=0;
	@Expose
	String firstName="";
	@Expose
	String lastName="";

	//constructor to populate config
	public FullNameXml(FieldConfig fcg){
		this.config=fcg;
	}

	//generates the views using context and assigns ids using viewLookup
	//Also calls setValues to initialize values(and calls noErrorMessage)
	//for EditText:
	//if focus lost, setValues is called
	//if focus gained, noErrorMessage is called
	public void createForm(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		subForm=(LinearLayout) inflater.inflate(R.layout.fullname,null);
		headingText = (TextView) subForm.findViewById(R.id.textView1);
		prefixBox = (Spinner) subForm.findViewById(R.id.spinner1);
		firstNameTextBox = (EditText) subForm.findViewById(R.id.editText1);
		lastNameTextBox = (EditText) subForm.findViewById(R.id.editText2);

		defineViewSettings(context);
		setViewValues();
		mapView();
		
		setValues();
		noErrorMessage();
	}

	void defineViewSettings(Activity context){
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.prefixArray, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prefixBox.setAdapter(adapter);
		firstNameTextBox.setHint(firstNameHint);
		firstNameTextBox.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessage();
				else
					setValues();
			}
		});
		lastNameTextBox.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessage();
				else
					setValues();
			}
		});
		lastNameTextBox.setHint(lastNameHint);
	}

	void setViewValues(){
		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		prefixBox.setSelection(prefixPosition);
		firstNameTextBox.setText(firstName);
		lastNameTextBox.setText(lastName);

	}

	void mapView(){
		ViewLookup.mapField(this.config.getCid()+"_1", subForm);
		ViewLookup.mapField(this.config.getCid()+"_1_1",prefixBox);
		ViewLookup.mapField(this.config.getCid()+"_1_2", firstNameTextBox);
		ViewLookup.mapField(this.config.getCid()+"_1_3", lastNameTextBox);

	}

	//return views
	public ViewGroup getView() {
		return subForm;
	}

	@Override
	public void clearViews() {
		setValues();

		subForm=null;
		headingText=null;
		nameField=null;
		prefixBox=null;
		firstNameTextBox=null;
		lastNameTextBox=null;

	}

	public void setValues() {
		this.cid=config.getCid();

		if(subForm!=null){
			firstName=firstNameTextBox.getText().toString();
			lastName=lastNameTextBox.getText().toString();
			prefixPosition=prefixBox.getSelectedItemPosition();
			prefix=prefixBox.getSelectedItem().toString();
		}
		validate();
	}

	public boolean validate() {
		boolean valid;

		if(config.getRequired()&&firstName.equals("")){
			valid=false;
			errorMessage("Required");
		}
		else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	public void errorMessage(String message){
		if(headingText==null)return;

		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		headingText.setText(headingText.getText() + " " + message);
		headingText.setTextColor(-65536);
	}

	public void noErrorMessage(){
		if(headingText==null)return;

		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		headingText.setTextColor(-1);
	}
};
