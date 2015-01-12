package com.vertis.test;

import com.vertis.formbuilder.FormBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends Activity {
	private String result;
	FormBuilder formBuilder = new FormBuilder(); 
	private LinearLayout form; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		form=(LinearLayout)findViewById(R.id.Lay1);
		String jsonstr = "{\"fields\":[" +
				"{\"label\":\"Full Name\", \"field_type\":\"fullname\", \"required\":true, \"field_options\":{\"include_other_option\":true}, \"conditions\":[], \"cid\":\"c26\"}" +
				",{\"label\":\"Pura Naam\", \"field_type\":\"fullname\", \"required\":false, \"field_options\":{\"include_other_option\":true}, \"conditions\":[], \"cid\":\"c27\"}" +
				",{\"label\":\"Naam batao\", \"field_type\":\"fullname\", \"required\":false, \"field_options\":{\"include_other_option\":true}, \"conditions\":[], \"cid\":\"c21\"}" +
				",{\"label\":\"Untitled\", \"field_type\":\"section_break\", \"required\":false, \"field_options\":{}, \"conditions\":[], \"cid\":\"c28\",\"section_id\":100}" +
				",{\"label\":\"Naam batao na!\", \"field_type\":\"fullnamexml\", \"required\":false, \"field_options\":{\"include_other_option\":true}, \"conditions\":[], \"cid\":\"c29\",\"section_id\":100}" +
				",{\"label\":\"Arre! Ajeeb pagal hai.\", \"field_type\":\"fullnamexml\", \"required\":true, \"field_options\":{\"include_other_option\":true}, \"conditions\":[], \"cid\":\"c30\",\"section_id\":100}" +
				",{\"label\":\"Email\", \"field_type\":\"email\", \"required\":true, \"field_options\":{\"include_other_option\":true}, \"conditions\":[], \"cid\":\"c30\"}" +
				",{\"label\":\"My Address\", \"field_type\":\"address\", \"required\":true, \"field_options\":{\"include_other_option\":true}, \"conditions\":[], \"cid\":\"c40\"}" +	
				"]}";
/**
 * Set up formbuiler by giving json, linearlayout where form is to be displayed
 * and context
 */
		try {
			formBuilder.setup(jsonstr, form, this);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * link submit button to submit function of the formbuilder
 * @param button
 */
	public void submit(View button) {

			this.result= formBuilder.submit();
			Toast.makeText(this, this.result, Toast.LENGTH_LONG).show();			
	}

	public void save(View button) {
		this.submit(button);
	}

	public void cancel(View button) {
	}
}