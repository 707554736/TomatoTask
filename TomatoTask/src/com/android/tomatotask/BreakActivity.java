package com.android.tomatotask;

import java.text.SimpleDateFormat;
import java.util.Date;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BreakActivity extends Activity implements OnClickListener {
	private TextView breakTextView;
	private ProgressBar breakProgressBar;
	private Button returnbButton;
	private int rest=0;
	private TimeCount time;
	private int timeSpan;
	private long exitTime = 0;

	private boolean showShake = true;// ��
	private String showRing; // ����
	private Vibrator vibrator;
	MediaPlayer player;

	public BreakActivity() {
		// TODO �Զ����ɵĹ��캯�����
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.break_layout);
		Log.v("MAIN", "-------BreakActivity---------onCreate-------");
		breakProgressBar = (ProgressBar)findViewById(R.id.breakBar);
		breakTextView = (TextView)findViewById(R.id.breakTxtView);
		returnbButton = (Button)findViewById(R.id.ReturnButton);
		//�޸�����
		Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		breakTextView.setTypeface(fontFace);
//		returnbButton.setTypeface(fontFace);
		Intent intent = getIntent();
		rest = intent.getIntExtra("rest", 5);
		showShake=intent.getBooleanExtra("showShake", true);
		showRing=intent.getStringExtra("showRing");
		Log.v("MAIN", showRing+"----------------showRing----");
		timeSpan = rest * 60 * 1000;
		breakProgressBar.setMax(rest*60);
		returnbButton.setOnClickListener(this);
		time = new TimeCount(timeSpan, 1000);// ����CountDownTimer����
		time.start();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (player != null) {
			if (player.isPlaying()) {
				player.stop();
			}
		}
		Log.v("MAIN", "-------BreakActivity---------onPause-------");
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}
		/**
		 * ��ʱ������ʾ
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			// TODO �Զ����ɵķ������
			int nowprogress;
			String string = new SimpleDateFormat("mm:ss").format(new Date(
					millisUntilFinished));
			breakTextView.setText(string);
			nowprogress = (int)(rest*60-millisUntilFinished/1000);
			breakProgressBar.setProgress(nowprogress);
		}
		/**
		 * ��ʱ���ʱ����
		 */
		@Override
		public void onFinish() {
			// TODO �Զ����ɵķ������
			if (showShake) {
				//������
				vibrator =(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
				long [] pattern = {200,500,200,500,1200,500,200,500};   // ֹͣ ���� ֹͣ ����  
				vibrator.vibrate(pattern,-1);           //�ظ����������pattern ���ֻ����һ�Σ�index��Ϊ-1   
			}
			if (showRing != "") {
				Log.v("MAIN", "�ҽ������ҽ�����");
				//��������
				player = new MediaPlayer();
				Uri uri = Uri.parse(showRing);
				
				try {
					player.setDataSource(BreakActivity.this, uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
				final AudioManager audioManager = (AudioManager) BreakActivity.this
						.getSystemService(Context.AUDIO_SERVICE);
				if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
					player.setAudioStreamType(AudioManager.STREAM_RING);
					Log.v("MAIN", AudioManager.STREAM_RING +"");
					player.setLooping(false);
					try {
						player.prepare();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					player.start();
				}
			}
			returnbButton.setVisibility(View.VISIBLE);
		}

		
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		finish();//�رյ�ǰactivity ������һ��activity��activity��������activity
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
					BreakActivity.this);
			alertBuilder
					.setTitle("���ѣ�")
					.setMessage("������Ϣ������һ�����ѣ�")
					.setPositiveButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO �Զ����ɵķ������
									time.cancel();
									BreakActivity.this.finish();
								}
							})
					.setNegativeButton("��Ϣ",
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
	    return super.onKeyDown(keyCode, event);
	}

	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v("MAIN", "-------BreakActivity---------onRestart-------");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("MAIN", "-------BreakActivity---------onResume-------");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("MAIN", "-------BreakActivity---------onStop-------");
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("MAIN", "-------BreakActivity---------onDestroy-------");
	}
}
