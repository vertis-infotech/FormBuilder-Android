package com.vertis.formbuilder;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ViewSwitcher;

@SuppressLint("InflateParams")
public class DateTimePicker extends RelativeLayout implements
View.OnClickListener, OnDateChangedListener, OnTimeChangedListener{

	private static int TIME_PICKER_STEPS = 1;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private ViewSwitcher viewSwitcher;
	private Calendar calendar;
	private LinearLayout datePickerControl;
	private LinearLayout timePickerControl;
	private LinearLayout buttonsLinearLayout;

	public DateTimePicker(Context context) {
		this(context, null);
	}

	public DateTimePicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.datetimepicker, this, true);
		calendar = Calendar.getInstance();
		getIds(inflater);
		setEvents();
	}

	@SuppressLint("InflateParams")
	private void getIds(LayoutInflater inflater) {
		datePickerControl = (LinearLayout) inflater.inflate(
				R.layout.datepicker, null);
		timePickerControl = (LinearLayout) inflater.inflate(
				R.layout.time_picker, null);
		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		datePicker = (DatePicker) datePickerControl
				.findViewById(R.id.DatePicker);
		buttonsLinearLayout = (LinearLayout) findViewById(R.id.buttonlayout);
	}

	private void setEvents() {
		timePicker = (TimePicker) timePickerControl
				.findViewById(R.id.TimePicker);
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setOnTimeChangedListener(this);
		getTimeField();
		findViewById(R.id.settime).setOnClickListener(this);
		findViewById(R.id.setdate).setOnClickListener(this);
	}

	private void getTimeField() {
		try {
			Class<?> classForid = Class.forName("com.android.internal.R$id");
			Field field = classForid.getField("minute");
			NumberPicker mMinuteSpinner = (NumberPicker) timePicker
					.findViewById(field.getInt(null));
			mMinuteSpinner.setMinValue(0);
			mMinuteSpinner.setMaxValue((60 / TIME_PICKER_STEPS) - 1);
			List<String> displayedValues = new ArrayList<String>();
			for (int i = 0; i < 60; i += TIME_PICKER_STEPS) {
				displayedValues.add(String.format("%02d", i));
			}
			mMinuteSpinner.setDisplayedValues(displayedValues
					.toArray(new String[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() ==  R.id.setdate) {
			v.setEnabled(false);
			findViewById(R.id.settime).setEnabled(true);
			viewSwitcher.showPrevious();
		}else if(v.getId() == R.id.settime) {
			//		:
			v.setEnabled(false);
			findViewById(R.id.setdate).setEnabled(true);
			viewSwitcher.showNext();
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		calendar.set(year, monthOfYear, dayOfMonth,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute*TIME_PICKER_STEPS);
	}

	public int get(final int field) {
		return calendar.get(field);
	}

	public void setMaxDate(long maxDate) {
		datePicker.setMaxDate(maxDate);
	}

	public void reset() {
		final Calendar c = Calendar.getInstance();
		updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		updateTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
	}

	public long getDateTimeMillis() {
		return calendar.getTimeInMillis();
	}

	public void setIs24HourView(boolean is24HourView) {
		timePicker.setIs24HourView(is24HourView);
	}

	public boolean is24HourView() {
		return timePicker.is24HourView();
	}

	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		datePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	public void updateTime(int currentHour, int currentMinute) {
		timePicker.setCurrentHour(currentHour);
		timePicker.setCurrentMinute(((int)currentMinute/TIME_PICKER_STEPS));
	}

	public void setButtonsVisibility(int visibility) {
		buttonsLinearLayout.setVisibility(visibility);
	}

	@SuppressLint("SimpleDateFormat")
	public String returnDateTime(String format) {
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	public void controlShow(String type) {
		if (type.equals("birth_date")|| type.equals("date")) {
			viewSwitcher.removeAllViews();
			viewSwitcher.addView(datePickerControl, 0);
			setButtonsVisibility(GONE);
		} else if (type.equals("time")) { 
			viewSwitcher.removeAllViews();
			viewSwitcher.addView(timePickerControl, 0);
			setButtonsVisibility(GONE);
		} else if (type.equals("endDateTimeDifference") 
				|| type.equals("startDateTimeDifference") 
				|| type.equals("date_time")) {
			viewSwitcher.removeAllViews();
			viewSwitcher.addView(datePickerControl, 0);
			viewSwitcher.addView(timePickerControl, 1);
			setButtonsVisibility(VISIBLE);
		}
	}

	public static void setSteps(int steps){
		TIME_PICKER_STEPS = steps;
	}
}