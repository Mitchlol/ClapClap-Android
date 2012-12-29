package com.MitchellLustig.ClapClapAndroid;

import java.io.IOException;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import com.MitchellLustig.ClapClapAndroid.R;
import com.MitchellLustig.ClapClapAndroid.MusicG.DetectorThread;
import com.MitchellLustig.ClapClapAndroid.MusicG.OnSignalsDetectedListener;
import com.MitchellLustig.ClapClapAndroid.MusicG.RecorderThread;

public class PlaySongClip extends BaseActivity {
	String file;
	long start, stop;
	
	MediaPlayer mMediaPlayer;
	SongMonitorThread mSongMonitorThread;
	
	RecorderThread recorderThread;
	DetectorThread detectorThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView text = new TextView(context);
		text.setText("All Set! Here is how it works! \n1)Clap twice to play clip \n2)Clap twice to restart clip (only after it has ended) \n3)Press back to exit! \n4)blow me, cus i like blowjobs, and i can write what i want!");
		text.setTextSize(20);
		
		setContentView(text);
		
		Intent intent = getIntent();
		file = intent.getStringExtra("data");
		start = intent.getLongExtra("start",0);
		stop = intent.getLongExtra("stop",0);
		log("start is " + start);
		
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(file);
		}catch (IllegalArgumentException e1){
			e1.printStackTrace();
		}catch (IllegalStateException e1){
			e1.printStackTrace();
		}catch (IOException e1){
			e1.printStackTrace();
		}

		
		mSongMonitorThread = new SongMonitorThread();
		mSongMonitorThread.start();
		
		recorderThread = new RecorderThread();
		recorderThread.start();
		
		detectorThread = new DetectorThread(recorderThread);
		detectorThread.start();
		
		prepareMusicAndListenForClaps();
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	public void prepareMusicAndListenForClaps(){
		try {
			mMediaPlayer.prepare();
			mMediaPlayer.seekTo((int)start);
			detectorThread.setOnSignalsDetectedListener(new OnSignalsDetectedListener() {		
				public void onClapDetected() {
					//detectorThread.stopDetection();
					detectorThread.setOnSignalsDetectedListener(null);
					mMediaPlayer.start();
				}
			});
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		mSongMonitorThread.stopMonitoring();
		try {
			mSongMonitorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		detectorThread.stopDetection();
		recorderThread.stopRecording();
		
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
					//make it impossable for clips to restart themslves!
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					prepareMusicAndListenForClaps();
					//finish();
				}
			}
			super.run();
		}
		
	}
	
	

}
