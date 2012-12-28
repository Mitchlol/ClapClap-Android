package com.MitchellLustig.ChoreographyAssistantAndroid;

import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.MitchellLustig.ChoreographyAssistantAndroid.MusicG.DetectorThread;
import com.MitchellLustig.ChoreographyAssistantAndroid.MusicG.OnSignalsDetectedListener;
import com.MitchellLustig.ChoreographyAssistantAndroid.MusicG.RecorderThread;

public class PlaySongClip extends BaseActivity {
	String file;
	long start, stop;
	
	MediaPlayer mMediaPlayer;
	SongMonitorThread mSongMonitorThread;
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
			//mMediaPlayer.start();
			
			RecorderThread recorderThread = new RecorderThread();
			recorderThread.start();
			DetectorThread detectorThread = new DetectorThread(recorderThread);
			detectorThread.setOnSignalsDetectedListener(new OnSignalsDetectedListener() {		
				public void onWhistleDetected() {
					mMediaPlayer.start();
				}
			});
			detectorThread.start();
			
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
		
		mSongMonitorThread = new SongMonitorThread();
		mSongMonitorThread.start();
	}
	@Override
	protected void onDestroy() {
		mSongMonitorThread.stopMonitoring();
		try {
			mSongMonitorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mMediaPlayer.reset();
		mMediaPlayer.release();
		super.onDestroy();
	}
	
	class SongMonitorThread extends Thread{
		boolean stopMonitoring;
		public SongMonitorThread(){
			stopMonitoring = false;
		}
		public void stopMonitoring(){
			stopMonitoring = true;
		}
		@Override
		public void run() {
			while(!stopMonitoring){
				if(mMediaPlayer.isPlaying() && mMediaPlayer.getCurrentPosition() > (int)stop){
					mMediaPlayer.stop();
					finish();
				}
			}
			super.run();
		}
		
	}
	
	

}
