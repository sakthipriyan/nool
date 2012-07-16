package me.nool.android.content;

import java.sql.Date;
import java.util.Calendar;

import me.nool.android.dba.NoolDB;
import me.nool.android.service.StreamProcessService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class BootServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i("Boot service register","received");
		// Get Time stamp after 5 minutes.
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.SECOND, 10);
		long defaultTS = calendar.getTimeInMillis();

		// Retrieving the next time stamp of the up coming event using the
		// content provider
		long nextEntryTS = 0;
		Cursor c = context.getContentResolver().query(
				NoolContentProvider.NEXT_ENTRY_TS_URI, null, null, null, null);
		if (c.moveToFirst()) {
			nextEntryTS = c.getLong(c.getColumnIndex(NoolDB.KEY_TIMESTAMP));
		}

		if(nextEntryTS != 0){
			 boolean scheduleNextRun = false;
			if(nextEntryTS < defaultTS){
				Intent serviceIntent = new Intent(context, StreamProcessService.class);
				context.startService(serviceIntent);
				if(c.moveToNext()){
					scheduleNextRun = true;
					nextEntryTS = c.getLong(c.getColumnIndex(NoolDB.KEY_TIMESTAMP));
				}
			} else{
				scheduleNextRun = true;
			}
			
			if(scheduleNextRun){
				Intent receiverIntent = new Intent(context, BootServiceReceiver.class);
				PendingIntent pi = PendingIntent.getBroadcast(context, 0, receiverIntent, 0);
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP,nextEntryTS, pi);
				Log.i("Broadcast", "Next run schedule at " + new Date(nextEntryTS).toLocaleString());
			}

		} else{
			Log.i("Broadcast", "Nothing to schedule");
		}
		c.close();
	}

}
