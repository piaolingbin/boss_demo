package com.example.boss.boss_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.example.boss.boss_demo.utils.StringUtils;
import com.example.boss.boss_demo.view.ArcChartView.Arc;
import com.example.boss.boss_demo.view.ArcChartView.ArcChartView;

import java.util.ArrayList;
import java.util.List;

public class ArcChartActivity extends Activity {

    ArcChartView circlePercentBar;

    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_chart);
        circlePercentBar= (ArcChartView) findViewById(R.id.arc_chart_view);
        List<Arc> data = new ArrayList<>();
        int color;
        for (int i = 0; i < 4; i++){
            Arc arc = new Arc();
            arc.setValue(12.5f * (4 - i));
            if (i == 0){
                color = R.color.chart_line_color;
            } else if (i == 1){
                color = R.color.base_red;
            } else if (i == 2){
                color = R.color.colorEnd;
            } else {
                color = R.color.colorStart;
            }
            arc.setColor(color);
            data.add(arc);
        }
        circlePercentBar.setData(data, new DecelerateInterpolator());

        startBtn= (Button) findViewById(R.id.start_btn);
       /* startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circlePercentBar.setData((float) (100*Math.random()),new DecelerateInterpolator());
            }
        });*/
    }
}
