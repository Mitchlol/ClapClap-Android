package com.MitchellLustig.ClapClapAndroid;

import java.io.IOException;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.MitchellLustig.ClapClapAndroid.MusicG.DetectorThread;
import com.MitchellLustig.ClapClapAndroid.MusicG.OnSignalsDetectedListener;
import com.MitchellLustig.ClapClapAndroid.MusicG.RecorderThread;

public class PlaySongClip extends BaseActivity {
	ImageButton imageButton;
	
	String file;
	long start, stop;
	
	MediaPlayer mMediaPlayer;
	SongMonitorThread mSongMonitorThread;
	
	RecorderThread recorderThread;
	DetectorThread detectorThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.play_song_clip_layout);
		imageButton = (ImageButton)findViewById(R.id.button);
		
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
					imageButton.post(new Runnable() {
						public void run() {
							imageButton.setImageResource(R.drawable.stop);
							imageButton.setOnClickListener(new OnClickListener() {
								public void onClick(View v){
									if(mMediaPlayer.isPlaying()){
										mMediaPlayer.stop();
									}
									imageButton.setOnClickListener(null);
									//make it impossable for clips to restart themslves!
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									prepareMusicAndListenForClaps();
								}
							});
						}
					});
				}
			});
			imageButton.setImageResource(R.drawable.play);
			imageButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					detectorThread.setOnSignalsDetectedListener(null);
					mMediaPlayer.start();
					imageButton.setImageResource(R.drawable.stop);
					imageButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v){
							if(mMediaPlayer.isPlaying()){
								mMediaPlayer.stop();
							}
							imageButton.setOnClickListener(null);
							//make it impossable for clips to restart themslves!
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							prepareMusicAndListenForClaps();
						}
					});
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
