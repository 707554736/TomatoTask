package com.android.tomatotask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.android.tomatotask.R.string;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private CircleProgressBar progressBar;
	private TextView textView;
	private TimeCount time;
	protected Animation animation;
	private CharSequence tomatocharSequence;
	private CharSequence breakcharSequence;
	private int timeSpan;
	private final String TAG = "Main";
	private int flag = 1;// ��־λ��1:����δ��ʼ��2:���ѿ�ʼ��ģ�3:����ʱ�䵽ͬʱ��Ϣʱ��δ��ʼ��4:��Ϣʱ�俪ʼ��ģ�1:��Ϣʱ�䵽����δ��ʼ
	private int count = 0; // ͳ�������������ǽ�ͳ�Ƶ����ݴ浽���浱��ȥ��ͬʱ�ǵ�д���ļ���
	private int percentTime = 0;// ���õ���������������ԲȦ�İٷֱ�����
	private boolean flagHide = true;// trueΪ��ʾ����ʱ�䣬falseΪ����ʾ����ʱ��
	private int tick = 0;
	private int rest = 0;
	private int longrest = 0;
	private int maxProgress;// �����ȣ���ֵ��CircleProgressBar���е�maxProgress������ͬ
	private long exitTime = 0;
	private int[] ID;
	
//	//ViewPager
//	LocalActivityManager manager =null;
//	ViewPager pager = null;
//	TabHost tabHost = null;
//	
//	private int offset = 0;// ����ͼƬƫ����
//	private int currIndex = 0;// ��ǰҳ�����
//	private int bmpW;// ����ͼƬ���
//	private ImageView cursor;// ����ͼƬ
//	private List<View> listViews; // Tabҳ���б�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * ��������assets�´���fonts�ļ��в����������ļ���ͬʱ�ṩ���·������Typeface����
		 * ��ʹ���ⲿ����ȴ�ַ��������ޱ仯ʱ����Droid Sans���棩��ͨ������Ϊ������androidû��֧��
		 */
		Log.v("MAIN", "-------MainActivity---------onCreate-------");
		Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		textView = (TextView) this.findViewById(R.id.txtView);
		textView.setTypeface(fontFace);
		progressBar = (CircleProgressBar) findViewById(R.id.circleProgressbar);
		tomatocharSequence = (CharSequence) (getResources()
				.getString(R.string.tomatocharSequence));
		breakcharSequence = (CharSequence) (getResources()
				.getString(R.string.breakcharSequence));
		// ���ö�ȡ
		SharedPreferences mySharedPreferences = getSharedPreferences("test",
				Activity.MODE_PRIVATE);
		tick = mySharedPreferences.getInt("tick", 25);
		tick = 1;//�ǵ�ɾ��
		rest = mySharedPreferences.getInt("rest", 5);
		longrest = mySharedPreferences.getInt("longrest", 15);

		//����progressBar������
		progressBar.setMaxProgress(tick * 60);
		progressBar.setOnClickListener(this);

		//������Դ�ļ�
		ID = new int[] { R.anim.my_alpha_action, R.anim.my_scale_action,
				R.anim.my_rotate_action, R.anim.alpha_scale,
				R.anim.alpha_rotate, R.anim.scale_rotate,
				R.anim.alpha_scale_rotate, R.anim.myown_design };
		
		
		stateFlag();

	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}

		/**
		 * ��ʱ���ʱ����
		 */
		@Override
		public void onFinish() {
			// TODO �Զ����ɵķ������
			switch (flag) {
			case 2:// ���ѿ�ʼ��ģ������ʱ����ʱ�䵽�ˣ��޸�flag����ʾʱ�䵽
				flag = 3;
				if (flagHide) {
					textView.setVisibility(View.VISIBLE);
					flagHide = false;
					// ���Ӷ���Ч��
					int randow= new Random().nextInt(8);
					animation = AnimationUtils.loadAnimation(MainActivity.this, ID[randow]);
					textView.startAnimation(animation);
				}
				
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = getSharedPreferences("TomatoCount",
						Activity.MODE_PRIVATE);
				int todayTomatoCount;
				int allTomatoCount;
				todayTomatoCount = mySharedPreferences.getInt("todayTomatoCount", 0);
				allTomatoCount = mySharedPreferences.getInt("allTomatoCount", 0);
				String dateNowString = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(new java.util.Date());//��ȡ��ǰʱ��
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				todayTomatoCount +=1;
				allTomatoCount+=1;
				editor.putInt("todayTomatoCount", todayTomatoCount);
				editor.putInt("allTomatoCount", allTomatoCount);
				editor.putString("date", dateNowString);
				// �ύ��ǰ����
				editor.commit();
				Log.v("MAIN", "-------todayTomatoCount---------"+todayTomatoCount+"-------");
				Log.v("MAIN", "-------allTomatoCount---------"+allTomatoCount+"-------");
				Log.v("MAIN", "-------dateNowString---------"+dateNowString+"-------");
				
				
				Toast.makeText(MainActivity.this,
						getResources().getString(R.string.EndTask),
						Toast.LENGTH_SHORT).show();
				textView.setText("�����Ϣ��");
				break;
			default:
				break;
			}
		}

		/**
		 * ��ʱ������ʾ
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			// TODO �Զ����ɵķ������
			String string = new SimpleDateFormat("mm:ss").format(new Date(
					millisUntilFinished));
			textView.setText(string);
			progressBar
					.setProgressNotInUiThread((int) (millisUntilFinished / 1000));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			// Log.i(TAG, "onResume Method is executed");
			Intent intent = new Intent(MainActivity.this, SettingFragment.class);
			item.setIntent(intent);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (flag == 2) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
						MainActivity.this);
				alertBuilder
						.setTitle("������")
						.setMessage("�Ƿ����������Ѳ��˳���")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO �Զ����ɵķ������
										MainActivity.this.finish();
									}
								})
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO �Զ����ɵķ������
										dialog.cancel();
									}
								}).create();
				alertBuilder.show();
			}
		} else {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "�ٰ�һ���˵�������",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v("MAIN", "-------MainActivity---------onRestart----Do Something----");
		SharedPreferences mySharedPreferences = getSharedPreferences("test",
				Activity.MODE_PRIVATE);
		tick = mySharedPreferences.getInt("tick", 25);
		rest = mySharedPreferences.getInt("rest", 5);
		longrest = mySharedPreferences.getInt("longrest", 15);
		progressBar.setMaxProgress(tick * 60);
		if (flag == 4) {
			flag = 1;
			if (flagHide) {
				textView.setVisibility(View.VISIBLE);
				flagHide = false;
				// ���Ӷ���Ч��
				int randow= new Random().nextInt(8);
				animation = AnimationUtils.loadAnimation(MainActivity.this, ID[randow]);
				textView.startAnimation(animation);
			}
			stateFlag();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id.circleProgressbar:
			stateFlag();

			if (flagHide) {
				textView.setVisibility(View.VISIBLE);
				flagHide = false;
				// ���Ӷ���Ч��
				int randow= new Random().nextInt(8);
				animation = AnimationUtils.loadAnimation(this,ID[randow]);
				textView.startAnimation(animation);
			} else {
				textView.setVisibility(View.INVISIBLE);
				flagHide = true;
				// Log.i(TAG, "--flagHide-->>"+flagHide);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * ����״̬��־�������1��ʼ���ѣ�ͬʱ��flagΪ2�������3����ʱ�䵽����ʼ��Ϣ��flag��Ϊ4
	 */
	private void stateFlag() {
		switch (flag) {
		case 1:
			timeSpan = tick * 60 * 1000;
			percentTime = tick * 60;
			// timeSpan = 10 * 1000;
			// percentTime = 10;
			time = new TimeCount(timeSpan, 1000);// ����CountDownTimer����
			Toast.makeText(MainActivity.this, tomatocharSequence,
					Toast.LENGTH_SHORT).show();
			flag = 2;// ���ѿ�ʼ��
			time.start();
			break;
		case 3://������ϢActivity
			Log.i(TAG, "--intent-->>" + rest);
			Intent intent = new Intent(getApplicationContext(),
					BreakActivity.class);
			intent.putExtra("rest", rest);
			flag = 4;
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("MAIN", "-------MainActivity---------onResume-------");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("MAIN", "-------MainActivity---------onStop-------");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("MAIN", "-------MainActivity---------onPause-------");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("MAIN", "-------MainActivity---------onDestroy-------");
	}
}
















