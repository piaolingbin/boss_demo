package com.example.boss.boss_demo.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/12/7.
 */

public class StringUtils {
    public static int getIntLength(float num){
        return String.valueOf((int) num).length();
    }

    public static int dp2px(Context context, float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
