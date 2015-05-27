package Listeners;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vertis.formbuilder.FormBuilder;
import com.vertis.formbuilder.IField;
import com.vertis.formbuilder.parser.FieldConfig;

import android.text.Editable;
import android.text.TextWatcher;

public class TextChangeListener implements TextWatcher  {

	private FieldConfig config;

	public TextChangeListener(FieldConfig config){
		this.config = config;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) { }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		CheckConditions checkConditionsObject=new CheckConditions(config);
		checkConditionsObject.loopOverCheckCondition();
	}

	@Override
	public void afterTextChanged(Editable s) { }   
}