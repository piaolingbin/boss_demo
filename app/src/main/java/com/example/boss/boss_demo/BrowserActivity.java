package com.example.boss.boss_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boss.boss_demo.view.MyWebView;

import java.util.HashMap;
import java.util.Set;

public class BrowserActivity extends Activity {
    private static final String TAG = BrowserActivity.class.getSimpleName();
    private WebView mWebView;
    private static final int RESULT_CODE = 110;
    private static final int REQUEST_CODE = 12306;
    private Boolean from2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        from2 = intent.getBooleanExtra("from2", false);
        setContentView(from2 ? R.layout.activity_browser2: R.layout.activity_browser);
        if (from2){
            init2();
        } else {
            init1();
        }
    }

    private void init2() {
        mWebView = findViewById(R.id.webView);

        mWebView.setVerticalScrollbarOverlay(true);
        //设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);

        String url = "file:///android_asset/js_17_android_webview.html";
        mWebView.loadUrl(url);

        //在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
        //添加客户端支持
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJs(String name) {
            Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
        }
    }

    //在java中调用js代码
    public void sendInfoToJs(View view) {
        String msg = ((EditText) findViewById(R.id.input_et)).getText().toString();
        Log.e(TAG, "sendInfoToJs:" + msg);
        //调用js中的函数：showInfoFromJava(msg)
        mWebView.loadUrl("javascript:showInfoFromJava('" + msg + "')");
    }

    private void init1() {
        Button button = findViewById(R.id.btn_callJs);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LineChartActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mWebView = findViewById(R.id.webview);
        // 先加载JS代码
        // 格式规定为:file:///android_asset/文件名.html

        // You can set a default handler in Java, so that js can send message to Java without assigned handlerName
        //mWebView.setDefaultHandler(new DefaultHandler());
        mWebView.loadUrl("file:///android_asset/javascript.html");
        //registerHandler();
        mWebView.setWebChromeClient(new WebChromeClient() {
                // 拦截输入框(原理同方式2)
                // 参数message:代表promt（）的内容（不是url）
                // 参数result:代表输入框的返回值
                @Override
                public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                    // 根据协议的参数，判断是否是所需要的url(原理同方式2)
                    // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                    //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                    Uri uri = Uri.parse(message);
                    // 如果url的协议 = 预先约定的 js 协议
                    // 就解析往下解析参数
                    if (uri.getScheme().equals("js")) {
                        // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                        // 所以拦截url,下面JS开始调用Android需要的方法
                        if (uri.getAuthority().equals("webview")) {
                            //
                            // 执行JS所需要调用的逻辑
                            Log.e("BrowserActivity", "js调用了Android的方法");
                            // 可以在协议上带有参数并传递到Android上
                            //HashMap<String, String> params = new HashMap<>();
                            //Set<String> collection = uri.getQueryParameterNames();

                            //参数result:代表消息框的返回值(输入值)
                            //result.confirm("js调用了Android的方法成功啦");
                            Intent intent = new Intent(getBaseContext(), LineChartActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        return true;
                    }
                    return super.onJsPrompt(view, url, message, defaultValue, result);
                }

                // 通过alert()和confirm()拦截的原理相同，此处不作过多讲述

                // 拦截JS的警告框
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    return super.onJsAlert(view, url, message, result);
                }

                // 拦截JS的确认框
                @Override
                public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                    return super.onJsConfirm(view, url, message, result);
                }
            }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "resultCode:" + resultCode + "-- > requestCode:" + REQUEST_CODE);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE:
                    Log.e(TAG, "callJs");
                    if (from2){
                        onResult2();
                    } else {
                        onResult();
                    }
                    //mWebView.loadUrl("javascript:callJs('" + TAG + "')");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onResult2() {
        String msg = ((EditText) findViewById(R.id.input_et)).getText().toString();
        //调用js中的函数：showInfoFromJava(msg)
        mWebView.loadUrl("javascript:showInfoFromJava('" + msg + "')");
    }

    private void onResult() {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version <= 18) {
            mWebView.loadUrl("javascript:callJs('" + TAG + "')");
        } else {
            mWebView.evaluateJavascript("javascript:callJs('" + TAG + "')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                }
            });
        }
    }

    /*private void registerHandler() {
        // 注册js可以调用的Java函数
        mWebView.registerHandler("getUserInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(getBaseContext(), data, Toast.LENGTH_LONG).show();
                function.onCallBack(TAG);
            }
        });
    }*/
}
