package com.cnsworder.transgo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.cnsworder.qrcodescanner.R;
import com.cnsworder.transgo.GlobalData;
import com.cnsworder.transgo.util.ToastShow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

/**
 * 拍照
 * 
 * @author 伊冲
 * @version 1.0 2010-5-26
 */
public class CameraView extends Activity implements SurfaceHolder.Callback {

	Camera camera;
	SurfaceView cameraView;
	boolean mPreviewRunning = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.camera);
		super.onCreate(savedInstanceState);
		if (!android.os.Environment.getExternalStorageState().equals("mounted")) {
			ToastShow.show(this, R.string.sdcard_camera);
			try {
				this.finish();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return;
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		cameraView = (SurfaceView) findViewById(R.id.camera_view);
		cameraView.getHolder().addCallback(this);
		cameraView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!mPreviewRunning) {
					return ;
				}
				cameraOn();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 1, R.string.save);
		menu.add(1, 2, 2, R.string.cancel);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			setResult(0);
			this.finish();
			break;
		case 2:
			if (GlobalData.imageName == null) {
				return true;
			}
			File file = new File(GlobalData.imagePath + GlobalData.imageName);
			file.delete();
			GlobalData.imageName = null;
			camera.startPreview();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(0);
			this.finish();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return super.onKeyDown(keyCode, event);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if (!mPreviewRunning) {
				return super.onKeyDown(keyCode, event);
			}

			return cameraOn();
		}

		return super.onKeyDown(keyCode, event);
	}
	
	public boolean cameraOn() {
		if (GlobalData.imageName != null) {
			File file = new File(GlobalData.imagePath
					+ GlobalData.imageName);
			file.delete();
		}

		Camera.PictureCallback mpicture = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (bitmap == null) {
					return;
				}
				try {
					Date date = new Date();
					File imagePath = new File(GlobalData.imagePath);
					String savePath = GlobalData.imagePath;

					if (!imagePath.exists()) {
						boolean result = imagePath.mkdirs();
						if (!result) {
							Log.e("Error", "mkdir");
							savePath = GlobalData.basePath;
							GlobalData.imagePath = GlobalData.basePath;
						}
					}
					GlobalData.imageName = "/" + date.getTime() + ".tgo";
					String imageFile = savePath + GlobalData.imageName;
					new File(imageFile).createNewFile();
					FileOutputStream out = new FileOutputStream(imageFile);
					out.write(data);
					out.flush();
					out.close();
					//data.clone();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bitmap.recycle();
			}
		};

		camera.takePicture(null, null, mpicture);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder
	 * , int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mPreviewRunning) {

			camera.stopPreview();
		}

		try {

			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		camera.startPreview();
		mPreviewRunning = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder
	 * )
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.
	 * SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
		mPreviewRunning = false;
	}
}
