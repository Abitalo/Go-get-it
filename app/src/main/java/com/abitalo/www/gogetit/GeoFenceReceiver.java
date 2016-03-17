package com.abitalo.www.gogetit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

/**
 * Created by Lancelot on 2016/3/16.
 */
public class GeoFenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        Set<String> keySet=bundle.keySet();
        StringBuffer sb=new StringBuffer();
        for(String key : keySet)
            sb.append(key+"\n");
        int status = intent.getIntExtra("status",-1);
        if(status == 0) {
            Log.e("gogetit","在外面");
            sb.append("出来了！\n");
        }
        else if (status == 1){
            Log.e("gogetit", "在外面");
            sb.append("进来了！\n");
        }
        else
        {
            Log.e("gogetit","fuckit");
        }
        HomeFragment.debugInfo.setText(sb.toString());
    }
}
