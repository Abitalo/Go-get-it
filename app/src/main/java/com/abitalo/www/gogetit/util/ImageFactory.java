package com.abitalo.www.gogetit.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lancelot on 2016/3/16.
 */
public final class ImageFactory {
    public static Bitmap getBitmap(String url){
        URL imageUrl  = null;
        Bitmap bitmap = null;

        try{
            imageUrl=new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e("ImageFactory","url illegal");
        }
        try {
            HttpURLConnection conn;
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setDoInput(true);
            InputStream is = conn.getInputStream();
            bitmap= BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            Log.e("ImageFactory","open connection error");
        }
        return bitmap;
    }
}
