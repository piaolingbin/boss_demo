package com.example.boss.boss_demo.view.ArcChartView;

/**
 * Created by Administrator on 2018/1/18.
 */


public class Arc {
    private int arcColor;
    private int arcWidth;
    private int arcRadius;
    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return arcColor;
    }

    public void setColor(int color) {
        this.arcColor = color;
    }

    public int getWidth() {
        return arcWidth;
    }

    public void setWidth(int width) {
        this.arcWidth = width;
    }

    public int getRadius() {
        return arcRadius;
    }

    public void setRadius(int radius) {
        this.arcRadius = radius;
    }
}
