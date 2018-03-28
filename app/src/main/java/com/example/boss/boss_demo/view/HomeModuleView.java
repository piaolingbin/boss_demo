package com.example.boss.boss_demo.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.boss.boss_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/17.
 */

public class HomeModuleView extends LinearLayout {

    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_up)
    Button btnUp;
    private Context mContext;
    private LayoutInflater mInflater;
    private ChildOnclickListener childOnClickListener;

    public HomeModuleView(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.home_module_layout, this, true);
        ButterKnife.bind(this);
    }

    public HomeModuleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.home_module_layout, this, true);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_down, R.id.btn_up})
    public void onClickView(View view) {
        switch (view.getId()){
            case R.id.btn_down:
                childOnClickListener.onDownClick(this);
                break;
            case R.id.btn_up:
                childOnClickListener.onUpClick(this);
                break;
        }
    }

    public void setChildOnClickListener(ChildOnclickListener childOnClickListener){
        this.childOnClickListener = childOnClickListener;
    }

   public interface ChildOnclickListener{
        void onDownClick(View view);
        void onUpClick(View view);
    }

}
