package com.MitchellLustig.ChoreographyAssistantAndroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.MitchellLustig.ChoreographyAssistantAndroid.R;

public class SongViewer extends BaseActivity {
	LinearLayout layout;
	ListView list;
	
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
		
		layout.addView(list);
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
			
			View v = View.inflate(context, android.R.layout.two_line_list_item, null);
			((TextView)v.findViewById(android.R.id.text1)).setText(cursor.getString(clip_name_index));
			String timetext = cursor.getInt(start_index) + " - " + cursor.getInt(stop_index);
			((TextView)v.findViewById(android.R.id.text2)).setText(timetext);
			
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
		startActivityForResult(intent, 1);
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		log("title returnes = " +   data.getStringExtra("title"));
		songClipsDB.addEntry(artist, song, resultCode, data.getStringExtra("title"), data.getLongExtra("start", 0), data.getLongExtra("stop", 0));
		updateList();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
}
