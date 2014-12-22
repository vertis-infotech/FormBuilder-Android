package com.vertis.formbuilder;

import java.util.HashMap;

public class FieldRegistry {

	@SuppressWarnings("rawtypes")
	static HashMap<String,Class> fields=new HashMap<String,Class>();
	
	static{
		fields.put("fullname", FullName.class);
		fields.put("radio", Radio.class);
		fields.put("section_break", Section.class);
		}
	
	@SuppressWarnings("rawtypes")
	public static Class getField(String typeName){		
		return fields.get(typeName);
	}
}
