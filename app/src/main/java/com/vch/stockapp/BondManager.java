package com.vch.stockapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.NotificationCompat;
import cn.leancloud.AVCloud;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 可转债管理类
 * 后台配置在leancloud的云函数中。云函数存储在github中，地址为
 * https://github.com/hwldzh/python-getting-started.git
 * @author VC_H
 * @date 2019-10-26
 */
public class BondManager {
    public static final String TAG = "BondManager";

    public static void getBondMsg(final IBondListener listener) {
        // 构建传递给服务端的参数字典
        Map<String, Object> dicParameters = new HashMap<String, Object>();

        // 调用指定名称的云函数 averageStars，并且传递参数
        AVCloud.callFunctionInBackground("test", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {}

            @Override
            public void onNext(Object object) {
                // succeed.
                Log.d(TAG, "get bond msg success");
                if (object instanceof String) {
                    String data = (String) object;
                    final String regex = "(\\{\".*?\\})";
                    final Pattern pattern = Pattern.compile(regex);
                    final Matcher matcher = pattern.matcher(data);

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    List<BondModel> bondList = new ArrayList<>();
                    while (matcher.find()) {
                        String groupData = matcher.group(0);
                        if (groupData == null) {
                            continue;
                        }
                        try {
                            JSONObject groupDataObj = new JSONObject(groupData);
                            //债券代码
                            String bondcode = groupDataObj.optString("BONDCODE");
                            //债券简称
                            String sname = groupDataObj.optString("SNAME");
                            //申购日期
                            String startdate = groupDataObj.optString("STARTDATE");
                            //申购代码
                            String correscode = groupDataObj.optString("CORRESCODE");
                            //正股代码
                            String swapscode = groupDataObj.optString("SWAPSCODE");
                            //正股简称
                            String securityshortname = groupDataObj.optString("SECURITYSHORTNAME");
                            String[] dateArray = startdate.split("-");
                            if (dateArray.length != 3) {
                                Log.e(TAG, "startdate parse error：" + startdate);
                                continue;
                            }
                            String yearStr = dateArray[0];
                            String monthStr = dateArray[1];
                            String dayStr = dateArray[2].substring(0, 2);
                            startdate = yearStr + "-" + monthStr + "-" + dayStr;

                            if (Integer.parseInt(yearStr) == year && Integer.parseInt(monthStr) == month
                                    && Integer.parseInt(dayStr) == day) {
                                BondModel bondModel = new BondModel(bondcode, sname, startdate, correscode
                                        , swapscode, securityshortname);
                                bondList.add(bondModel);
                                Log.d(TAG, "bingo day：" + startdate);
                                Log.d(TAG, "债券代码：" + bondcode + "; 债券简称："
                                        + sname + "; 申购日期：" + startdate + "; 申购代码：" + correscode
                                        + "; 正股代码："+swapscode + "; 正股简称：" + securityshortname);

                            } else { //由于可转债时间的顺序是按时间逆序的，因此一旦找不到则后面也再找不到了
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (listener != null) {
                        listener.onBondMsgSuccess(bondList);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
                Log.d(TAG, "get bond msg failed");
                throwable.printStackTrace();
                if (listener != null) {
                    listener.onBondMsgFailed(throwable.getMessage());
                }
            }

            @Override
            public void onComplete() {}
        });
    }

    public static void sendBondMsg(Context context, String content, String channelId, int notificationId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "可转债消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(context, channelId, channelName, importance);
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("申购提醒")
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .setOngoing(true)
                .build();
        if (manager == null) {
            return;
        }
        manager.notify(notificationId, notification);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        notificationManager.createNotificationChannel(channel);
    }
}
