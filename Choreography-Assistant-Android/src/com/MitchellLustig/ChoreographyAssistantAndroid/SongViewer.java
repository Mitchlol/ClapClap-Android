package com.MitchellLustig.ChoreographyAssistantAndroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SongViewer extends BaseActivity {
	LinearLayout layout;
	ListView list;
	TextView listEmptyView;
	
	SongClipsDB songClipsDB; 
	Cursor clipsCursor;
	
	String artist, song, file;
	long duration;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		songClipsDB = new SongClipsDB(context);
		
		Intent data = getIntent();
		artist = data.getStringExtra("artist");
		song = data.getStringExtra("song");
		file = data.getStringExtra("data");
		duration = data.getLongExtra("duration", 0);
		
		setTitle(song + " " +duration);
		
		layout = new LinearLayout(context);
		list = new ListView(context);
		
		
		
		layout.addView(list, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View item, int position,long id) {
				clipsCursor.moveToPosition(position);
				Intent intent = new Intent(context, PlaySongClip.class);
				intent.putExtra("artist", artist);
				intent.putExtra("song", song);
				intent.putExtra("duration", duration);
				intent.putExtra("data", file);
				intent.putExtra("duration", duration);
				intent.putExtra("title", clipsCursor.getString(clipsCursor.getColumnIndex(SongClipsDB.Tables.SongClips.CLIP_NAME)));
				intent.putExtra("start", clipsCursor.getLong(clipsCursor.getColumnIndex(SongClipsDB.Tables.SongClips.START)));
				intent.putExtra("stop", clipsCursor.getLong(clipsCursor.getColumnIndex(SongClipsDB.Tables.SongClips.STOP)));
				startActivity(intent);
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
				clipsCursor.moveToPosition(position);
				int DB_ROW_ID = clipsCursor.getInt(clipsCursor.getColumnIndex(SongClipsDB.Tables.SongClips._ID));
				songClipsDB.deleteEntry(DB_ROW_ID);
				updateList();
				return false;
			}
		});
		
		listEmptyView = new TextView(context);
		listEmptyView.setGravity(Gravity.CENTER);
		listEmptyView.setText("You have not created any clips yet, press \"Menu\" to add a clip!");
		list.setEmptyView(listEmptyView);
		layout.addView(listEmptyView, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		setContentView(layout);
		
		updateList();
	}
	
	public void updateList(){
		clipsCursor = songClipsDB.getEntriesForSong(artist, song, duration);
		list.setAdapter(new ClipListAdpter(context, clipsCursor));
	}
	
	public class ClipListAdpter extends BaseAdapter implements ListAdapter {
		Cursor cursor;
    	Context context;
    	int clip_name_index;
    	int start_index;
    	int stop_index;

    	public ClipListAdpter(Context context, Cursor cursor) {
    		this.context = context;
    		this.cursor = cursor;
    		
    		clip_name_index = cursor.getColumnIndexOrThrow(SongClipsDB.Tables.SongClips.CLIP_NAME);
    		start_index = cursor.getColumnIndexOrThrow(SongClipsDB.Tables.SongClips.START);
    		stop_index = cursor.getColumnIndexOrThrow(SongClipsDB.Tables.SongClips.STOP);
    	}
    	public int getCount() {return cursor.getCount();}

    	public Object getItem(int position) {return position;}

    	public long getItemId(int position) {return position;}	

		public View getView(int position, View convertView, ViewGroup parent) {
			cursor.moveToPosition(position);
			
			View v = View.inflate(context, R.layout.clip_list_item, null);
			((TextView)v.findViewById(R.id.title)).setText(cursor.getString(clip_name_index));
			String timetext = MStoHRBS(cursor.getLong(start_index)) + " - " + MStoHRBS(cursor.getLong(stop_index));
			((TextView)v.findViewById(R.id.info)).setText(timetext);
			
			return v;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("add clip");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//only 1 item
		Intent intent = new Intent(context, AddClip.class);
		intent.putExtra("artist", artist);
		intent.putExtra("song", song);
		intent.putExtra("duration", duration);
		intent.putExtra("data", file);
		intent.putExtra("duration", duration);
		startActivityForResult(intent, 1);
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			log("title returnes = " +   data.getStringExtra("title"));
			songClipsDB.addEntry(artist, song, resultCode, data.getStringExtra("title"), data.getLongExtra("start", 0), data.getLongExtra("stopMonitoring", 0));
			updateList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
}
