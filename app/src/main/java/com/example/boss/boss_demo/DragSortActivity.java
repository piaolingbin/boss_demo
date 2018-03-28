package com.example.boss.boss_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.example.boss.boss_demo.adapter.DragAdapter;
import com.example.boss.boss_demo.view.DragSortGridView;

import java.util.ArrayList;
import java.util.List;

public class DragSortActivity extends AppCompatActivity {

    private List<String> title = new ArrayList<>();
    private boolean mNeedShake = false;
    private static final float DEGREE_0 = 2.5f;
    private static final int ANIMATION_DURATION = 90;
    float mDensity;
    private DragSortGridView dragSortGridView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm != null) {
            mDensity = dm.density;
        }
        title.add("营业");
        title.add("会员");
        title.add("外卖");
        setContentView(R.layout.activity_drag_sort);

        dragSortGridView = (DragSortGridView) findViewById(R.id.dragSortGridView);
        mAdapter = new MyAdapter();
        dragSortGridView.setAdapter(mAdapter);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.startShakeAnimation();
            }
        });
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.stopShakeAnimation();
            }
        });
    }

    private class MyAdapter extends DragAdapter{

        @Override
        public void onDataModelMove(int from, int to) {
            String s = title.remove(from);
            title.add(to, s);
        }

        @Override
        public int getCount() {
            return title.size();
        }

        @Override
        public String getItem(int position) {
            return title.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                //FrameLayout frameLayout = new FrameLayout(DragSortActivity.this);
                //convertView = frameLayout;
                convertView = LayoutInflater.from(DragSortActivity.this).inflate(R.layout.drag_grid_item_layout, null);
            }
            textView = convertView.findViewById(R.id.textView);
            textView.setText(getItem(position));
            shakeAnimation(textView);
            return convertView;
        }

        public void startShakeAnimation(){
            mNeedShake = true;
            notifyDataSetChanged();
        }

        public void stopShakeAnimation(){
            mNeedShake = false;
        }

    }

    private void shakeAnimation(final View v) {
        float rotate = 0;
        /*int c = mCount++ % 5;
        if (c == 0) {
            rotate = DEGREE_0;
        } else if (c == 1) {
            rotate = DEGREE_1;
        } else if (c == 2) {
            rotate = DEGREE_2;
        } else if (c == 3) {
            rotate = DEGREE_3;
        } else {
            rotate = DEGREE_4;
        }*/
        v.measure(0 , 0);
        final RotateAnimation mra = new RotateAnimation(DEGREE_0, -DEGREE_0, v.getMeasuredWidth() / 2, v.getMeasuredHeight() / 2);
        final RotateAnimation mrb = new RotateAnimation(-DEGREE_0, DEGREE_0, v.getMeasuredWidth() / 2, v.getMeasuredHeight() / 2);
        mra.setDuration(ANIMATION_DURATION);
        mrb.setDuration(ANIMATION_DURATION);
        mra.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (mNeedShake) {
                    mra.reset();
                    v.startAnimation(mrb);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        mrb.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (mNeedShake) {
                    mrb.reset();
                    v.startAnimation(mra);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        v.startAnimation(mra);
    }
    @Override
    public void onBackPressed() {
        if (!mNeedShake) {
            super.onBackPressed();
        } else {
            mNeedShake = false;
        }
    }
}
