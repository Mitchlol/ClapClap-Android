package com.MitchellLustig.ChoreographyAssistantAndroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.MitchellLustig.ChoreographyAssistantAndroid.R;

public class SongPicker extends BaseActivity{
	LinearLayout layout;
	ListView list;
	
	Cursor songCursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		layout = new LinearLayout(context);
		list = new ListView(context);
		
		layout.addView(list);
		
		setContentView(layout);
		
		
		songCursor = getSongCursor();
		list.setAdapter(new SongListAdpter(context, songCursor));
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View item, int position,long id) {
				songCursor.moveToPosition(position);
				Intent intent = new Intent(context, SongViewer.class);
				intent.putExtra("song", songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
				intent.putExtra("artist", songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
				intent.putExtra("duration", songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
				intent.putExtra("data", songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
				startActivity(intent);
			}
		});
	}
	
	public Cursor getSongCursor(){
        Cursor mediaCursor;
    	Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] mediaProjection={
        		MediaStore.Audio.Media._ID,
        		MediaStore.Audio.Media.DATA,
        		MediaStore.Audio.Media.TITLE,
        		MediaStore.Audio.Media.ARTIST,
        		MediaStore.Audio.Media.ALBUM,
        		//MediaStore.Audio.Media.ALBUM_ART,
        		MediaStore.Audio.Media.TRACK,
        		MediaStore.Audio.Media.YEAR,
        		MediaStore.Audio.Media.DURATION,
        		MediaStore.Audio.Media.DATE_MODIFIED};
        String mediaSelection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        mediaCursor = managedQuery(mediaUri, mediaProjection, mediaSelection, null, "artist, title");
    	return mediaCursor;
    }
	
	public class SongListAdpter extends BaseAdapter implements ListAdapter {
		Cursor cursor;
    	Context context;
    	int name_index;
    	int artist_index;

    	public SongListAdpter(Context context, Cursor cursor) {
    		this.context = context;
    		this.cursor = cursor;
    		
    		name_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
			artist_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
    	}
    	public int getCount() {return cursor.getCount();}

    	public Object getItem(int position) {return position;}

    	public long getItemId(int position) {return position;}	

		public View getView(int position, View convertView, ViewGroup parent) {
			cursor.moveToPosition(position);
			
			View v = View.inflate(context, android.R.layout.two_line_list_item, null);
			((TextView)v.findViewById(android.R.id.text1)).setText(cursor.getString(artist_index));
			((TextView)v.findViewById(android.R.id.text2)).setText(cursor.getString(name_index));
			
			return v;
		}
		
	}
	
}
