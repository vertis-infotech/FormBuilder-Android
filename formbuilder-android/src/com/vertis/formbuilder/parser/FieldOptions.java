package com.vertis.formbuilder.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class FieldOptions {
	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public String getDate_format() {
		return date_format;
	}

	public void setDate_format(String date_format) {
		this.date_format = date_format;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	public String getExistingFieldValue() {
		return existingFieldValue;
	}

	public void setExistingFieldValue(String existingFieldValue) {
		this.existingFieldValue = existingFieldValue;
	}

	public FieldOptions() {
		this.steps = 1;
		this.date_format = "dd/MM/yyyy";
		this.maxDate = "";
		this.existingFieldValue = "";
	}
	ArrayList<AllOptions> options = new ArrayList<AllOptions>();
	int steps;
	String date_format, maxDate, existingFieldValue;
	
	public ArrayList<AllOptions> getOptions() {
		return options;
	}
	
	public void setOptions(ArrayList<FieldConfig> fields) {
		this.options = options;
	}

}
