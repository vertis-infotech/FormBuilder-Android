package com.vertis.formbuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vertis.formbuilder.parser.FieldConfig;
import com.vertis.formbuilder.parser.FieldOptions;
import com.vertis.formbuilder.parser.FormJson;

import com.vertis.formbuilder.Listeners.CheckConditions;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class FormBuilder {
    Activity context;
    public static int currentSection;
    /**
     * Private Variables:
     * Tree map containing all the views
     * (section id mapped to all views in that section )
     * currentSection stores section id of the section currently displayed
     */
    public static TreeMap<Integer,ArrayList<IField>> fields = new TreeMap<Integer, ArrayList<IField>>();
    /**
     *  Form is the linearLayout containing various section breaks.
     *  These section breaks will contain various fields.
     *  the fields context will be taken from the constructor
     */
    public static TreeMap<Integer,Section> sectionsTreeMap= new TreeMap<Integer,Section>();
    LinearLayout previousNextContainer;
    Button previousButton;
    Button nextButton;
    public FormBuilder() {
        fields =  new TreeMap<Integer, ArrayList<IField>>();
    }

    public void setup(String json , LinearLayout form, Activity context) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        this.context=context;
        Gson gson = new Gson();
        //fj contains all the field models
        FormJson fj = gson.fromJson(json, FormJson.class);
        //iterate through FieldConfigs and generate instances of IFields to populate fields array
        ArrayList<FieldConfig>fieldConfigs = fj.getFields();
        addSection();
        loopOverFieldConfig(fieldConfigs);
        checkMoreThanOneSections(form);
        //render the first section
        currentSection = 0;
        sectionsTreeMap.get(currentSection).getView().setVisibility(View.VISIBLE);
        loopOverFieldConfigToCheckConditions(fieldConfigs);
    }

    private void addSection() {
        Section section =  new Section(new FieldConfig("c00", 0, "section_break",new FieldOptions()));
        section.createForm(context);
        sectionsTreeMap.put(0,section);
    }

    private void loopOverFieldConfigToCheckConditions(ArrayList<FieldConfig> fieldConfigs) {
        for(FieldConfig fcg : fieldConfigs){
            CheckConditions checkConditionsObject=new CheckConditions(fcg);
            checkConditionsObject.loopOverCheckCondition();
        }
    }

    private void checkMoreThanOneSections(LinearLayout form) {
        if(fields.size()!=1){
            /**
             * Section break exists:
             * Split main linear layout(form) in two parts:
             * 1) contains linear layout which hosts the fields(this.form)
             * 2) initialize previous and next buttons and put them in a container
             */
            buttonSpecifications();
            loopOverSectionTreeMap(form);
            form.addView(this.previousNextContainer);
        } else{
            //No section break=>no previous/next buttons
            View v = this.sectionsTreeMap.get(0).getView();
            //render generates the required views(from json) and adds the required views to linearlayout passed to the functions
            this.render(0);
            form.addView(v);
        }
    }

    private void loopOverSectionTreeMap(LinearLayout form) {
        for (int i = 0; i < sectionsTreeMap.size() ; i++) {
            View v = this.sectionsTreeMap.get(i).getView();
            v.setVisibility(View.GONE);
            this.render(i);
            form.addView(v);
        }
    }

    private void loopOverFieldConfig(ArrayList<FieldConfig> fieldConfigs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        int j=1;
        for(FieldConfig fcg : fieldConfigs){
            Class<?> ViewImpl = FieldRegistry.getField(fcg.getField_type());
            Constructor<?> ctor =ViewImpl.getConstructor(FieldConfig.class);
            IField ifield = (IField) ctor.newInstance(fcg);
            //putting each field in appropriate ArrayList(mapped to section id)
            if(fields.get(fcg.getSection_id()) == null ){
                fields.put(fcg.getSection_id(), new ArrayList<IField>());
            }
            if (fcg.getField_type().equals("section_break")){
                ifield.createForm(context);
                sectionsTreeMap.put(j,(Section)ifield);
                j++;
            }else {
                fields.get(fcg.getSection_id()).add(ifield);
            }
        }
    }

    private void buttonSpecifications() {
        // previous and next button will be used if there are section breaks
        this.previousNextContainer= new LinearLayout(context);
        this.previousNextContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams abc =  new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        abc.setMargins(0, 10, 0, 10);
        this.previousNextContainer.setGravity(Gravity.RIGHT);
        this.previousNextContainer.setLayoutParams(abc);
        this.nextButton=new Button(context);
        this.nextButton.setText("Next");
        this.nextButton.setBackgroundResource(R.drawable.prev_next_buttons);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                next();
            }
        });
        this.previousButton=new Button(context);
        this.previousButton.setText("Previous");
        this.previousButton.setBackgroundResource(R.drawable.prev_next_buttons);
        this.previousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                previous();
            }
        });
        this.previousNextContainer.addView(previousButton);
        this.previousNextContainer.addView(nextButton);
        this.previousButton.setEnabled(false);
    }

    /**
     * createForm initializes the Views
     * getView returns the view to be displayed
     * form is the linearLayout which hosts the field
     */
    public void render(int sectionId){
        for(IField field : fields.get(sectionId)){
            field.createForm(context);
            if(field.getView()!=null)
                sectionsTreeMap.get(sectionId).getView().addView(field.getView());
        }
    }

    //delete all views currently displayed after saving their data(done by clearViews)
    public void clear(int sectionId){
        for(IField field : fields.get(sectionId)){
            field.clearViews();
        }
        //form_section.removeAllViews();
    }

    //Submit works only if you are in the last section and all fields are valid
    //returns output json which is {values:[array of json objects from field.toJson ]}
    public String submit(){
        if(fields.higherEntry(currentSection)!=null)
            return "Not ready to submit!!!";

        for(IField field: fields.get(currentSection)){
            field.setValues();
            if(!field.validate())
                return "Not ready to submit!!!";
        }
        ResultJson rj = new ResultJson();
        for (ArrayList<IField> fieldList: this.fields.values()) {
            for(IField field:fieldList){
                field.setValues();
                rj.addValue(field);
            }
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(rj);
    }

    /**
     * called when next is pressed
     * activates previous button
     * deactivates next if this is the last section
     * goes to next page only if all fields are valid (after setting values)
     * calls clear on current sectionId
     * calls render on next sectionId
     */
    public void next(){
        for(IField field : fields.get(currentSection)){
            field.setValues();
            if(!field.validate())
                return;
        }
        previousButton.setEnabled(true);
        sectionsTreeMap.get(currentSection).getView().setVisibility(View.GONE);
        currentSection=fields.higherKey(currentSection);
        if(sectionsTreeMap.get(currentSection).isHidden()){
            sectionsTreeMap.get(currentSection).getView().setVisibility(View.GONE);
            if(fields.higherKey(currentSection)!= null) {
                next();
            }
        }else{
            sectionsTreeMap.get(currentSection).getView().setVisibility(View.VISIBLE);
        }
        if(fields.higherKey(currentSection)==null){
            nextButton.setEnabled(false);
        }
    }

    /**
     * called when previous is pressed
     * activates next button
     * deactivates previous if this is the first section
     * calls clear on current sectionId
     * calls render on previous sectionId
     */
    public void previous(){
        nextButton.setEnabled(true);
        sectionsTreeMap.get(currentSection).getView().setVisibility(View.GONE);
        currentSection=fields.lowerKey(currentSection);
        if(sectionsTreeMap.get(currentSection).isHidden()){
            sectionsTreeMap.get(currentSection).getView().setVisibility(View.GONE);
        }else{
            sectionsTreeMap.get(currentSection).getView().setVisibility(View.VISIBLE);
        }
        if(fields.lowerKey(currentSection)==null){
            previousButton.setEnabled(false);
        }
    }
}
