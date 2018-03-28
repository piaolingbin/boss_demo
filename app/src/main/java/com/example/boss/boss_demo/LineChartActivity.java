package com.example.boss.boss_demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import com.example.boss.boss_demo.view.CurveChartColuli;
import com.example.boss.boss_demo.view.CurveChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartActivity extends Activity {
    private static final String TAG = LineChartActivity.class.getSimpleName();
    private CurveChartColuli line_curvechart;
    //这里的x和y轴自己可以添加100000条，只要你想要多少就可以。去试试吧。
    ArrayList<Float> xValues = new ArrayList<>();
    ArrayList<Float> yValues = new ArrayList<>();
    private List<Integer> arryList;
    private float MaxColilu;
    private float density;
    private int densityDPI;
    private int screenWidth;
    private int screenHeight;
    private static CurveChartColuli.CurveChartBuilder chartBuilder;
    private CurveChartView curveChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        setResult(RESULT_OK);
        for (int i = 0; i < 25; i++){
            xValues.add((float) i);
        }
        /*for (int i = 0; i < 25; i++){
            yValues.add((float) (Math.random() * 100));
        }*/
        yValues.add(0f);
        yValues.add(100f);
        yValues.add(140f);
        yValues.add(160f);
        yValues.add(200f);
        yValues.add(250f);
        yValues.add(250f);
        yValues.add(250f);
        yValues.add(250f);
        yValues.add(250f);
        yValues.add(320f);
        yValues.add(320f);
        yValues.add(320f);
        yValues.add(320f);
        yValues.add(450f);
        yValues.add(500f);
        yValues.add(510f);
        yValues.add(520f);
        yValues.add(550f);
        yValues.add(600f);
        yValues.add(700f);
        yValues.add(700f);
        yValues.add(700f);
        yValues.add(700f);
        yValues.add(700f);
        //初始化时候你们不要buidle.show()。没封装好调用会异常，你们有时间自己可以好好优化一下。
        line_curvechart = (CurveChartColuli) findViewById(R.id.line_curvechart_colu);
        chartBuilder = CurveChartColuli.CurveChartBuilder.createBuilder(line_curvechart);
        chartBuilder.setFillDownColor(Color.parseColor("#8c8b8b"));
        chartBuilder.setXYValues(xValues, yValues);
        //这个方法里面为了屏幕适配没别的。你们可以不用管
        setDimager();
        initView();
    }

    private void setDimager() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
        screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
        line_curvechart.mHeight = (int) (screenHeight * 0.31);
        line_curvechart.mWidth = screenWidth;
        line_curvechart.setFillDownLineColor(true);
    }

    private void initView() {
        //chartBuilder.setXYValues(xValues, yValues);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
