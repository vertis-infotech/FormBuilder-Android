package com.vertis.formbuilder;

import java.util.HashMap;

import android.util.Config;

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
		fields.put("price",Price.class);
		fields.put("date_time", DisplayDateTime.class);
		fields.put("birth_date", DisplayDateTime.class);
		fields.put("date", DisplayDateTime.class);
		fields.put("time", DisplayDateTime.class);
		fields.put("endDateTimeDifference", DisplayDateTime.class);
		fields.put("startDateTimeDifference", DisplayDateTime.class);
		fields.put("take_pic_video_audio", Capture.class);
		fields.put("number", NumberField.class);
		}

	@SuppressWarnings("rawtypes")
	public static Class getField(String typeName){		
		return fields.get(typeName);
	}
}
