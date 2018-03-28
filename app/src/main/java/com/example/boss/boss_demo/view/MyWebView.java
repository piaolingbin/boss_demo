package com.example.boss.boss_demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class MyWebView extends WebView {
    Context mContext;

    public MyWebView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);// 支持js脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setSaveFormData(true);// 是否保存表单数据
        webSettings.setSavePassword(false);// 是否保存密码
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);//支持js插件

        webSettings.setSupportZoom(true);// 是否支持缩放
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setSupportMultipleWindows(false);// 支持多窗口显示
        webSettings.setBuiltInZoomControls(false);//设置内置的缩放控件。若为false，则该WebView不可缩放

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 提高渲染的优先级
        webSettings.setBlockNetworkImage(false);// 把图片加载放在最后来加载渲染
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);//媒体自动播放
        }
        webSettings.setGeolocationEnabled(true);//启用地理定位

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setDomStorageEnabled(true);  // 开启 DOM storage API 功能
        webSettings.setAppCacheMaxSize(1024*1024*8); // 设置缓存大小
        try{
            String appCachePath = mContext.getCacheDir().getAbsolutePath();
            webSettings.setAppCachePath(appCachePath);   // 设置缓存路径
            webSettings.setAllowFileAccess(true);   //设置可以访问文件
            webSettings.setAppCacheEnabled(true);// 开启Application Cache存储机制
        } catch(Exception e){
            e.printStackTrace();
        }
        // 解决https请求的web中加载不出http链接图片的问题
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        setLongClickable(true);
        setScrollbarFadingEnabled(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

    }
    @Override
    public void loadUrl(String url) {
        if (url == null || "".equals(url)) {
            return;
        }
        super.loadUrl(url);
    }

}
