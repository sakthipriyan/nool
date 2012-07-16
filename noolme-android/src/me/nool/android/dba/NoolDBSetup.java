package me.nool.android.dba;

import me.nool.android.content.NoolContentProvider;
import me.nool.android.entity.EntryStatus;
import android.content.ContentValues;
import android.content.Context;

public class NoolDBSetup {

	private static String[] threadNames ={"Birthday","Marriage","Lunch","Money","Books","Movie","Code"};
	
	public void init(Context _context) {
		
		for (int i = 0; i < 200; i++) {
			int status = i % 4;
			EntryStatus eStatus = EntryStatus.getStatus(status);
			long randomTime = (long) (Math.random() * 50000000);
			long entryTime = System.currentTimeMillis();
			ContentValues values = new ContentValues();
			if (EntryStatus.UPCOMING.equals(eStatus)) {
				entryTime += randomTime;
			} else {
				entryTime -= randomTime;
			}
			values.put(NoolDB.KEY_CONTENT_ID,i);
			values.put(NoolDB.KEY_CONTENT_TXT,"Your birthday! Go out!!.. Enjoy the life! Achieve the dreams. Some dummy txt here");
			values.put(NoolDB.KEY_ICON_URL,"");
			values.put(NoolDB.KEY_STATUS,eStatus.getInt());
			values.put(NoolDB.KEY_THREAD_ID,i);
			values.put(NoolDB.KEY_THREAD_NAME, threadNames[i%7]);
			values.put(NoolDB.KEY_TIMESTAMP,entryTime);
			
			//Uri uri =
				_context.getContentResolver().insert(
					NoolContentProvider.STREAM_CONTENT_URI, values);
			//System.out.println(uri.toString());
		}
	}
}
