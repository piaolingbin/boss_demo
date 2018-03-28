package com.example.boss.boss_demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.boss.boss_demo.view.MyPieChartView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.renderer.MyPieChartRenderer;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartActivity extends AppCompatActivity {

    private MyPieChartView chart;
    private PieChartData data;

    private boolean hasLabels = true;
    private boolean hasLabelsOutside = true;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        chart = (MyPieChartView) findViewById(R.id.chart);
        //chart.setOnValueTouchListener(new ValueTouchListener());
        generateData();
        setResult(RESULT_OK);
    }

    private void generateData() {
        int numValues = 10;
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue(
                    (float) Math.random() * 1000, ChartUtils.pickColor());
            values.add(sliceValue);
            sliceValue.setLabel("糖醋排骨  40%");
            sliceValue.setLabel2("销量 123456");
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);
        data.setValueLabelBackgroundAuto(false);
        data.setValueLabelsTextColor(Color.parseColor("#000000"));
        data.setValueLabelBackgroundEnabled(false);
        data.setValueLabelTextSize(8);
        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("销量");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library
            // uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(
                    getResources().getDisplayMetrics().scaledDensity, 40));
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,16));
        }

        chart.setPieChartData(data);
        chart.setInteractive(true);
        chart.setCircleFillRatio(0.5f);
        chart.setChartRotationEnabled(true);
        chart.setChartRotation(360000, true);
    }

    private void explodeChart() {
        isExploded = !isExploded;
        generateData();

    }

    private void toggleLabelsOutside() {
        // has labels have to be true:P
        hasLabelsOutside = !hasLabelsOutside;
        if (hasLabelsOutside) {
            hasLabels = true;
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        if (hasLabelsOutside) {
            chart.setCircleFillRatio(0.7f);
        } else {
            chart.setCircleFillRatio(1.0f);
        }

        generateData();

    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        generateData();
    }

    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
            hasLabelsOutside = false;

            if (hasLabelsOutside) {
                chart.setCircleFillRatio(0.7f);
            } else {
                chart.setCircleFillRatio(1.0f);
            }
        }

        generateData();
    }

    /**
     * To animate values you have to change targets values and then call
     * {@link Chart#startDataAnimation()} method(don't confuse with
     * View.animate()).
     */
    private void prepareDataAnimation() {
        for (SliceValue value : data.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(PieChartActivity.this, "Selected: " + value,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
