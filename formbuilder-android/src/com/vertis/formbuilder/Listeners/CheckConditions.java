package com.vertis.formbuilder.Listeners;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vertis.formbuilder.FormBuilder;
import com.vertis.formbuilder.IField;
import com.vertis.formbuilder.parser.FieldConfig;

public class CheckConditions {

    private FieldConfig config;

    public CheckConditions(FieldConfig config){
        this.config = config;
    }

    public void loopOverCheckCondition(){
        int size= config.getConditions().size();
        for (int i = 0; i < size; i++) {
            checkCondition(config.getConditions().get(i).getSource(),config.getConditions().get(i).getCondition(),config.getConditions().get(i).getValue(),
                    config.getConditions().get(i).getAction(),config.getConditions().get(i).getTarget(),config.getConditions().get(i).getIsSource());
        }
    }

    public void checkCondition(String source, String condition, String value, String action, String target, Boolean isSource) {
        String actualValue="";
        IField sourceField = getFieldFromCID(source);
        IField targetField = getFieldFromCID(target);

        sourceField.setValues();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        actualValue = gson.toJson(sourceField);
        switch(condition){
            case "equals":
                if(action.equals("hide")){
                    if(sourceField.validateDisplay(value,"equals")){
                        targetField.hideField();
                    }else{
                        targetField.showField();
                    }
                }else if(action.equals("show")){
                    if(sourceField.validateDisplay(value,"equals")){
                        targetField.showField();
                    }else{
                        targetField.hideField();
                    }
                }
                break;

            case "is greater than":
                if(action.equals("hide")){
                    if(sourceField.validateDisplay(value,"moreThan")){
                        targetField.hideField();
                    }else{
                        targetField.showField();
                    }
                }else if(action.equals("show")){
                    if(sourceField.validateDisplay(value,"moreThan")){
                        targetField.showField();
                    }else{
                        targetField.hideField();
                    }
                }
                break;

            case "is less than":
                if(action.equals("hide")){
                    if(sourceField.validateDisplay(value,"lessThan")){
                        targetField.hideField();
                    }else{
                        targetField.showField();
                    }
                }else if(action.equals("show")){
                    if(sourceField.validateDisplay(value,"lessThan")){
                        targetField.showField();
                    }else{
                        targetField.hideField();
                    }
                }
                break;
        }
    }

    private IField getFieldFromCID(String cid) {
        IField returnField=null;
        for (ArrayList<IField> fieldList: FormBuilder.fields.values()) {
            for(IField field:fieldList){
                if (field.getCIDValue().equals(cid)) {
                    returnField=field;
                    break;
                }
            }
        }
        if(returnField==null){
            for (IField field : FormBuilder.sectionsTreeMap.values()) {
                if (field.getCIDValue().equals(cid)) {
                    returnField=field;
                    break;
                }
            }
        }
        return returnField;
    }
}
