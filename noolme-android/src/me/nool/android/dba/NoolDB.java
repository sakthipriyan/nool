package me.nool.android.dba;

public class NoolDB {

	// Global database constants
	public static final String DATABASE_NAME = "Nool.db";
	public static final int DATABASE_VERSION = 1;

	// The index (key) column name for use in where clauses.
	public static final String KEY_ID = "_id";
	public static final String KEY_THREAD_ID = "thread_id";
	public static final String KEY_THREAD_NAME = "thread_name";
	public static final String KEY_CONTENT_ID = "content_id";
	public static final String KEY_CONTENT_TXT = "content_txt";
	public static final String KEY_ICON_URL = "image_url";
	public static final String KEY_STATUS = "status";
	public static final String KEY_TIMESTAMP = "timestamp";

	public static final class StreamTable {

		public static final String NAME = "stream";
		public static final String[] COLUMNS = { KEY_ID, KEY_THREAD_ID,
				KEY_THREAD_NAME, KEY_ICON_URL, KEY_CONTENT_ID, KEY_CONTENT_TXT,
				KEY_TIMESTAMP, KEY_STATUS };
		public static final String[] VIEW_COLUMNS = { KEY_ID, KEY_THREAD_NAME,
				KEY_ICON_URL, KEY_CONTENT_TXT, KEY_TIMESTAMP };

		public static final String CREATE_TABLE = "create table " + NAME + " ("
				+ KEY_ID + " integer primary key autoincrement, "
				+ KEY_THREAD_ID + " integer not null," + KEY_THREAD_NAME
				+ " text not null, " + KEY_ICON_URL + " text not null, "
				+ KEY_CONTENT_ID + " integer not null," + KEY_CONTENT_TXT
				+ " text not null, " + KEY_TIMESTAMP + " integer not null,"
				+ KEY_STATUS + " integer not null);";

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME;

		public static final String CREATE_INDEX_TS = "create index " + NAME
				+ "_" + KEY_TIMESTAMP + " on " + NAME + "(" + KEY_TIMESTAMP
				+ ");";

	}
}
