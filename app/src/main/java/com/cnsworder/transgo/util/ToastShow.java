/**
 * @author 伊冲
 * @version 1.0
 * Aug 19, 2010
 */
package com.cnsworder.transgo.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author 伊冲
 * @version 1.0 Aug 19, 2010
 */
public class ToastShow {

	public static void show(Context context, int mes) {
		Toast toast = Toast.makeText(context, mes, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 5);
		toast.show();
	}

}
