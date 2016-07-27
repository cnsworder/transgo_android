package com.cnsworder.transgo;

import java.util.ArrayList;
import java.util.HashMap;

import com.cnsworder.qrcodescanner.R;
//import cn.sword.fertility.data.BaseInfoPools;
import com.cnsworder.qrcodescanner.qrcode.OrderCheckActivity;
import com.cnsworder.qrcodescanner.qrcode.OrderExtCheckActivity;
import com.cnsworder.qrcodescanner.qrcode.OrderPostActivity;
import com.cnsworder.qrcodescanner.qrcode.QrCodeActivity;
import com.cnsworder.transgo.util.MessageBox;
import com.cnsworder.transgo.util.PortData;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class TransgoMainActivity extends Activity {
	MessageBox messageBox;
	PortData portData;
	public static TransgoMainActivity mainActivity;
	int length = 5;
	int[] menuImg = {
			R.drawable.check,
			R.drawable.checkin,
			R.drawable.open_box,
			R.drawable.post,
			//R.drawable.setup,
			R.drawable.about
			};
	@SuppressWarnings("rawtypes")
	Class[] classes = {
			OrderPostActivity.class,
			OrderCheckActivity.class,
			OrderExtCheckActivity.class,
		    QrCodeActivity.class,
			//Setup.class;
			About.class
			};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mainActivity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainframe);
		initalize();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			messageBox.exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initalize() {
		portData = new PortData(this);
		
		ImageView footimg = (ImageView) findViewById(R.id.footimg);
		footimg.setAlpha(40);
		GridView mainView = (GridView) findViewById(R.id.main_frame);
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> menu;
		String[] menuText = getResources().getStringArray(R.array.menuarray);
		for (int item = 0; item < length; item++) {
			menu = new HashMap<String, Object>();
			menu.put("image", menuImg[item]);
			menu.put("text", menuText[item]);
			data.add(menu);
		}
		String[] from = { "image", "text" };
		int[] to = { R.id.menu_image, R.id.menu_text };
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.menu,
				from, to);
		mainView.setAdapter(adapter);
		messageBox = new MessageBox();
		messageBox.init(this);
		//BaseInfoPools.buildPools(this);
		mainView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (position == 5 || position == 6) {
					Builder abuilder = new Builder(TransgoMainActivity.this);
					if(position == 5){
						abuilder.setMessage(R.string.down_warning);
					}else{
						abuilder.setMessage(R.string.up_warning);
					}
					
					abuilder.setIcon(R.drawable.warning)
					.setTitle(R.string.warning)
					.setPositiveButton(R.string.OK, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							final Builder builder = new Builder(TransgoMainActivity.this);
							final Spinner spinner = new Spinner(TransgoMainActivity.this);
							String[] item = {"SD卡", "网络"};
							spinner.setAdapter(new ArrayAdapter<String>(TransgoMainActivity.this,
									android.R.layout.simple_spinner_item,
									item));
							builder.setView(spinner);
							builder.setTitle("选择服务介质");
							builder.setMessage("服务类型:");
							builder.setPositiveButton("确定", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if(spinner.getSelectedItemPosition() == 1) {
										Toast.makeText(TransgoMainActivity.this, "服务暂不支持", Toast.LENGTH_LONG).show();
										return;
									}
									if(position == 5) {
										portData.importData();
										//BaseInfoPools.buildPools(TransgoMainActivity.this);
										return;
									}
									if(position == 6) {
										portData.exportData();
										return;
									}
								}
							});
							builder.setNeutralButton("取消", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									return;
								}
							});
							builder.setIcon(R.drawable.sync);
							builder.show();
							return;
						}
					}).setNegativeButton(R.string.cancel, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					}).show();
					return;
				}
				startActivity(new Intent(TransgoMainActivity.this, classes[position]));

			}
		});
	}
}