package com.vch.stockapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private TextView bondTextView;
    private View progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bondTextView = findViewById(R.id.bond_text);
        progressBarView = findViewById(R.id.progress_layout);
        getBondMsg();
    }

    private void getBondMsg() {
        progressBarView.setVisibility(View.VISIBLE);
        BondManager.getBondMsgFromOkHttp(new IBondListener() {
            @Override
            public void onBondMsgSuccess(final List<BondModel> bondList) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarView.setVisibility(View.GONE);
                        if (bondList == null) {
                            return;
                        }
                        if (bondList.size() == 0) {
                            bondTextView.setText("今日无申购指标");
                            return;
                        }
                        StringBuilder bondMsgBuilder = new StringBuilder();
                        for (BondModel bondModel : bondList) {
                            String bondMsg = "债券简称：" + bondModel.getSname() + " 申购代码：" + bondModel.getCorrescode();
                            bondMsgBuilder.append(bondMsg).append("\n");
                            BondManager.sendBondMsg(MainActivity.this,
                                    bondMsg, bondModel.getCorrescode(), Integer.parseInt(bondModel.getCorrescode()));
                        }
                        bondTextView.setText(bondMsgBuilder.toString());
                    }
                });
            }

            @Override
            public void onBondMsgFailed(final String errMsg) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarView.setVisibility(View.GONE);
                        bondTextView.setText("出错了.出错信息为：" + errMsg);
                    }
                });
            }
        });
    }


}
