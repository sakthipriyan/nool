package me.nool.android.service;

import me.nool.android.content.NoolContentProvider;
import me.nool.android.dba.NoolDB;
import me.nool.android.entity.EntryStatus;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StreamProcessService extends Service {
	
	//private NoolDBAdapter mDbAdapter;
	private NotificationManager mNotifyManager;
	private Intent mNotificationIntent;
	

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		//mDbAdapter = new NoolDBAdapter(getApplicationContext()).open();
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationIntent =  new Intent();
	}

	


	@Override
	public void onDestroy() {
		super.onDestroy();
		//mDbAdapter.close();
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
//		String[] projection = {NoolDB.KEY_ID,NoolDB.KEY_TIMESTAMP}; 
//		String selection =  NoolDB.KEY_STATUS + " = ? and " + NoolDB.KEY_TIMESTAMP + " < ?";
//		String[] selectionArgs = {String.valueOf(EntryStatus.UPCOMING.getInt()), String.valueOf(System.currentTimeMillis())};
//		Cursor c = getContentResolver().query(NoolContentProvider.STREAM_CONTENT_URI, projection, selection, selectionArgs, NoolDB.KEY_TIMESTAMP);
//		if(c.moveToFirst()){
//			do{
//				
//			} while(c.moveToNext());
//		}
		Log.i("StreamProcessService", "Starting service");
		ContentValues values = new ContentValues();
		values.put(NoolDB.KEY_STATUS, EntryStatus.OVERDUE.getInt());
		String where = NoolDB.KEY_TIMESTAMP + "  < ? and " + NoolDB.KEY_STATUS + " = ?";
		String[] whereArgs = {String.valueOf(System.currentTimeMillis()), String.valueOf(EntryStatus.UPCOMING.getInt())};
		int count = getContentResolver().update(NoolContentProvider.STREAM_CONTENT_URI, values, where, whereArgs);
		Log.i("StreamProcessService", "Count "+ count);
		notifyUser("No of records updated:"+ count);
		stopSelf();
		return START_STICKY;
				
	}



	//private int processEntry(HomeEntry entry) {
		// TODO Auto-generated method stub
		//EntryStatus status = EntryStatus.OVERDUE;
		//return mDbAdapter.updateEntryStatus(entry.getId(), status);
	//}
	
	private void notifyUser(String message){
		Notification notification = new Notification(me.nool.android.R.drawable.ic_launcher, message,
				System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), message,
				"STATUS UPDATE",
				PendingIntent.getActivity(this, 0, mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

		mNotifyManager.notify(0x0001, notification);

	}
	
	
	
	
	

}
