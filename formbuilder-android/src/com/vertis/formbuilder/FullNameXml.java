package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
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
		headingText.setTypeface(getFontFromRes(R.raw.roboto, context));
		firstNameTextBox.setTypeface(getFontFromRes(R.raw.roboto, context));
		lastNameTextBox.setTypeface(getFontFromRes(R.raw.roboto, context));
		firstNameTextBox.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
		lastNameTextBox.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float) 12.5);
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
	private ArrayAdapter<SelectElement> getAdapter(Context context) {
		return new CountriesArrayAdapter(context, getPrefixList(context));
	}

	private ArrayList<SelectElement> getPrefixList(Context context) {
		int i=0;
		String[] prefixes = context.getResources().getStringArray(R.array.prefixArray);
		ArrayList<SelectElement> prefixArr=new ArrayList<SelectElement>();
		for (String string : prefixes) 
			prefixArr.add(new SelectElement(i, string, string));
		return prefixArr;
	}
 
	void defineViewSettings(Activity context){
		prefixBox.setAdapter(getAdapter(context));
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
		headingText.setTextColor(Color.BLACK);
	}
};