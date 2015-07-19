package com.vertis.formbuilder;

import com.vertis.formbuilder.parser.FieldConfig;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class Section implements IField {
    Activity context;
    FieldConfig config;
    LinearLayout emptyLayout;
    boolean hide_section;

    public Section(FieldConfig fcg){
        config=fcg;
    }

    @Override
    public void createForm(Activity context) {
        this.context=context;
        emptyLayout = new LinearLayout(context);
        emptyLayout.setOrientation(LinearLayout.VERTICAL);
        emptyLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public ViewGroup getView() {
        return emptyLayout;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void setValues() {
    }

    @Override
    public void clearViews() {

    }

    public String getCIDValue() {
        return this.config.getCid();
    }

    @Override
    public void hideField() {
        this.hide_section = true;
    }

    @Override
    public void showField() {
        this.hide_section = false;
    }

    public boolean isHidden(){
        return this.hide_section;
    }

    public boolean validateDisplay(String value,String condition) {
        return false;
    }
}
