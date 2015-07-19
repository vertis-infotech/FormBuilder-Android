package com.vertis.formbuilder.Listeners;
import com.vertis.formbuilder.parser.FieldConfig;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ListChangeListener implements OnItemClickListener {

	private FieldConfig config;

	public ListChangeListener(FieldConfig config){
		this.config = config;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CheckConditions checkConditionsObject=new CheckConditions(config);
		checkConditionsObject.loopOverCheckCondition();
	}

}
