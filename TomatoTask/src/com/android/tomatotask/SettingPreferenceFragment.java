package com.android.tomatotask;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
//import android.preference.Preference;
//import android.preference.PreferenceScreen;
//import android.preference.RingtonePreference;
import android.support.v4.preference.PreferenceFragment;
import android.widget.Toast;

public class SettingPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	ListPreference lstPre_TomatoTime_value, lstPre_BreakTime_value;
	public SettingPreferenceFragment() {
		// TODO �Զ����ɵĹ��캯�����
	}
	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO �Զ����ɵķ������
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
		prefs.registerOnSharedPreferenceChangeListener(this);
		lstPre_TomatoTime_value=(ListPreference)findPreference("TomatoTime_value");
		lstPre_BreakTime_value=(ListPreference)findPreference("BreakTime_value");
		lstPre_TomatoTime_value.setSummary(lstPre_TomatoTime_value.getEntry());
		lstPre_BreakTime_value.setSummary(lstPre_BreakTime_value.getEntry());
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO �Զ����ɵķ������
		if (key.equals("TomatoTime_value")) {
			lstPre_TomatoTime_value.setSummary(lstPre_TomatoTime_value.getEntry());
		}
		if (key.equals("BreakTime_value")) {
			lstPre_BreakTime_value.setSummary(lstPre_BreakTime_value.getEntry());
		}
	}
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO �Զ����ɵķ������
		if (preference.getKey().equals("clearCount")) {
			alertDialogShow();
		}
		if (preference.getKey().equals("aboutTomatoTask")) {
			Uri uri = Uri.parse("http://baike.baidu.com/link?url=b7rlhS6YssFup2xqAjnw9__6VsQnyhtVT8Gx_-qwckUE4IZ-ns6i_jw9w_aKH-C_sjWheb9NFR_GZcfUII0bV_");
			startActivity(new Intent(Intent.ACTION_VIEW,uri));
		}
		if (preference.getKey().equals("supportAuthor")) {
			Uri uri = Uri.parse("https://m.alipay.com/personal/payment.htm?userId=2088302435546204&reason=%E6%94%AF%E6%8C%81%E8%BD%AF%E4%BB%B6%E5%BC%80%E5%8F%91&weChat=true");  
			startActivity(new Intent(Intent.ACTION_VIEW,uri));
		}
		return false;
	}
	
	/**
	 * ��ʾAlertDialog
	 */
	private void alertDialogShow() {
		new AlertDialog.Builder(getActivity()).setTitle("�����").setMessage("�Ƿ����������\nע���ò��������棡").setPositiveButton("���", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TomatoCount",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putInt("todayTomatoCount", 0);
				editor.putInt("allTomatoCount", 0);
				editor.commit();
				Toast.makeText(getActivity(), "����ɹ���", Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
			}
		}).create().show();
	}
	

}
