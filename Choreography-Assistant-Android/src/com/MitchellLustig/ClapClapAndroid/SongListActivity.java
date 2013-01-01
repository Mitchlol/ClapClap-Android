package com.MitchellLustig.ClapClapAndroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;

public class SongListActivity extends BaseActivity{
	
	EditText searchEdit;
	ListView list;
	
	
	Cursor songCursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.song_list_layout);
		
		searchEdit = (EditText)findViewById(R.id.search_edit);
		list = (ListView)findViewById(R.id.list);
		
		songCursor = getSongCursor();
		SongListAdpter adapter = new SongListAdpter(context, songCursor, true);
		adapter.setFilterQueryProvider(new FilterQueryProvider(){
			public Cursor runQuery(CharSequence constraint) {
				songCursor = getSongCursor(constraint.toString());
				return songCursor;
			}
		});
		
		list.setAdapter(adapter);
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
		
		
		searchEdit.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Filter f = ((SongListAdpter)list.getAdapter()).getFilter();
				f.filter(s.toString());
			}
		});
	}
	
	



	@Override
	protected void onDestroy() {
		songCursor.close();
		super.onDestroy();
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
        mediaCursor = getContentResolver().query(mediaUri, mediaProjection, mediaSelection, null, "artist, title");
    	return mediaCursor;
    }
	
	public Cursor getSongCursor(String constraint){
		String[] words = constraint.split("\\s");
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
        for(int i = 0; i < words.length; i++){
        	mediaSelection = mediaSelection + 
        			" AND (" + 
             		MediaStore.Audio.Media.TITLE + " LIKE '%" + words[i]+"%'" +
             		" OR "+ 
             		MediaStore.Audio.Media.ARTIST + " LIKE '%" + words[i]+"%'"
             		+")";
        }
       
       
        mediaCursor = getContentResolver().query(mediaUri, mediaProjection, mediaSelection, null, "artist, title");
    	return mediaCursor;
    }
	
	public class SongListAdpter extends CursorAdapter{
		Cursor cursor;
    	Context context;
    	
    	int name_index;
    	int artist_index;
    	
    	public SongListAdpter(Context context, Cursor cursor, boolean autoRequery) {
			super(context, cursor, autoRequery);
			this.context = context;
    		this.cursor = cursor;
    		
    		
		}	

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			name_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
			artist_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
			
			((TextView)view.findViewById(R.id.title)).setText(cursor.getString(artist_index));
			((TextView)view.findViewById(R.id.info)).setText(cursor.getString(name_index));
		}
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View newView = View.inflate(context, R.layout.song_list_item, null);
			return newView;
		}
		
	}
	
}
