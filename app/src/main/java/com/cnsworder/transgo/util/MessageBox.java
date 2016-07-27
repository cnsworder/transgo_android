/**
 * @author 伊冲
 * @version 1.0
 * Jul 15, 2010
 */
package com.cnsworder.transgo.util;

import com.cnsworder.qrcodescanner.R;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;

/**
 * @author 伊冲
 * @version 1.0 Jul 15, 2010
 */
public class MessageBox {
 Builder builder = null;
 Activity activity = null;
 public String dataName = "data.db";
 Dialog dialog;

	public  void init(Activity activity) {
		this.activity = activity;
		builder = new Builder(activity);
	}

	public  void exit() {
		builder.setMessage(R.string.message_exit)
				.setIcon(R.drawable.exit)
				.setTitle(R.string.message)
				.setPositiveButton(R.string.OK,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								activity.finish();
								return;
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	public  void save(final IProxy proxy) {
		builder.setMessage(R.string.message_do)
				.setIcon(R.drawable.setup)
				.setTitle(R.string.message)
				.setPositiveButton(R.string.OK,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								proxy.execut();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}
	
	public  void saveas(final IProxy proxy) {
		builder.setMessage(R.string.saveas_message)
				.setIcon(R.drawable.setup)
				.setTitle(R.string.message)
				.setPositiveButton(R.string.OK,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								proxy.execut();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	public  void warning(int strid) {
		builder.setMessage(strid).setIcon(R.drawable.warning)
				.setTitle(R.string.warning)
				.setPositiveButton(R.string.OK, null).show();
	}
}
