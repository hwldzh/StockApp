package com.vch.stockapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机启动监听
 *
 * @author VC_H
 * @date 2019-10-25
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent myIntent = new Intent(context, MainActivity.class);
            myIntent.putExtra("from", "Receiver");
            myIntent.setAction("android.intent.action.MAIN");
            myIntent.addCategory("android.intent.category.LAUNCHER");
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }
}
