package com.vertis.formbuilder.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class FieldConfig {
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getField_type() {
		return field_type;
	}
	public void setField_type(String field_type) {
		this.field_type = field_type;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public FieldOptions getField_options() {
		return field_options;
	}
	public void setField_options(FieldOptions field_options) {
		this.field_options = field_options;
	}
	
	public ArrayList<Conditions> getConditions() {
		return conditions;
	}
	public void setConditions(ArrayList<Conditions> condition) {
		this.conditions = condition;
	}
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public int getSection_id() {
		return section_id;
	}
	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

    public FieldConfig() { }

    public FieldConfig(String cid, int section_id, String field_type, FieldOptions field_options) {
        this.cid = cid;
        this.section_id = section_id;
        this.field_options = field_options;
        this.field_type = field_type;
    }

    String label;
	String field_type;
	Boolean required;
	ArrayList<AllOptions> options = new ArrayList<AllOptions>();
	FieldOptions field_options = new FieldOptions();
	ArrayList<Conditions> conditions= new ArrayList<Conditions>();
	//ArrayList<HashMap<String,String>> conditions = new ArrayList<HashMap<String,String>>();
	String cid;
	int section_id=0;
}
