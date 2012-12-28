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
	
	public static String MStoHRBS(String sms){
    	int ms = Integer.parseInt(sms);
    	int x = ms / 1000;
    	int seconds = x % 60;
    	x /= 60;
    	int minutes = x % 60;
    	x /= 60;
    	int hours = x % 24;
    	String HRBS = new String();
    	if(hours > 0){
    		HRBS += hours + ":";
    	}
    	if(minutes > 0 && minutes < 10 && hours > 0){
    		HRBS += "0" + minutes + ":";
    	}else if(minutes > 0){
    		HRBS += minutes + ":";
    	}else if(minutes == 0 && hours > 0){
    		HRBS += "00:";
    	}else{
    		HRBS += "0:";
    	}
    	if(seconds > 0 && seconds < 10){
    		HRBS += "0" + seconds + "";
    	}else if(seconds > 0){
    		HRBS += seconds + "";
    	}else if(seconds == 0 && hours > 0 || minutes > 0){
    		HRBS += "00";
    	}else{
    		HRBS += "00";
    	}
    	return HRBS;
    }
	
	public static String MStoHRBS(long ms){
    	int x = (int)(ms / 1000);
    	int seconds = x % 60;
    	x /= 60;
    	int minutes = x % 60;
    	x /= 60;
    	int hours = x % 24;
    	String HRBS = new String();
    	if(hours > 0){
    		HRBS += hours + ":";
    	}
    	if(minutes > 0 && minutes < 10 && hours > 0){
    		HRBS += "0" + minutes + ":";
    	}else if(minutes > 0){
    		HRBS += minutes + ":";
    	}else if(minutes == 0 && hours > 0){
    		HRBS += "00:";
    	}else{
    		HRBS += "0:";
    	}
    	if(seconds > 0 && seconds < 10){
    		HRBS += "0" + seconds + "";
    	}else if(seconds > 0){
    		HRBS += seconds + "";
    	}else if(seconds == 0 && hours > 0 || minutes > 0){
    		HRBS += "00";
    	}else{
    		HRBS += "00";
    	}
    	return HRBS;
    }

}
