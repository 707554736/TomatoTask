package com.android.tomatotask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TomatoFragment extends Fragment{
//	private CircleProgressBar tomatoProgressBar;
	private TextView tomatoTxtView,todayTomatoCountTextView,allTomatoCountTextView;
	private ImageView imageView;
	private View mMainView;
	private int todayTomatoCount;
	private int allTomatoCount;

	public TomatoFragment() {
		// TODO �Զ����ɵĹ��캯�����
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.tomato_layout, (ViewGroup)getActivity().findViewById(R.id.viewpager),false);

		tomatoTxtView = (TextView)mMainView.findViewById(R.id.tomatoTxtView);
		todayTomatoCountTextView = (TextView)mMainView.findViewById(R.id.todayTomatoCount);
		allTomatoCountTextView = (TextView)mMainView.findViewById(R.id.allTomatoCount);
		imageView =  (ImageView)mMainView.findViewById(R.id.imageTomato);
		//�޸�����
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
		tomatoTxtView.setTypeface(fontFace);
		todayTomatoCountTextView.setTypeface(fontFace);
		allTomatoCountTextView.setTypeface(fontFace);
		

		Log.i("MAIN", "++++++++TomatoFragment++++++++onCreate++++++++");
		imageView.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
			}
		});
		
	}

	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO �Զ����ɵķ������
		Log.i("MAIN", "++++++++TomatoFragment++++++++onCreateView++++++++");
		ViewGroup p = (ViewGroup)mMainView.getParent();
		if (p!=null) {
			p.removeAllViewsInLayout();
			Log.i("MAIN", "++++++++TomatoFragment++++++++removeAllViewsInLayout!!!!!!!!");
		}
			return mMainView;
		}
	
	@Override
	public void onResume() {
		// TODO �Զ����ɵķ������
		super.onResume();
		Log.i("MAIN", "++++++++TomatoFragment++++++++onResume++++++++");
		//ʱ�����ڵĶ�ȡ�����ʱ�䲻�ǽ����ʱ�䣬���ý��շ�����Ϊ 0 
		// ���ö�ȡ
		SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TomatoCount",
				Activity.MODE_PRIVATE);
		String dateStr = mySharedPreferences.getString("date", "2001-01-01");
		todayTomatoCount = mySharedPreferences.getInt("todayTomatoCount", 0);//��ȡ�洢�Ľ��շ���ʱ��
		allTomatoCount = mySharedPreferences.getInt("allTomatoCount", 0);//��ȡ�洢�ĺϼƷ���ʱ��
		String dateNowString = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(new java.util.Date());
		if (!dateStr.equals(dateNowString)) {//�жϴ洢ʱ���Ƿ�͵�ǰʱ����ͬһ��
			todayTomatoCount=0;
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putInt("todayTomatoCount", todayTomatoCount);
			editor.commit();
		}
		todayTomatoCountTextView.setText("����:"+todayTomatoCount);
		allTomatoCountTextView.setText("�ܼ�:"+allTomatoCount);
		Log.v("MAIN", "----TomatoFragment---todayTomatoCount---------"+todayTomatoCount+"-------");
		Log.v("MAIN", "----TomatoFragment---allTomatoCount---------"+allTomatoCount+"-------");
		Log.v("MAIN", "----TomatoFragment---dateStr---------"+dateStr+"-------");
		
	}
	
	
	@Override
		public void onDestroy() {
			// TODO �Զ����ɵķ������
			super.onDestroy();
			Log.i("MAIN", "++++++++TomatoFragment++++++++onDestroy++++++++");
		}
	@Override
		public void onPause() {
			// TODO �Զ����ɵķ������
			super.onPause();
			Log.i("MAIN", "++++++++TomatoFragment++++++++onPause++++++++");
		}

	
	@Override
		public void onStart() {
			// TODO �Զ����ɵķ������
			super.onStart();
			Log.i("MAIN", "++++++++TomatoFragment++++++++onStart++++++++");
		}
	
	@Override
		public void onStop() {
			// TODO �Զ����ɵķ������
			super.onStop();
			Log.i("MAIN", "++++++++TomatoFragment++++++++onStop++++++++");
		}

}
