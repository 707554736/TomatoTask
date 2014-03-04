package com.android.tomatotask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.android.tomatotask.Task.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskFragment extends Fragment {
	private View mMainView;
	
	public String EXPANDED = "EXPANDED";
	public NotepadAdapter adapter;
	public ArrayList<Map<String, Object>> itemList;
	public ListView listView;
	public int number;
	public Button numberButton;
	public Button topButton;
	
	RelativeLayout layout ;
    TextView textView ;
    EditText editText ;
    Date getDate ;
    String date ;

	public TaskFragment() {
		// TODO �Զ����ɵĹ��캯�����
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
//		mMainView = inflater.inflate(R.layout.task_layout, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
		mMainView = inflater.inflate(R.layout.task_layout_main, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);
		
		this.numberButton = ((Button) mMainView.findViewById(R.id.number));
		this.topButton = ((Button) mMainView.findViewById(R.id.topButton));
		this.listView = ((ListView) mMainView.findViewById(R.id.listview));
		//�޸�����
		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
		numberButton.setTypeface(fontFace);
		topButton.setTypeface(fontFace);
		
		this.listView.setDivider(null);
		this.listView.setOnItemClickListener(new ItemClick());
		this.topButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
//				Intent intent = new Intent(getActivity(),
//						WriteActivity.class);
//				startActivity(intent);
				 layout = (RelativeLayout)(getActivity().getLayoutInflater().inflate(R.layout.writedown, null));
			     textView = ((TextView)layout.findViewById(R.id.writedate));
			     editText = ((DrawLine)layout.findViewById(R.id.edittext));
//					//�޸�����
//					Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
//					textView.setTypeface(fontFace);
//					editText.setTypeface(fontFace);
//			    this.cancelButton = ((Button)findViewById(R.id.button));
//			    this.sureButton = ((Button)findViewById(R.id.button2));
			     getDate = new Date();
			     date = getDate.getDate();
			    textView.setText(date);
				new AlertDialog.Builder(getActivity()).setView(layout).setPositiveButton("����", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �Զ����ɵķ������
						SQLiteDatabase localSqLiteDatabase = new SqliteHelper(getActivity(), null, null, 0).getWritableDatabase();
						Notepad localNotepad = new Notepad();
						ChangeSqlite localChangeSqlite = new ChangeSqlite();
						String strContent = editText.getText().toString();
						if (strContent.equals("")) {
							Toast.makeText(getActivity(), "����Ϊ��", Toast.LENGTH_SHORT).show();
						}
						String strTitle=strContent.length()>11?" "+strContent.substring(0, 11):strContent;
						localNotepad.setContent(strContent);
						localNotepad.setTitle(strTitle);
						localNotepad.setdata(date);
						localChangeSqlite.add(localSqLiteDatabase, localNotepad);
						showUpdate();
					}
				}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �Զ����ɵķ������
						
					}
				}).show();

			}
		});
		
		Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^onCreate^^^^^^^^");
	}
	
	class ItemClick implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			// TODO �Զ����ɵķ������
			System.out.println("item----------click");
			Map<String, Object> localMap = itemList.get(paramInt);
			if (((Boolean) localMap.get("EXPANDED")).booleanValue()) {
				localMap.put("EXPANDED", Boolean.valueOf(false));
			} else {
				localMap.put("EXPANDED", Boolean.valueOf(true));
			}
			adapter.notifyDataSetChanged();
		}

	}
	
	public void showUpdate() {
		// TODO �Զ����ɵķ������
		this.itemList = new ArrayList<Map<String, Object>>();
		SQLiteDatabase localSqLiteDatabase = new SqliteHelper(getActivity(), null, null,
				0).getReadableDatabase();
		Iterator<Notepad> localIterator = new ChangeSqlite().query(
				localSqLiteDatabase).iterator();
		while (true) {
			if (!localIterator.hasNext()) {
				Collections.reverse(this.itemList);
				this.adapter = new NotepadAdapter(getActivity(),this, this.itemList);
				this.listView.setAdapter(this.adapter);
				if (this.itemList.size()==0) {
					number=0;
					this.numberButton.setText("");
				}
				return;
			}
			Notepad localNotepad = (Notepad) localIterator.next();
			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
			localHashMap.put("titleItem", localNotepad.getTitle());
			localHashMap.put("dateItem", localNotepad.getdata());
			localHashMap.put("contentItem", localNotepad.getContent());
			localHashMap.put("idItem", localNotepad.getid());
			// Ĭ�ϱʼ���̯�������۵���trueΪ̯��
			localHashMap.put("EXPANDED", Boolean.valueOf(true));
			this.itemList.add(localHashMap);
			this.number = this.itemList.size();
			System.out.println("number----------number=" + number);
			this.numberButton.setText("(" + this.number + ")");
		}
	}
	
	
	
	
	
	@Override
	public void onResume() {
		// TODO �Զ����ɵķ������
		super.onResume();
		showUpdate();
		Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^onResume^^^^^^^^");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^onCreateView^^^^^^^^");
		ViewGroup p = (ViewGroup) mMainView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
			Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^removeAllViewsInLayout!!!!!!!!");
		}
		return mMainView;
	}
	
	
	
	
	
	
	
	@Override
	public void onDestroy() {
		// TODO �Զ����ɵķ������
		super.onDestroy();
		Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^onDestroy Method^^^^^^^^");
	}
	
	@Override
	public void onPause() {
		// TODO �Զ����ɵķ������
		super.onPause();
		Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^onPause^^^^^^^^");
	}
	

	
	@Override
	public void onStart() {
		// TODO �Զ����ɵķ������
		super.onStart();
		Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^onStart^^^^^^^^");
	}
	
	@Override
	public void onStop() {
		// TODO �Զ����ɵķ������
		super.onStop();
		Log.i("MAIN", "^^^^^^^^TaskFragment^^^^^^^^onStop^^^^^^^^");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
