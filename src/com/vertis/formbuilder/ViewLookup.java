package com.vertis.formbuilder;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import android.view.View;

public class ViewLookup {
	
	static HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	
	public static void mapField(String stringId,View field){
		int intId=View.generateViewId();
		field.setId(intId);
		hashMap.put(stringId, intId);	
	}
	
	public static int getId(String stringId){
		return hashMap.get(stringId);
	}
	
}
