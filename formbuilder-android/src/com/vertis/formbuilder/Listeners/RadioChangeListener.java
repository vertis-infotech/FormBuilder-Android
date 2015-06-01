package com.vertis.formbuilder.Listeners;

import com.vertis.formbuilder.parser.FieldConfig;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RadioChangeListener implements OnCheckedChangeListener{

	private FieldConfig config;

	public RadioChangeListener(FieldConfig config){
		this.config = config;
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		CheckConditions checkConditionsObject=new CheckConditions(config);
		checkConditionsObject.loopOverCheckCondition();
	}
}