package me.nool.android.content;

import java.util.HashMap;

import me.nool.android.dba.NoolDB;
import me.nool.android.dba.NoolDB.StreamTable;
import me.nool.android.entity.EntryStatus;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class NoolContentProvider extends ContentProvider {
	
	private static final class Path {
		
		public static final String STREAM = StreamTable.NAME; 
		public static final String NEXT_TS = STREAM + "/" + "nextentryts";
	}
	public static final String AUTHORITY = "me.nool.android";
	
	public static final Uri STREAM_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + Path.STREAM);
	public static final Uri NEXT_ENTRY_TS_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + Path.NEXT_TS);

	// private static final String TAG = "HomeEntryContentProvider";
	private static final int STREAM_ENTRIES = 1;
	private static final int NEXT_ENTRY_TS = 2;
	
	
	private static final UriMatcher sUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	private static HashMap<String, String> entriesProjectionMap = new HashMap<String, String>();

	static {
		sUriMatcher.addURI(AUTHORITY, Path.STREAM, STREAM_ENTRIES);
		sUriMatcher.addURI(AUTHORITY, Path.NEXT_TS + "/" + "", NEXT_ENTRY_TS);
		entriesProjectionMap.put(NoolDB.KEY_ID, NoolDB.KEY_ID);
		entriesProjectionMap.put(NoolDB.KEY_TIMESTAMP, NoolDB.KEY_TIMESTAMP);
		entriesProjectionMap
				.put(NoolDB.KEY_THREAD_NAME, NoolDB.KEY_THREAD_NAME);
		entriesProjectionMap.put(NoolDB.KEY_ICON_URL, NoolDB.KEY_ICON_URL);
		entriesProjectionMap
				.put(NoolDB.KEY_CONTENT_TXT, NoolDB.KEY_CONTENT_TXT);
	}

	private NoolDBHelper dbHelper;

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case STREAM_ENTRIES:
			count = db.delete(StreamTable.NAME, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;

	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case STREAM_ENTRIES:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.me.nool.android.stream";
		case NEXT_ENTRY_TS:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.me.nool.android.stream.nextentryts";
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {

		switch (sUriMatcher.match(uri)) {
		case STREAM_ENTRIES:

			ContentValues values;
			if (initialValues != null) {
				values = new ContentValues(initialValues);
			} else {
				values = new ContentValues();
			}

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			long rowId = db.insert(StreamTable.NAME, NoolDB.KEY_CONTENT_TXT,
					values);
			if (rowId > 0) {
				getContext().getContentResolver().notifyChange(STREAM_CONTENT_URI, null);
				Uri returnUri = ContentUris.withAppendedId(STREAM_CONTENT_URI,
						rowId);
				return returnUri;
			}

			throw new SQLException("Failed to insert row into " + uri);

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public boolean onCreate() {
		dbHelper = new NoolDBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = null;
		switch (sUriMatcher.match(uri)) {
		case STREAM_ENTRIES:
			qb.setTables(StreamTable.NAME);
			qb.setProjectionMap(entriesProjectionMap);
			c = qb.query(db, projection, selection, selectionArgs, null,
					null, sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			break;
		case NEXT_ENTRY_TS:
			qb.setTables(StreamTable.NAME);
			String[] columns = {NoolDB.KEY_TIMESTAMP};
			c = qb.query(db, columns, NoolDB.KEY_STATUS + " = " + EntryStatus.UPCOMING.getInt() , null, null,
					null, NoolDB.KEY_TIMESTAMP, "2");
			
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case STREAM_ENTRIES:
			count = db.update(StreamTable.NAME, values, where, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private static class NoolDBHelper extends SQLiteOpenHelper {
		
		
		public NoolDBHelper(Context context) {
			super(context, NoolDB.DATABASE_NAME, null, NoolDB.DATABASE_VERSION);
		}

		// Called when no database exists in disk and the helper class needs
		// to create a new one.
		@Override
		public void onCreate(SQLiteDatabase _db) {
			Log.i("NoolDBHelper", "Creating Database");
			_db.execSQL(NoolDB.StreamTable.CREATE_TABLE);
			_db.execSQL(NoolDB.StreamTable.CREATE_INDEX_TS);
			// TODO -- add all required tables here
		}

		// Called when there is a database version mismatch meaning that the
		// version
		// of the database on disk needs to be upgraded to the current version.
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Log the version upgrade.
			Log.w("NoolDBHelper", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", which will destroy all old data");

			// The simplest case is to drop the old table and create a new one.
			_db.execSQL(NoolDB.StreamTable.DROP_TABLE);
			// TODO -- add all required tables here

			// Create a new one.
			this.onCreate(_db);
		}
	}
}
