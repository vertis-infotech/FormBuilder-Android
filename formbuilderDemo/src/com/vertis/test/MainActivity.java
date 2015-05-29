package com.vertis.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

import com.vertis.formbuilder.FormBuilder;

public class MainActivity extends Activity {
	private String result;
	FormBuilder formBuilder = new FormBuilder();
	private LinearLayout form;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		form=(LinearLayout)findViewById(R.id.Lay1);
		//String jsonstr =  "{\"fields\":[" +
		//				"{\"label\":\"Full Name\", \"field_type\":\"fullnamexml\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c26\"}" +
		//				",{\"label\":\"Untitled\", \"field_type\":\"section_break\", \"required\":false, \"field_options\":{}, \"conditions\":[], \"cid\":\"c28\",\"section_id\":100}" +
		//				",{\"label\":\"Email\", \"field_type\":\"email\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c30\"}" +
		//				",{\"label\":\"My Address\", \"field_type\":\"address\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c40\"}" +
		//				",{\"label\":\"Radio\", \"field_type\":\"radio\", \"required\":true, \"field_options\":{\"options\":[{\"label\":\"y\",\"checked\":false},{\"label\":\"n\",\"checked\":false}]},\"conditions\":[],\"cid\":\"c47\"}" +
		//				",{\"label\":\"Checkbox\", \"field_type\":\"checkbox\", \"required\":true, \"field_options\":{\"options\":[{\"label\":\"1\",\"checked\":false},{\"label\":\"2\",\"checked\":false}]},\"conditions\":[],\"cid\":\"c49\"}" +
		//				",{\"label\":\"Dropdown\", \"field_type\":\"dropdown\", \"required\":true, \"field_options\":{\"options\":[{\"label\":\"A\",\"checked\":false}, {\"label\":\"B\", \"checked\":false}], \"include_blank_option\":true,\"size\":\"small\",\"empty_option_text\":\"\"}, \"conditions\":[{\"source\":\"c1\", \"condition\":\"equals\", \"value\":\"A\", \"action\":\"show\", \"target\":\"c2\", \"isSource\":false}], \"cid\":\"c1\"}" +
		//				",{\"label\":\"Contact No.\", \"field_type\":\"contact\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c31\"}" +
		//				",{\"label\":\"Text\", \"field_type\":\"text\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c32\"}" +
		//				",{\"label\":\"MultiLine Text\", \"field_type\":\"mtext\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c33\"}" +
		//				",{\"label\":\"Price\", \"field_type\":\"price\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c27\"}" +
		//				",{\"label\":\"Date Time Picker\",\"field_type\":\"date_time\",\"required\":false,\"field_options\":{\"minage\":\"21\",\"date_format\":\"yyyy-MM-dd HH:mm:ss.SSS\"},\"conditions\":[],\"cid\":\"c2\"}" +
		//				",{\"label\":\"Take a pic\",\"field_type\":\"take_pic_video_audio\",\"required\":true,\"field_options\":{\"include_photo_option\":true,\"include_video_option\":true,\"include_audio_option\":true},\"conditions\":[],\"cid\":\"c99\"}" +
		//				",{\"label\":\"Number\", \"field_type\":\"number\", \"required\":true, \"field_options\":{}, \"conditions\":[], \"cid\":\"c14\"}" +
		//				"]}";

		String jsonstr =	"{\"fields\":[{\"label\":\"Contact\",\"field_type\":\"contact\",\"required\":false,\"field_options\":{\"size\":\"small\"},\"field_options\":{\"options\":[{\"label\":\"y\",\"checked\":false},{\"label\":\"n\",\"checked\":false}]},\"conditions\":[{\"source\":\"c6\",\"condition\":\"equals\",\"value\":\"100\",\"action\":\"hide\",\"target\":\"c4\",\"isSource\":false},{\"source\":\"c6\",\"condition\":\"equals\",\"value\":\"100\",\"action\":\"hide\",\"target\":\"c28\",\"isSource\":false}],\"cid\":\"c6\"},"
				+ "{\"label\":\"Birthdate\",\"field_type\":\"birth_date\",\"required\":false,\"field_options\":{},\"conditions\":[{\"source\":\"c6\",\"condition\":\"equals\",\"value\":\"100\",\"action\":\"hide\",\"target\":\"c4\",\"isSource\":true}],\"cid\":\"c4\"}"
                + ",{\"label\":\"Untitled\", \"field_type\":\"section_break\", \"required\":false, \"field_options\":{}, \"conditions\":[{\"source\":\"c6\",\"condition\":\"equals\",\"value\":\"100\",\"action\":\"hide\",\"target\":\"c28\",\"isSource\":false}], \"cid\":\"c28\",\"section_id\":1}"
				+ ",{\"label\":\"Address\",\"field_type\":\"address\",\"required\":true,\"field_options\":{},\"conditions\":[],\"section_id\":1,\"cid\":\"c8\"}],\"latest_cid\":8}";

		/**
		 * Set up formbuiler by giving json, linearlayout where form is to be displayed
		 * and context
		 */
		try {
			formBuilder.setup(jsonstr, form, this);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
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