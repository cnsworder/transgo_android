package com.cnsworder.transgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.entity.FileEntity;
import com.cnsworder.qrcodescanner.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***************************************
 * 签收
 */
public class CheckinActivity extends AppCompatActivity {

    ImageView photo = null;
    BitmapFactory.Options option = new BitmapFactory.Options();
    boolean photostatus = false;
    private Button checkinButton;
    private Button cameraStart;
    private String order_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView orderId = (TextView) findViewById(R.id.order_id);
        Intent intent = getIntent();
        order_id = intent.getStringExtra("order");
        orderId.setText(order_id);

        photo = (ImageView) findViewById(R.id.photo);
        cameraStart = (Button) findViewById(R.id.camera);
        checkinButton = (Button) findViewById(R.id.return_ok);

        cameraStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CheckinActivity.this,
                        CameraView.class), 1);
            }
        });

        checkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("order", order_id);
                setResult(RESULT_OK, intent);
                new Thread(runnable).start();
            }
        });

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("xx","请求结果:" + val);

            CheckinActivity.this.finish();
        }
    };

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            boolean resultFlag = false;
            HttpGet request = new HttpGet(GlobalData.changeStatus + "/" + order_id + "/Finish");
            try {
                HttpClient client =  new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String resultEntity = EntityUtils.toString(response.getEntity());
                    JSONObject json = new JSONObject(resultEntity);
                    String result = json.get("result").toString();
                    if (result.equals("true")) {
                        resultFlag = true;
                    }
                }
                HttpPost post = new HttpPost(GlobalData.pushPhoto);
                String imageFile = GlobalData.imagePath + GlobalData.imageName;
                File imageHandler = new File(imageFile);
                FileEntity multiEntiy = new FileEntity(imageHandler, "binary/octet-stream");
                post.setEntity(multiEntiy);
                response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    resultFlag = true;
                    imageHandler.delete();
                } else {
                    resultFlag = false;
                }
            } catch (Exception e) {
                Log.d("HTTP", e.getMessage().toString());
            }
            if (resultFlag) {
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", order_id);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            if (GlobalData.imageName != null) {
                String path = GlobalData.imagePath + GlobalData.imageName;
                File file = new File(path);
                if (file.exists()) {
                    photo.setImageBitmap(BitmapFactory.decodeFile(path, option));
                    //info.photo = path;
                    photostatus = true;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
