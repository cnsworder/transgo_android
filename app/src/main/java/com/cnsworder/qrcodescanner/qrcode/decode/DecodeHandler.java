/*
 * Copyright (C) 2010 ZXing authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.cnsworder.qrcodescanner.qrcode.decode;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.maxicode.MaxiCodeReader;
import com.google.zxing.qrcode.QRCodeReader;
import com.cnsworder.qrcodescanner.R;
import com.cnsworder.qrcodescanner.qrcode.QrCodeActivity;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

//import javax.crypto.spec.DESedeKeySpec;

final class DecodeHandler extends Handler {

    private final QrCodeActivity mActivity;
    //private final QRCodeReader mQrCodeReader;
    private final MultiFormatReader mReader;
    private final Map<DecodeHintType, Object> mHints;
    private byte[] mRotatedData;

    DecodeHandler(QrCodeActivity activity) {
        this.mActivity = activity;
        //mQrCodeReader = new QRCodeReader();
        mReader = new MultiFormatReader();
        mHints = new Hashtable<>();
        mHints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        mHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();

        decodeFormats.add(BarcodeFormat.CODE_39);
        decodeFormats.add(BarcodeFormat.CODE_93);
        decodeFormats.add(BarcodeFormat.CODE_128);
        decodeFormats.add(BarcodeFormat.ITF);
        // decodeFormats.add(BarcodeFormat.QR_CODE);
        decodeFormats.add(BarcodeFormat.AZTEC);
        decodeFormats.add(BarcodeFormat.CODABAR);
        decodeFormats.add(BarcodeFormat.DATA_MATRIX);
        decodeFormats.add(BarcodeFormat.EAN_8);
        decodeFormats.add(BarcodeFormat.EAN_13);
        decodeFormats.add(BarcodeFormat.MAXICODE);
        decodeFormats.add(BarcodeFormat.PDF_417);
        decodeFormats.add(BarcodeFormat.RSS_14);
        decodeFormats.add(BarcodeFormat.RSS_EXPANDED);
        decodeFormats.add(BarcodeFormat.UPC_A);
        decodeFormats.add(BarcodeFormat.UPC_E);
        decodeFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
        mHints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case R.id.decode:
                decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case R.id.quit:
                Looper looper = Looper.myLooper();
                if (null != looper) {
                    looper.quit();
                }
                break;
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency, reuse the same reader
     * objects from one decode to the next.
     *
     * @param data The YUV preview frame.
     * @param width The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        if (null == mRotatedData) {
            mRotatedData = new byte[width * height];
        } else {
            if (mRotatedData.length < width * height) {
                mRotatedData = new byte[width * height];
            }
        }
        Arrays.fill(mRotatedData, (byte) 0);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x + y * width >= data.length) {
                    break;
                }
                mRotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;

        Result rawResult = null;
        try {
            PlanarYUVLuminanceSource source =
                new PlanarYUVLuminanceSource(mRotatedData, width, height, 0, 0, width, height, false);
            //LuminanceSource source = new  LuminanceSource(width, height);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            rawResult = mReader.decode(bitmap1, mHints);
        } catch (ReaderException e) {
        } finally {
            mReader.reset();
        }

        if (rawResult != null) {
            Message message = Message.obtain(mActivity.getCaptureActivityHandler(), R.id.decode_succeeded, rawResult);
            message.sendToTarget();
        } else {
            Message message = Message.obtain(mActivity.getCaptureActivityHandler(), R.id.decode_failed);
            message.sendToTarget();
        }
    }
}
