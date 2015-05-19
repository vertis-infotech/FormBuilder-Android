package com.vertis.formbuilder.parser;

import com.vertis.formbuilder.Email;

public class Conditions {
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Boolean getIsSource() {
		return isSource;
	}
	public void setIsSource(Boolean isSource) {
		this.isSource = isSource;
	}
	String source;
	String condition;
	String value;
	String action;
	String target;
	Boolean isSource;
	
	public Conditions() {
		this.source="";
		this.condition="";
		this.value="";
		this.action="";
		this.target="";
		this.isSource=false;
	}
}