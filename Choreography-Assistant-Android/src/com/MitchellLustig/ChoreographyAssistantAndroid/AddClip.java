package com.MitchellLustig.ChoreographyAssistantAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AddClip extends BaseActivity {
	EditText title;//, start, stopMonitoring;
	TextView startTime, stopTime;
	SeekBar startSeek, stopSeek;
	Button save;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_clip);
		
		title = (EditText)findViewById(R.id.title);
		//start = (EditText)findViewById(R.id.start);
		startTime = (TextView)findViewById(R.id.start_time);
		startSeek = (SeekBar)findViewById(R.id.start_seek);
		stopTime = (TextView)findViewById(R.id.stop_time);
		stopSeek = (SeekBar)findViewById(R.id.stop_seek);
		//stopMonitoring = (EditText)findViewById(R.id.stop);
		save = (Button)findViewById(R.id.save);
		
		
		startSeek.setMax((int)(getIntent().getLongExtra("duration", 0)/1000));
		startSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}	
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				startTime.setText(MStoHRBS(""+(progress*1000l)));
				if(stopSeek.getProgress() < progress){
					stopSeek.setProgress(progress);
				}
			}
		});
		
		stopSeek.setMax((int)(getIntent().getLongExtra("duration", 0)/1000));
		stopSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}	
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				stopTime.setText(MStoHRBS(""+(progress*1000l)));
				if(startSeek.getProgress() > progress){
					startSeek.setProgress(progress);
				}
			}
		});
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("title", title.getText().toString());
				data.putExtra("start", startSeek.getProgress() * 1000l);
				data.putExtra("stopMonitoring", stopSeek.getProgress() * 1000l);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
}
