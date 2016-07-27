/**
 * @author 伊冲
 * @version 1.0
 * May 14, 2011
 */
package com.cnsworder.transgo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * @author 伊冲
 * 照片查看
 * @version 1.0
 * May 14, 2011
 */
public class PhotoView extends Activity {
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView view = new ImageView(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(view);
		String photo = getIntent().getStringExtra("PHOTO");
		if(photo == null || photo.equals("")) {
			this.finish();
			return;
		}
		view.setImageBitmap(BitmapFactory.decodeFile(photo));
	}

}
