package com.cnsworder.transgo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsworder.qrcodescanner.R;

/***************************************
 * 签收
 */
public class CheckinActivity extends AppCompatActivity {

    ImageView photo = null;
    BitmapFactory.Options option = new BitmapFactory.Options();
    boolean photostatus = false;
    private Button checkinButton;
    private Button cameraStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView orderId = (TextView) findViewById(R.id.order_id);
        Intent intent = getIntent();
        final String order_id = intent.getStringExtra("order");
        orderId.setText(order_id);

        photo = (ImageView) findViewById(R.id.photo);
        cameraStart = (Button) findViewById(R.id.camera);
        checkinButton = (Button) findViewById(R.id.check_button);

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
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            if (GlobalData.imageName != null) {
                String path = GlobalData.imagePath + GlobalData.imageName;
                photo.setImageBitmap(BitmapFactory.decodeFile(path, option));
                //info.photo = path;
                photostatus = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
