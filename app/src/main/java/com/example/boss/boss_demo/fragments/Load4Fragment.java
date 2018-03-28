package com.example.boss.boss_demo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.boss.boss_demo.Event.ModuleDownEvent;
import com.example.boss.boss_demo.Event.ModuleUpEvent;
import com.example.boss.boss_demo.MultiTypeActivity;
import com.example.boss.boss_demo.R;

import org.greenrobot.eventbus.EventBus;


public class Load4Fragment extends BaseFragment {

    private static final String TAG = Load4Fragment.class.getSimpleName();
    private View progressBar;
    private String i;
    private TextView textView;
    private Button up;
    private Button down;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        index = getArguments().getInt("index");
        i = getArguments().getString("1");
        //int color = getArguments().getInt("color");
        View view = inflater.inflate(R.layout.test, container, false);
        up = view.findViewById(R.id.btn_up);
        down = view.findViewById(R.id.btn_down);
        up.setOnClickListener(onClickListener);
        down.setOnClickListener(onClickListener);
        //view.setBackgroundColor(color);
        view.findViewById(R.id.ll_set).setVisibility(MultiTypeActivity.setMode ? View.VISIBLE : View.GONE);
        progressBar = view.findViewById(R.id.progressBar);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(i);
        textView.append("  3秒后加载完毕");
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(i);
                textView.append("  2秒后加载完毕");
            }
        }, 1000);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(i);
                textView.append("  1秒后加载完毕");
            }
        }, 2000);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                textView.setText(i);
                textView.append("  加载完毕");
            }
        }, 3000);
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_down:
                    Log.e(TAG, "Click Down");
                    ModuleDownEvent downEvent = new ModuleDownEvent();
                    downEvent.index = index;
                    EventBus.getDefault().post(downEvent);
                    break;

                case R.id.btn_up:
                    Log.e(TAG, "Click Up");
                    ModuleUpEvent upEvent = new ModuleUpEvent();
                    upEvent.index = index;
                    EventBus.getDefault().post(upEvent);
                    break;

            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
