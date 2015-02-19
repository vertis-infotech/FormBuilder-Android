package com.vertis.formbuilder;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class Dropdown implements IField{

	private FieldConfig config;
	
	LinearLayout llDropdown;
	TextView tvDropdown;
	Spinner sDropdown;
	
	@Expose
	String cid;
	@Expose
	String drop="";
	int ddPosition=0;
	
	public Dropdown(FieldConfig fcg){
		this.config=fcg;
	}
	
	@Override
	public void createForm(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llDropdown=(LinearLayout) inflater.inflate(R.layout.drop_down_xml,null);
		tvDropdown = (TextView) llDropdown.findViewById(R.id.textViewDropdown);
		sDropdown =(Spinner) llDropdown.findViewById(R.id.spinnerDropdown);		
		tvDropdown.setText(this.config.getLabel() + (this.config.getRequired()?"*":""));
		tvDropdown.setTextColor(Color.BLACK);
		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	private void noErrorMessage() {
		if(tvDropdown==null)return;
		tvDropdown.setText(this.config.getRequired()?"Dropdown":"" );
		tvDropdown.setTextColor(android.R.color.black);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", llDropdown);
		ViewLookup.mapField(this.config.getCid()+"_1_1",sDropdown);
	}

	private void setViewValues() {
		sDropdown.setSelection(ddPosition);
	}

	private void defineViewSettings(Activity context) {
		ArrayAdapter<SelectElement> adapter = getAdapter(context);
		sDropdown.setAdapter(adapter);
	}

	private ArrayAdapter<SelectElement> getAdapter(Activity context) {
		return new CountriesArrayAdapter(context, getContentList(context));
	}

	private ArrayList<SelectElement> getContentList(Activity context) {
		int i=0;
		ArrayList<SelectElement> contents=new ArrayList<SelectElement>();
		ArrayList<String> new_contents=new ArrayList<String>();
		for (int j = 0; j < this.config.getField_options().getOptions().size(); j++) {
			new_contents.add(this.config.getField_options().getOptions().get(j).getLabel());
		}
		for (String string : new_contents) {
			contents.add(new SelectElement(i, string, string));
		}
		return contents;
	}

	@Override
	public ViewGroup getView() {
		return llDropdown;
	}

	@Override
	public boolean validate() {
		boolean valid=false;
		if(config.getRequired()&&drop.equals("")) {
			valid=false;
			errorMessage("  Pick one!");
		} else{
			valid=true;
			noErrorMessage();
		}
		return valid;
	}

	private void errorMessage(String message) {
		if(tvDropdown==null)return;
		tvDropdown.setText((this.config.getRequired()?"*":"") );
		tvDropdown.setText(tvDropdown.getText() + message);
		tvDropdown.setTextColor(-65536);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(llDropdown!=null){			
			ddPosition=sDropdown.getSelectedItemPosition();
			drop=sDropdown.getSelectedItem().toString();
		}
		validate();
	}

	@Override
	public void clearViews() {
		llDropdown=null;
		tvDropdown=null;
		sDropdown=null;
	}
}