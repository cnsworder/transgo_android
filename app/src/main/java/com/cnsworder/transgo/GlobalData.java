package com.cnsworder.transgo;

import android.content.Context;

/**
 * Created by crossorbit on 16/7/10.
 */
public class GlobalData {
    public static String xcmPath = "/sdcard/transgo/photo/";
    public static String imageName = null;
    public static String basePath = android.os.Environment.getExternalStorageDirectory().getPath();
    public static String imagePath = basePath + "/transgo";
    public static String webServer = "http://139.129.203.236";
    public static String changeStatus = webServer + "/change_status";
    public static String postOrder = webServer + "/post_order";
    public static String pushPhoto = webServer + "/upphto";
}
