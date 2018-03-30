package com.example.boss.boss_demo;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;
import com.igexin.sdk.PushManager;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private static final String APATCH_PATH = "/patch_signed_7zip.apk";

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), PushIntentService.class);
        login = (Button) findViewById(R.id.btn_login);
        findViewById(R.id.btn_registe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MultiTypeActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FragmentsActivity.class));
            }
        });

        findViewById(R.id.btn_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DragSortActivity.class));
            }
        });

        findViewById(R.id.btn_pie_chart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PieChartActivity.class));
            }
        });

        findViewById(R.id.btn_line_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LineChartActivity.class));
            }
        });

        findViewById(R.id.btn_arc_chart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ArcChartActivity.class));
            }
        });

        findViewById(R.id.btn_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BrowserActivity.class));
            }
        });

        findViewById(R.id.btn_webview2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
                intent.putExtra("from2", true);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_tinker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patchFileString = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + APATCH_PATH;
                TinkerInstaller.onReceiveUpgradePatch(getApplication(), patchFileString);
                TextView tvTinker = (TextView) findViewById(R.id.tv_tinker);
                tvTinker.setText("哈哈，修复成功了！");
                EditText etName = (EditText) findViewById(R.id.et_name);
                etName.setText("哦哟，又成功了！");
                File file = new File(patchFileString);
                if (file.exists()) {
                    Log.i(TAG, "补丁包存在>>>>" + patchFileString);
                } else {
                    Log.i(TAG, "补丁包不存在");
                }
            }
        });

        findViewById(R.id.btn_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BannerActivity.class));
            }
        });
        NewbieGuide.with(this)//activity or fragment
                .setLabel("guide2")//Set guide layer labeling to distinguish different guide layers, must be passed! Otherwise throw an error
                .addHighLight(login, HighLight.Type.RECTANGLE)//Add the view that needs to be highlighted
                .setLayoutRes(R.layout.fragment_food, R.id.textView)//Custom guide layer layout, do not add background color, the boot layer background color is set by setBackgroundColor()
                .setEveryWhereCancelable(false)
                .alwaysShow(false)
                .show();
    }
}
