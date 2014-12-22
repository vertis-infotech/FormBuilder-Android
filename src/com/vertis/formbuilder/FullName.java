package com.vertis.formbuilder;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;

import android.app.Activity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FullName implements IField {

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
	@Expose
	String firstName="";
	@Expose
	String lastName="";

	//constructor to populate config
	public FullName(FieldConfig fcg){
		this.config=fcg;
	}

	//generates the views using context and assigns ids using viewLookup
	//Also calls setValues to initialize values(and calls noErrorMessage)
	//for EditText:
	//if focus lost, setValues is called
	//if focus gained, noErrorMessage is called
	public void createForm(Activity context) {
		subForm = new LinearLayout(context);
		ViewLookup.mapField(this.config.getCid()+"_1", subForm);
		subForm.setOrientation(LinearLayout.VERTICAL);
		headingText = new TextView(context);
		headingText.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		subForm.addView(headingText);
		nameField = new LinearLayout(context);
		prefixBox = new Spinner(context);
		ViewLookup.mapField(this.config.getCid()+"_1__1",prefixBox);
		String[] ss = { "Mr", "Mrs" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, ss);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prefixBox.setAdapter(adapter);
		if(prefix.equals("Mrs")){
			prefixBox.setSelection(1);
		}
		nameField.addView(prefixBox);
		firstNameTextBox = new EditText(context);
		ViewLookup.mapField(this.config.getCid()+"_1_2", firstNameTextBox);
		firstNameTextBox.setHint(firstNameHint);
		firstNameTextBox.setText(firstName);
		firstNameTextBox.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessage();
				else
					setValues();

			}
		});
		nameField.addView(firstNameTextBox);
		lastNameTextBox = new EditText(context);
		ViewLookup.mapField(this.config.getCid()+"_1_3", lastNameTextBox);
		lastNameTextBox.setOnFocusChangeListener( new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessage();
				else
					setValues();

			}
		});
		nameField.addView(lastNameTextBox);
		lastNameTextBox.setHint(lastNameHint);
		lastNameTextBox.setText(lastName);
		subForm.addView(nameField);

		setValues();
		noErrorMessage();
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
