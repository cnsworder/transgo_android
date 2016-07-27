/**
 * @author 伊冲
 * @version 1.0
 * Apr 29, 2011
 */
package com.cnsworder.transgo.util;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.cnsworder.transgo.GlobalData;
import com.cnsworder.qrcodescanner.R;
// import cn.sword.fertility.data.BaseInfoPools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * @author 伊冲
 * @version 1.0 Apr 29, 2011
 */
public class PortData {

	final static String datafile = "/data/data/com.cnsworder.transgo/databases/transgo";
	static String desfile = "transgo_data.db";
	//final static String SD = "/sdcard/";
	static String path = GlobalData.xcmPath + "data/";

	Context context = null;

	public PortData(Context context) {
		this.context = context;
	}

	public void exportData() {

		final ProgressDialog progress = new ProgressDialog(context);
		progress.setTitle("数据导出");
		progress.setMessage("数据正在导入到SD卡，请稍候！");
		progress.setIcon(R.drawable.upload);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] buffer = new byte[1024];
				int length = 0;
				File dfile = new File(path + desfile);
				File sfile = new File(datafile);
				if (dfile.isFile()) {
					dfile.delete();
				}
				File pathFile = new File(path);
				if (!pathFile.exists()) {
					pathFile.mkdirs();
				}
				try {
					dfile.createNewFile();
					FileInputStream in = new FileInputStream(sfile);
					FileOutputStream out = new FileOutputStream(dfile);
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
					handler.sendEmptyMessage(0);
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				} finally {
					progress.dismiss();
				}
			}
		});
		progress.show();
		thread.start();
	}

	public void importData() {
		final ProgressDialog progress = new ProgressDialog(context);
		progress.setTitle("数据导入");
		progress.setMessage("数据正在导入数据库，请稍候！");
		progress.setIcon(R.drawable.download);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] buffer = new byte[1024];
				int length = 0;
				File dfile = new File(path + desfile);
				File sfile = new File(datafile);
				if (sfile.isFile()) {
					sfile.delete();
				}
				try {
					sfile.createNewFile();
					FileInputStream in = new FileInputStream(dfile);
					FileOutputStream out = new FileOutputStream(sfile);
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
					out.flush();
					//do {
						//BaseInfoPools.buildPools(context);
					//}while(BaseInfoPools.baseInfo == null);
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					handler.sendEmptyMessage(1);
				} finally {
					progress.dismiss();
				}
			}
		});
		progress.show();
		thread.start();
	}
	
	/**
	 * 通过http下载数据
	 * @param url
	 */
	protected void download(String url) {
		URL updateURL = null;
		HttpURLConnection connect = null;
		FileOutputStream out = null;
		BufferedInputStream reader = null;
		try {
			updateURL = new URL(url);
			connect = (HttpURLConnection) updateURL.openConnection();

			reader =  new BufferedInputStream(connect.getInputStream());
			File file = new File(Environment.getExternalStorageDirectory(),"fertility_data.db");
			out = new FileOutputStream(file);
			int readLenght = 0;
			int writecount = 0;
			int fileLength = connect.getContentLength();
			byte[] buf = new byte[100];
			while((readLenght = reader.read(buf)) != -1){
				out.write( buf, 0, readLenght);
				writecount += readLenght;
				if(fileLength <= writecount){
					out.close();
					reader.close();
					connect.disconnect();
					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		return;
	}
	
	/**
	 * 上传数据
	 * @throws Exception 
	 */
	protected void updata(String actionUrl, 
			Map<String, String> params,
			Map<String, File> files) throws Exception {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--" ;
		String LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
		sb.append(PREFIX);
		sb.append(BOUNDARY);
		sb.append(LINEND);
		sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
		sb.append("Content-Type: text/plain; charset=" + CHARSET+LINEND);
		sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
		sb.append(LINEND);
		sb.append(entry.getValue());
		sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if(files!=null)
		for (Map.Entry<String, File> file: files.entrySet()) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append(PREFIX);
		sb1.append(BOUNDARY);
		sb1.append(LINEND);
		sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""+file.getKey()+"\""+LINEND);
		sb1.append("Content-Type: application/octet-stream; charset="+CHARSET+LINEND);
		sb1.append(LINEND);
		outStream.write(sb1.toString().getBytes());

		InputStream is = new FileInputStream(file.getValue());
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
		outStream.write(buffer, 0, len);
		}

		is.close();
		outStream.write(LINEND.getBytes());
		}

		//请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		int res = conn.getResponseCode();
		if (res == 200) {
		InputStream in = conn.getInputStream();
		int ch;
		StringBuilder sb2 = new StringBuilder();
		while ((ch = in.read()) != -1) {
		sb2.append((char) ch);
		}
		}
		outStream.close();
		conn.disconnect();
		//return null; 
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				ToastShow.show(context, R.string.save_message_true);
			} else {
				ToastShow.show(context, R.string.save_message_false);
			}

			super.handleMessage(msg);
		}
	};

}
