package com.example.boss.boss_demo.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boss.boss_demo.Event.ModuleDownEvent;
import com.example.boss.boss_demo.Event.ModuleUpEvent;
import com.example.boss.boss_demo.MultiTypeActivity;
import com.example.boss.boss_demo.R;
import com.viewpagerindicator.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class Load1Fragment extends BaseFragment {

    private static final String TAG = Load1Fragment.class.getSimpleName();
    private String i;
    private Button up;
    private Button down;
    private int index;
    private List<Fragment> fragments = new ArrayList<>();

    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[] { "Food", "Part", "Shop"};
    private ViewPager pager;
    private RadioGroup rgBusiness;

    private RadioButton rbBusiness;
    private RadioButton rbMember;
    private RadioButton rbTakeaway;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("dddd", "onCreateView :" + getClass().getSimpleName() + "  " + savedInstanceState);
        index = getArguments().getInt("index");
        i = getArguments().getString("1");
        //int color = getArguments().getInt("color");
        View view = inflater.inflate(R.layout.test1, container, false);
        up = view.findViewById(R.id.btn_up);
        down = view.findViewById(R.id.btn_down);
        up.setOnClickListener(onClickListener);
        down.setOnClickListener(onClickListener);
        rgBusiness = view.findViewById(R.id.rg_business);
        rbBusiness = view.findViewById(R.id.rb_business);
        rbMember = view.findViewById(R.id.rb_member);
        rbTakeaway = view.findViewById(R.id.rb_takeaway);
        rbBusiness.setChecked(true);

        FoodFragment foodFragment = new FoodFragment();
        PartFragment partFragment = new PartFragment();
        ShopFragment shopFragment = new ShopFragment();

        view.findViewById(R.id.ll_set).setVisibility(MultiTypeActivity.setMode ? View.VISIBLE : View.GONE);

        Button set = view.findViewById(R.id.btn_in_set);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (!MultiTypeActivity.showFoodModule){
            fragments.add(foodFragment);
        } else {
            rbBusiness.setVisibility(View.GONE);
        }

        if (!MultiTypeActivity.showPartModule){
            fragments.add(partFragment);
        } else {
            rbMember.setVisibility(View.GONE);
        }

        if (!MultiTypeActivity.showShopModule){
            fragments.add(shopFragment);
        } else {
            rbTakeaway.setVisibility(View.GONE);
        }

        //ViewPager的adapter
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getChildFragmentManager());
        pager = view.findViewById(R.id.viewPager);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(onPageChangeListener);

        rgBusiness.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_business:
                        if (pager.getCurrentItem() != 0) {
                            pager.setCurrentItem(0);
                        }
                        break;
                    case R.id.rb_member:
                        if (pager.getCurrentItem() != 1){
                            pager.setCurrentItem(1);
                        }
                        break;
                    case R.id.rb_takeaway:
                        if (pager.getCurrentItem() != 2){
                            pager.setCurrentItem(2);
                        }
                        break;
                }
            }
        });

       /* //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = view.findViewById(R.id.tab_indicator);
        indicator.setViewPager(pager);

        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });*/
        return view;
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                rbBusiness.setChecked(true);
            } else if (position == 1) {
                rbMember.setChecked(true);
            } else if (position == 2) {
                rbTakeaway.setChecked(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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

    /**
     * ViewPager适配器
     * @author len
     *
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            Fragment fragment = fragments.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
