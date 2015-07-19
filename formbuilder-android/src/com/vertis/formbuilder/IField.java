package com.vertis.formbuilder;

import android.app.Activity;
import android.view.ViewGroup;

public interface IField {
	
	//Steps to create new field:
	
	/**
	 * Data members:
	 * 1)FieldConfig config will be populated by constructor, contains data needed for form creation.
	 *  it will have cid label field_type required and field_options
	 * 
	 * 2)Values to be converted to json when submit button is clicked should have annotation \@Expose
	 * 
	 * 3)Views including a container view which will be returned by getView()
	 */
	
	
	/**
	 * Write a constructor which initializes FieldConfig
	 */
	
	
	
	/**
	 * CreateForm()
	 *Use ViewLookup.mapField(string,view) to provide the view with an id
	 *use ViewLookup.getid(string) to get id(view) associated with the string in the future.
	 *Convention: String id associated with layout is same as id in default values. Id of other views are id_of_parent + "_i", where i is a number
	 * 
	 */
	public abstract void createForm(Activity context);

	/**
	 * getView
	 * return layout which encloses all the views
	 */
	public abstract ViewGroup getView();

	
	/**
	 * validate
	 Check for validations. Return true if entries are valid.
	 *You can also output a message like "This field required" in this function.
	 */
	public abstract boolean validate();

	
	/**
	 * setValues
	 * Use data from Views to populate Values to return. 
	 */
	public abstract void setValues();
	
	public abstract void clearViews();
	
	public abstract String getCIDValue();
	
	public abstract void hideField();
	
	public abstract void showField();
	
	public abstract boolean validateDisplay(String value,String condition);

    public boolean isHidden();
	
}