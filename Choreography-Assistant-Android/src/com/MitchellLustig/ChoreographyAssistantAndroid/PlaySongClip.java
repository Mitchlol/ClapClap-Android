package com.MitchellLustig.ChoreographyAssistantAndroid;

import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import com.MitchellLustig.ChoreographyAssistantAndroid.R;

public class PlaySongClip extends BaseActivity {
	String file;
	long start, stop;
	
	MediaPlayer mMediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Intent intent = getIntent();
		file = intent.getStringExtra("data");
		start = intent.getLongExtra("start",0);
		stop = intent.getLongExtra("stop",0);
		log("start is " + start);
		
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(file);
			mMediaPlayer.prepare();
//			mMediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
//				public void onSeekComplete(MediaPlayer mp) {
//					mMediaPlayer.start();
//				}
//			});
			mMediaPlayer.seekTo((int)start);
			mMediaPlayer.start();
			//mMediaPlayer.pause();
			//mMediaPlayer.seekTo((int)start);
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@Override
	protected void onDestroy() {
		mMediaPlayer.reset();
		mMediaPlayer.release();
		super.onDestroy();
	}
	
	

}
