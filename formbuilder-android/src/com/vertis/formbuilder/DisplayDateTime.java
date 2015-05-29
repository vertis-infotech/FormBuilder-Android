package com.vertis.formbuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;
import com.vertis.formbuilder.util.FormBuilderUtil;

@SuppressLint("SimpleDateFormat")
public class DisplayDateTime implements IField {

	//Views
	private FieldConfig config;
	LinearLayout llDisplayDateTime;
	TextView showTextDateTime;
	TextView tvDateTime;

	//Values
	@Expose
	String cid;
	@Expose
	String datetime="";
	String maxtime="";
	@Expose
	String dateTimeToRetainValue="";
	private Activity context;
	private Typeface font;

	public DisplayDateTime(FieldConfig fcg){
		this.config=fcg;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void createForm(Activity context) {
		this.context = context;
		font = new FormBuilderUtil().getFontFromRes(context);
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llDisplayDateTime=(LinearLayout) inflater.inflate(R.layout.display_date_time, null);
		showTextDateTime = (TextView) llDisplayDateTime.findViewById(R.id.showTextDateTime);
		tvDateTime=(TextView) llDisplayDateTime.findViewById(R.id.tvErrorMessage);
		datetime = config.getField_options().getExistingFieldValue();
		tvDateTime.setTextColor(R.color.TextViewNormal);
		showTextDateTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 12.5);
		tvDateTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tvDateTime.setTypeface(font);
		showTextDateTime.setTypeface(font);
		showTextDateTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTextDateTime.setFocusableInTouchMode(true);
				showTextDateTime.setFocusable(true);
				showTextDateTime.requestFocus();
				if(config.getField_type().equals("birth_date")){
					maxtime = new SimpleDateFormat(config.getField_options().getDate_format()).format(Calendar.getInstance().getTime());
				}else {
					maxtime = config.getField_options().getMaxDate();
				}
				openDialog(config.getField_options().getSteps(),
						config.getField_options().getDate_format(),
						config.getField_type(),
						maxtime,
						datetime);
			}
		});

		defineViewSettings(context);
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", llDisplayDateTime);
		ViewLookup.mapField(this.config.getCid()+"_1_1", showTextDateTime);
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(tvDateTime==null)return;
		tvDateTime.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvDateTime.setTextColor(R.color.TextViewNormal);
	}

	private void setViewValues() {
		tvDateTime.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		showTextDateTime.setText(dateTimeToRetainValue);
		tvDateTime.setTextColor(-1);
	}

	private void defineViewSettings(Activity context) {
		showTextDateTime.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					noErrorMessage();
				else
					setValues();
			}
		});
	}

	@Override
	public ViewGroup getView() {
		return llDisplayDateTime;
	}

	@Override
	public boolean validate() {
		boolean valid;
		if(config.getRequired() && showTextDateTime.getText().toString().equals("")){
			valid=false;
			errorMessage(" Required");
		}else{
			valid=true;
		}
		return valid;
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessage(String message) {
		if(tvDateTime==null)return;
		tvDateTime.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvDateTime.setText(tvDateTime.getText() + message);
		tvDateTime.setTextColor(R.color.ErrorMessage);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		if(llDisplayDateTime!=null){
			datetime=showTextDateTime.getText().toString();
			dateTimeToRetainValue=showTextDateTime.getText().toString();
		}
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		llDisplayDateTime=null;
		showTextDateTime=null;
		tvDateTime=null;
	}

	private void openDialog(int steps, final String date_format, final String field_type, final String maxDate, final String existingFieldValue) {
		try {
			DateTimePicker.setSteps(steps);
			final Dialog mDateTimeDialog = new Dialog(context);
			final RelativeLayout mDateTimeDialogView = (RelativeLayout) context
					.getLayoutInflater().inflate(R.layout.date_time_dialog, null);
			final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
					.findViewById(R.id.DateTimePicker);
			mDateTimeDialogView.findViewById(R.id.SetDateTime).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							mDateTimePicker.clearFocus();
							String returnDateTime = mDateTimePicker.returnDateTime(date_format);
							mDateTimeDialog.dismiss();
							displayDateTime(returnDateTime);
						}

						private void displayDateTime(String returnDateTime) {
							showTextDateTime.setText(returnDateTime);
							datetime = returnDateTime;
						}
					});

			mDateTimeDialogView.findViewById(R.id.CancelDialog).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							mDateTimeDialog.cancel();
						}
					});
			mDateTimeDialogView.findViewById(R.id.setcurrentDate)
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDateTimePicker.reset();
				}
			});
			mDateTimePicker.setIs24HourView(false);
			if (!field_type.equals("datetime") && !field_type.equals("startDateTimeDifference") && !field_type.equals("endDateTimeDifference")) {
				mDateTimePicker.setButtonsVisibility(View.GONE);
			}
			if (!maxDate.equals("")) { 
				mDateTimeDialogView.findViewById(R.id.setcurrentDate).setVisibility(View.GONE);
				mDateTimePicker.setMaxDate(getDate(maxDate));
			}

			Calendar calendar = getExistingFieldValueCalender(existingFieldValue, date_format);
			mDateTimePicker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
			mDateTimePicker.updateTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
			mDateTimePicker.controlShow(field_type);
			mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDateTimeDialog.setContentView(mDateTimeDialogView);
			mDateTimeDialog.show();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("SimpleDateFormat")
	private long getDate(String string) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat(config.getField_options().getDate_format()); //$NON-NLS-1$
		sdf.setTimeZone(TimeZone.getTimeZone("UTC")); //$NON-NLS-1$
		Date d = sdf.parse(string);
		return d.getTime();
	}

	private Calendar getExistingFieldValueCalender(String existingTime,
			String dateFormat) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		if (!existingTime.equals("")) {
			calendar.setTime(getExistingDate(existingTime, dateFormat));
		}
		return calendar;
	}

	private Date getExistingDate(String existingTime, String dateFormat) throws ParseException {
		return new SimpleDateFormat(dateFormat).parse(existingTime);
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(llDisplayDateTime!=null){
			llDisplayDateTime.setVisibility(View.GONE);
			llDisplayDateTime.invalidate();
		}
	}

	@Override
	public void showField() {
		if(llDisplayDateTime!=null){
			llDisplayDateTime.setVisibility(View.VISIBLE);
			llDisplayDateTime.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")){
			if(datetime.equals(value) || datetime.equals("")){
				return true;
			}
		} else if(condition.equals("is greater than")){
			if(Integer.parseInt(datetime)>Integer.parseInt(value) || datetime.equals("")){
				return true;
			}
		} else if(condition.equals("is less than")){
			if(Integer.parseInt(datetime)<Integer.parseInt(value) || datetime.equals("")){
				return true;
			}
		}
		return false;
	}

    public boolean isHidden(){
        if(llDisplayDateTime!=null) {
            return !llDisplayDateTime.isShown();
        } else {
            return false;
        }
    }
}