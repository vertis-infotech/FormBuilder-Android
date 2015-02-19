package com.vertis.formbuilder;

import java.util.HashMap;

public class FieldRegistry {

	@SuppressWarnings("rawtypes")
	static HashMap<String,Class> fields=new HashMap<String,Class>();
	
	static{
		fields.put("fullnamexml", FullNameXml.class);
		fields.put("radio", Radio.class);
		fields.put("section_break", Section.class);
		fields.put("email",Email.class);
		fields.put("address",Address.class);
		fields.put("checkbox",Checkbox.class);
		fields.put("dropdown", Dropdown.class);
		fields.put("contact", Contact.class);
		fields.put("text", SimpleEditText.class);
		fields.put("mtext", MultiLineEditText.class);
		}
	
	@SuppressWarnings("rawtypes")
	public static Class getField(String typeName){		
		return fields.get(typeName);
	}
}
