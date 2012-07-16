package me.nool.android.activity;

import java.util.Timer;
import java.util.TimerTask;

import me.nool.android.R;
import me.nool.android.activity.UserPreference.Key;
import me.nool.android.content.NoolContentProvider;
import me.nool.android.dba.NoolDB;
import me.nool.android.dba.NoolDBSetup;
import me.nool.android.entity.EntryStatus;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Home extends FragmentActivity {

	// Static final data
	private final static EntryStatus[] ENTRY_STATUS = EntryStatus.values();
	private final static int VIEWPAGER_COUNT = ENTRY_STATUS.length;
	
	// Reference to user preference & database
	private SharedPreferences mPreferences;
	
	// For local reference
	private Context cxt;

	// Reference to UI views & adapters
	private ViewSwitcher mViewSwitcher;
	private ViewPager mHomePager;
	//private TextView mHeaderTitle;
	private HomePagerAdapter mPagerAdapter;

	// Members for exit operation
	private boolean mExitTrue;
	private Toast mToastExit;

	// Shake to change viewpager fragment
	// private SensorManager mSensorManager;
	// private float mAccel; // acceleration apart from gravity
	// private float mAccelCurrent; // current acceleration including gravity
	// private float mAccelLast; // last acceleration including gravity

	// private final SensorEventListener mSensorListener = new
	// SensorEventListener() {
	//
	// public void onSensorChanged(SensorEvent se) {
	// float x = se.values[0];
	// float y = se.values[1];
	// float z = se.values[2];
	// Log.i("Acceleration", "X "+ x);
	// Log.i("Acceleration", "Y "+ y);
	// Log.i("Acceleration", "Z "+ z);
	// mAccelLast = mAccelCurrent;
	// mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
	// float delta = mAccelCurrent - mAccelLast;
	// mAccel = mAccel * 0.9f + delta; // perform low-cut filter
	// }
	//
	// public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// }
	// };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		cxt = this;
		mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
		mPreferences = PreferenceManager.getDefaultSharedPreferences(cxt);
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.homeSwitcher);
		mHomePager = (ViewPager) findViewById(R.id.homepager);
		//mHeaderTitle = (TextView) findViewById(R.id.headerTitle);
		mHomePager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.big_padding));
		mHomePager.setPageMarginDrawable(R.drawable.stripe_lines);
		mHomePager.setAdapter(mPagerAdapter);
		mHomePager.setCurrentItem(1);
		mHomePager.setOffscreenPageLimit(3);

		mHomePager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int pageSelected) {
				// TODO Auto-generated method stub
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		// mSensorManager = (SensorManager)
		// getSystemService(Context.SENSOR_SERVICE);
		// mSensorManager.registerListener(mSensorListener,
		// mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		// SensorManager.SENSOR_DELAY_NORMAL);
		// mAccel = 0.00f;
		// mAccelCurrent = SensorManager.GRAVITY_EARTH;
		// mAccelLast = SensorManager.GRAVITY_EARTH;

		new InitTask().execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// Getting the preference value from the preference settings

		boolean exit = mPreferences.getBoolean(Key.CB_EXIT, true);
		// If no confirmation required or user pressing for second time, then
		// exit app.
		if (exit && !mExitTrue) {
			mExitTrue = true;
			mToastExit = Toast.makeText(cxt,
					getResources().getString(R.string.confirm_exit_msg),
					Toast.LENGTH_SHORT);
			mToastExit.show();
			new Timer("cancelExit").schedule(new TimerTask() {
				@Override
				public void run() {
					Home.this.mExitTrue = false;
				}
			}, 2000);
		} else {
			if (mToastExit != null) {
				mToastExit.cancel();
			}
			super.onBackPressed();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// mSensorManager.unregisterListener(mSensorListener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// mSensorManager.registerListener(mSensorListener,
		// mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		// SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			Intent myIntent = new Intent(cxt, UserPreference.class);
			startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}



	private class InitTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (mPreferences.getBoolean(Key.FIRST_USE, true)) {
				new NoolDBSetup().init(cxt);
				System.out.println("Invoking send broadcast");
				sendBroadcast(new Intent("me.nool.android.receiver.SCHEDULE_RUN"));
				mPreferences.edit().putBoolean(Key.FIRST_USE, false)
						.commit();
				getContentResolver().notifyChange(NoolContentProvider.STREAM_CONTENT_URI, null);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mViewSwitcher.showNext();
			super.onPostExecute(result);
		}

	}

	public static class HomePagerAdapter extends FragmentPagerAdapter
	// implements TitleProvider
	{
		public HomePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return VIEWPAGER_COUNT;
		}

		@Override
		public Fragment getItem(int position) {
			return ArrayListFragment.newInstance(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return ENTRY_STATUS[position].toString();
		}
		//
		// public String getTitle(int position) {
		// return ENTRY_STATUS[position].toString();
		// }
	}

	public static class ArrayListFragment extends ListFragment implements
			LoaderCallbacks<Cursor> {
		int mNum;
		SimpleCursorAdapter mAdapter;
		private static final int LIST_LOADER = 0x01;

		private static final ViewBinder VIEW_BINDER = new ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if (cursor.getColumnIndex(NoolDB.KEY_TIMESTAMP) == columnIndex) {
					long timestamp = cursor.getLong(columnIndex);
					CharSequence relativeTime = DateUtils
							.getRelativeTimeSpanString(timestamp);
					TextView txView = (TextView) view;
					txView.setText(relativeTime);
					return true;
				}
				return false;
			}
		};

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int num) {
			ArrayListFragment f = new ArrayListFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
			Log.i("TAG", num + "");
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.home_list, container, false);
			// TODO - customise the no content found message
			// TextView message = (TextView) v.findViewById(android.R.id.empty);
			// message.setText("Hello it is working!");
			View topline = v.findViewById(R.id.topLine);
			topline.setBackgroundResource(getTopLineColorId());
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// the XML defined views which the data will be bound to
			int[] to = new int[] { R.id.entry_threadname, R.id.entry_timestamp,
					R.id.entry_preview }; // R.id.entry_icon,

			String columns[] = { NoolDB.KEY_THREAD_NAME, NoolDB.KEY_TIMESTAMP,
					NoolDB.KEY_CONTENT_TXT };
			// create the adapter using the cursor pointing to the desired data
			// as well as the layout information

			mAdapter = new SimpleCursorAdapter(getActivity()
					.getApplicationContext(), R.layout.home_entry_view, null,
					columns, to, 0);
			mAdapter.setViewBinder(VIEW_BINDER);
			// set this adapter as your ListActivity's adapter
			setListAdapter(mAdapter);

			getLoaderManager().initLoader(LIST_LOADER, null, this);
			


		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}

		private int getTopLineColorId() {
			if (mNum == 0) {
				return R.color.dark_green;
			} else if (mNum == 1) {
				return R.color.dark_red;
			} else if (mNum == 2) {
				return R.color.dark_blue;
			} else {
				return R.color.dark_violet;
			}

		}

		// LoaderManager.LoaderCallbacks<Cursor> methods:

		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			String sortOrder = NoolDB.KEY_TIMESTAMP + " DESC";
			if (this.mNum == EntryStatus.UPCOMING.getInt()) {
				sortOrder = NoolDB.KEY_TIMESTAMP;
			}
			

			String[] projection = { NoolDB.KEY_ID, NoolDB.KEY_TIMESTAMP,
					NoolDB.KEY_THREAD_NAME, NoolDB.KEY_ICON_URL,
					NoolDB.KEY_CONTENT_TXT };

			CursorLoader cursorLoader = new CursorLoader(getActivity(),
					NoolContentProvider.STREAM_CONTENT_URI, projection, NoolDB.KEY_STATUS + " = " + mNum,
					null, sortOrder);
			return cursorLoader;
		}

		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			mAdapter.swapCursor(cursor);
		}

		public void onLoaderReset(Loader<Cursor> loader) {
			mAdapter.swapCursor(null);
		}

	}

}
