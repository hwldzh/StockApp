package com.vch.stockapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 拉取网页内容的Service
 *
 * @author VC_H
 * @date 2019-10-25
 */
public class BackgroundService extends Service {
    public static final String TAG = "BackgroundService";

    @Override
    public void onCreate() {
        super.onCreate();
        BondManager.getBondMsg(new IBondListener() {
            @Override
            public void onBondMsgSuccess(List<BondModel> bondList) {
                if (bondList == null) {
                    return;
                }
                for (BondModel bondModel : bondList) {
                    String bondMsg = "债券简称：" + bondModel.getSname() + " 申购代码：" + bondModel.getCorrescode();
                    BondManager.sendBondMsg(BackgroundService.this,
                            bondMsg, bondModel.getCorrescode(), Integer.parseInt(bondModel.getCorrescode()));
                }
            }

            @Override
            public void onBondMsgFailed(String errMsg) {

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
