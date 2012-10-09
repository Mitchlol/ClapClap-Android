package com.MitchellLustig.ChoreographyAssistantAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.MitchellLustig.ChoreographyAssistantAndroid.R;

public class AddClip extends BaseActivity {
	EditText title, start, stop;
	Button save;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_clip);
		
		title = (EditText)findViewById(R.id.title);
		start = (EditText)findViewById(R.id.start);
		stop = (EditText)findViewById(R.id.stop);
		save = (Button)findViewById(R.id.save);
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("title", title.getText().toString());
				data.putExtra("start", Long.parseLong(start.getText().toString()));
				data.putExtra("stop", Long.parseLong(stop.getText().toString()));
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
}
