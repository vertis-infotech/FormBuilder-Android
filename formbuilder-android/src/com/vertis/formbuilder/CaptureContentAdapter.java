package com.vertis.formbuilder;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CaptureContentAdapter extends ArrayAdapter<String>  {
	
	@Override
	public int getCount() {
		return capturedObject.size();
	}

	private ArrayList<String> capturedObject;
	private Context context;
	private Button cross;
	private Capture c;

	public CaptureContentAdapter(Context context, int resource, ArrayList<String>  objects, Capture c) {
		super(context, resource, objects);
		this.context = context;
		this.capturedObject = objects;
		this.c=c;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View return_view= convertView;
		if (return_view == null) {
			LayoutInflater layoutInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return_view = layoutInf.inflate(R.layout.captureadapter, null);
		}
		TextView tv = (TextView)return_view.findViewById(R.id.tvCapturedFile);
		tv.setText(this.capturedObject.get(position));
		cross= (Button)return_view.findViewById(R.id.crossImage);
		cross.setTag(position);
		cross.setOnClickListener(new OnClickListener (){
			@Override
			public void onClick(View v) {
				int pos = (Integer) v.getTag();
				capturedObject.remove(capturedObject.get(pos));
				c.decreaseHeight();
				CaptureContentAdapter.this.notifyDataSetChanged();
			}
		});
		return return_view;
	}
}