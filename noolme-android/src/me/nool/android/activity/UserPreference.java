package me.nool.android.activity;

import me.nool.android.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserPreference extends PreferenceActivity {

	public static final class Key {
		//Values should be corresponding to the userpreference.xml file
		public static final String FIRST_USE = "first_use";
		public static final String CB_EXIT = "cb_exit";
		public static final String CB_DELETE = "cb_delete";
	}
	
	//Data related to user preference
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_preference);
//		CheckBoxPreference smsPreference = (CheckBoxPreference) findPreference("checkboxSMS");
//		smsPreference.setOnPreferenceClickListener(new OnPreferenceClickListener(){
//			public boolean onPreferenceClick(Preference arg0) {
//				CheckBoxPreference preference = (CheckBoxPreference) arg0;
//				ListPreference listPreference = (ListPreference) findPreference("smsUpdate");
//				listPreference.setEnabled(preference.isChecked());
//				if(preference.isChecked()){
//					Toast.makeText(getApplicationContext(), "Your friends will updated via SMS from your phone", Toast.LENGTH_LONG).show();	
//				}
//				return true;
//			}
//		});
//		
//		
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}	
	
}
