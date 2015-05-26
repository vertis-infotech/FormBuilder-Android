package com.vertis.formbuilder;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vertis.formbuilder.parser.FieldConfig;

import android.text.Editable;
import android.text.TextWatcher;

public class CustomTextChangeListener implements TextWatcher  {
	private FieldConfig config;

	CustomTextChangeListener(FieldConfig config){
		this.config = config;
	}
	/* Other methods protected by singleton-ness */
	protected  void demoMethod( ) {
		System.out.println("demoMethod for singleton"); 
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int size= config.getConditions().size();
		for (int i = 0; i < size; i++) {
			checkCondition(config.getConditions().get(i).getSource(), config.getConditions().get(i).getCondition(),
					config.getConditions().get(i).getValue(), config.getConditions().get(i).getAction(),
					config.getConditions().get(i).getTarget(), config.getConditions().get(i).getIsSource());
		}
	}

	@Override
	public void afterTextChanged(Editable s) { }   

	private void checkCondition(String source, String condition, String value, String action, String target, Boolean isSource) {
		String actualValue="";
		IField sourceField = getFieldFromCID(source);
		IField targetField = getFieldFromCID(target);
		sourceField.setValues();
		if(action.equals("hide")){
			if(sourceField.validateDisplay(value,condition)){
				targetField.hideField();
			}else {
				targetField.showField();
			}
		}else if(action.equals("show")){
			if(sourceField.validateDisplay(value,condition)){
				targetField.showField();
			}else{
				targetField.hideField();
			}
		}
	}
	
	private IField getFieldFromCID(String source) {
		IField returnField=null;
		for (ArrayList<IField> fieldList: FormBuilder.fields.values()) {
			for(IField field:fieldList){
				if (field.getCIDValue().equals(source)) {
					returnField=field;
					break;
				}
			}
		}
		return returnField;
	}
}