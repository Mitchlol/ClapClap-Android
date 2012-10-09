package com.MitchellLustig.ChoreographyAssistantAndroid;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.MitchellLustig.ChoreographyAssistantAndroid.R;

public class SongClipsDB extends SQLiteOpenHelper {
	Context context;
	SQLiteDatabase db;
	
	public static class Tables{
		public static String SONG_CLIPS = "song_clips";
		public static class SongClips{
			public static String _ID = "_id";
			public static String ARTIST_NAME = "artist_name";
			public static String SONG_NAME = "song_name";
			public static String DURATION = "duration";
			public static String CLIP_NAME = "clip_name";
			public static String START = "start";
			public static String STOP = "stop";
			
		}
	}
	
	public SongClipsDB(Context context){
		super(context, "SongClipsDB", null, 1);
		this.context = context;
		db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+Tables.SONG_CLIPS+"("+
			Tables.SongClips._ID+" INTEGER PRIMARY KEY AUTOINCREMENT "+","+
			Tables.SongClips.ARTIST_NAME+" VARCHAR(64)"+","+
			Tables.SongClips.SONG_NAME+" VARCHAR(64)"+","+
			Tables.SongClips.DURATION+" INTEGER"+","+
			Tables.SongClips.CLIP_NAME+" VARCHAR(64)"+","+
			Tables.SongClips.START+" INTEGER"+","+
			Tables.SongClips.STOP+" INTEGER"+
			")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS pranks");

	}
	
	//My Functions
	public boolean addEntry(String artist, String song, long duration, String title, long start, long end){

	
		ContentValues values = new ContentValues();
		values.put(Tables.SongClips.ARTIST_NAME, artist);
		values.put(Tables.SongClips.SONG_NAME, song);
		values.put(Tables.SongClips.DURATION, duration);
		values.put(Tables.SongClips.CLIP_NAME, title);
		values.put(Tables.SongClips.START, start);
		values.put(Tables.SongClips.STOP, end);
		
		db.insert(Tables.SONG_CLIPS, null, values);
	
		return true;
	}
	
	
	public int getEntryCount(){
		Cursor cursor = db.query(Tables.SONG_CLIPS, new String[]{"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor.getInt(cursor.getColumnIndex("count(*)"));
	}
	
	public Cursor getEntriesForSong(String artist, String title, long duration){
		return db.query(
				Tables.SONG_CLIPS,//table, 
				new String[]{Tables.SongClips.CLIP_NAME, Tables.SongClips.START, Tables.SongClips.STOP},//columns, 
				Tables.SongClips.SONG_NAME+"='"+title+"'",//selection, (shitty check for now) 
				null,//selectionArgs, 
				null,//groupBy, 
				null,//having, 
				null//orderBy
				);
	}
}
