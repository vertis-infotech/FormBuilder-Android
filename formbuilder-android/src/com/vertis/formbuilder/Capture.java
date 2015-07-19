package com.vertis.formbuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.Expose;
import com.vertis.formbuilder.parser.FieldConfig;
import com.vertis.formbuilder.util.FormBuilderUtil;

public class Capture implements IField{

	private static final int ACTION_LOAD_IMAGE = 100;
	private FieldConfig config;
	//Views
	LinearLayout llCapture;
	TextView tvCapture;
	Button buttonCaptureImage, buttonCaptureVideo, buttonCaptureAudio;
	ArrayAdapter<String> contentAdapter;
	ArrayList<String> captureList= new ArrayList<String>();
	ListView lvCapture;

	//Values
	@Expose
	String cid;
	@Expose
	String capturedFile="";
	private Activity context;
	private Uri uri;

	public Capture(FieldConfig fcg){
		this.config=fcg;
	}

	@Override
	public void createForm(final Activity context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
		llCapture=(LinearLayout) inflater.inflate(R.layout.capture,null);
		tvCapture = (TextView) llCapture.findViewById(R.id.textViewCapture);
		buttonCaptureImage=(Button) llCapture.findViewById(R.id.buttonImage);
		buttonCaptureVideo=(Button) llCapture.findViewById(R.id.buttonVideo);
		buttonCaptureAudio=(Button) llCapture.findViewById(R.id.buttonAudio);
		lvCapture=(ListView) llCapture.findViewById(R.id.listCapturedObjects);
		tvCapture.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
		tvCapture.setTypeface(new FormBuilderUtil().getFontFromRes(context));
		buttonCaptureImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				if (uri == null) {
					uri = getOutputVideoUri("PIC", ".jpg");
				}
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				context.startActivityForResult(cameraIntent, ACTION_LOAD_IMAGE);

				Toast.makeText(context, "Image Captured.", Toast.LENGTH_SHORT).show();
				captureList.add("Image");
				contentAdapter.notifyDataSetChanged();
				modifyListViewHeight();
			}
		});
		buttonCaptureVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Video Captured.", Toast.LENGTH_SHORT).show();
				captureList.add("Video");
				contentAdapter.notifyDataSetChanged();
				modifyListViewHeight();
			}
		});
		buttonCaptureAudio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Audio Captured.", Toast.LENGTH_SHORT).show();
				captureList.add("Audio");
				contentAdapter.notifyDataSetChanged();
				modifyListViewHeight();
			}
		});
		setViewValues();
		mapView();
		setValues();
		noErrorMessage();
	}

	private static Uri getOutputVideoUri(String prefix, String safix) {
		if (Environment.getExternalStorageState() == null) {
			return null;
		}
		File mediaStorage = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/SheqScan/uploadFiles");
		if (!mediaStorage.exists()) {
			if (!mediaStorage.mkdirs()) {
				Log.e("TAG", "failed to create directory: " + mediaStorage);
			}
			return null;
		}
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss",
				Locale.US).format(new Date());
		File mediaFile = new File(mediaStorage, prefix + "_" + timeStamp
				+ safix);
		return Uri.fromFile(mediaFile);
	}

	public void decreaseHeight(){
		int lvHeight = captureList.size()*62;
		lvCapture.setLayoutParams(new LayoutParams(lvCapture.getWidth(), lvHeight));
	}
	private void modifyListViewHeight(){
		int lvHeight = captureList.size()*62;
		lvCapture.setLayoutParams(new LayoutParams(lvCapture.getWidth(), lvHeight));
	}

	@SuppressLint("ResourceAsColor")
	private void noErrorMessage() {
		if(tvCapture==null)return;
		tvCapture.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvCapture.setTextColor(R.color.TextViewNormal);
	}

	private void mapView() {
		ViewLookup.mapField(this.config.getCid()+"_1", llCapture);
		ViewLookup.mapField(this.config.getCid()+"_1_1", buttonCaptureImage);
		ViewLookup.mapField(this.config.getCid()+"_1_1", buttonCaptureVideo);
		ViewLookup.mapField(this.config.getCid()+"_1_1", buttonCaptureAudio);
	}

	private void setViewValues() {
		tvCapture.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvCapture.setTextColor(-1);
		contentAdapter= new CaptureContentAdapter(context, R.layout.capture, captureList, Capture.this);
		lvCapture.setAdapter(contentAdapter);
		//modifyListViewHeight();
	}

	@Override
	public ViewGroup getView() {
		return llCapture;
	}

	@Override
	public boolean validate() {
		boolean valid;
		if(config.getRequired() && captureList.isEmpty()){
			valid=false;
			errorMessage(" Required");
		}else{
			valid=true;
		}
		return valid;
	}

	@SuppressLint("ResourceAsColor")
	private void errorMessage(String message) {
		if(tvCapture==null)return;
		tvCapture.setText(this.config.getLabel() + (this.config.getRequired()?"*":"") );
		tvCapture.setText(tvCapture.getText() + message);
		tvCapture.setTextColor(R.color.ErrorMessage);
	}

	@Override
	public void setValues() {
		this.cid=config.getCid();
		validate();
	}

	@Override
	public void clearViews() {
		setValues();
		llCapture=null;
		tvCapture=null;
		buttonCaptureImage=null;
		buttonCaptureVideo=null;
		buttonCaptureAudio=null;
	}

	public String getCIDValue() {
		return this.config.getCid();
	}

	public void hideField() {
		if(llCapture!=null){
			llCapture.setVisibility(View.GONE);
			llCapture.invalidate();
		}
	}

	@Override
	public void showField() {
		if(llCapture!=null){
			llCapture.setVisibility(View.VISIBLE);
			llCapture.invalidate();
		}
	}

	public boolean validateDisplay(String value,String condition) {
		if(condition.equals("equals")) {
			for (String capture : captureList) {
				if (capture.equals(value)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

    public boolean isHidden(){
        if(llCapture!=null) {
            return !llCapture.isShown();
        } else {
            return false;
        }
    }
}