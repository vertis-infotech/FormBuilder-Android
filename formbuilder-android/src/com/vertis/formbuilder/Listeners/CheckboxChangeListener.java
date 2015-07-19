package com.vertis.formbuilder.Listeners;

import com.vertis.formbuilder.parser.FieldConfig;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckboxChangeListener implements OnCheckedChangeListener {

	private FieldConfig config;

	public CheckboxChangeListener(FieldConfig config){
		this.config = config;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		CheckConditions checkConditionsObject=new CheckConditions(config);
		checkConditionsObject.loopOverCheckCondition();
	}
}
