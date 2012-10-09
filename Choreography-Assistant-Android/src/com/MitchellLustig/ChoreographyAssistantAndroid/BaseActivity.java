package com.MitchellLustig.ChoreographyAssistantAndroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.MitchellLustig.ChoreographyAssistantAndroid.R;

public class BaseActivity extends Activity {
	public static final String TAG = "NOELOL";
	
	protected Context context;
	protected Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		handler = new Handler();
	}
	
	
	protected void log(String s){
		Log.i(TAG, s);
	}
	
	
}
