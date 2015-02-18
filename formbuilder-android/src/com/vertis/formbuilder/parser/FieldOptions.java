package com.vertis.formbuilder.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class FieldOptions {
	
	ArrayList<AllOptions> options = new ArrayList<AllOptions>();
	
	public ArrayList<AllOptions> getOptions() {
		return options;
	}
	
	public void setOptions(ArrayList<FieldConfig> fields) {
		this.options = options;
	}

}
