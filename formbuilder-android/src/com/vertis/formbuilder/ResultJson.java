package com.vertis.formbuilder;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class ResultJson {
	@Expose
	ArrayList<Object> values = new ArrayList<Object>();
	public void addValue(Object value){
		this.values.add(value);
	}
}
