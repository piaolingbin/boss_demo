package com.example.boss.boss_demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.boss.boss_demo.fragments.FoodFragment;
import com.example.boss.boss_demo.fragments.PartFragment;
import com.example.boss.boss_demo.fragments.ShopFragment;
import com.example.boss.boss_demo.fragments.TotalFragment;
import com.example.boss.boss_demo.view.HomeModuleView;
import com.shizhefei.view.multitype.MultiTypeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentsActivity extends AppCompatActivity {

    private static final String TAG = FragmentsActivity.class.getSimpleName();
    @BindView(R.id.fragment1)
    HomeModuleView fragment1;
    @BindView(R.id.fragment2)
    HomeModuleView fragment2;
    @BindView(R.id.fragment3)
    HomeModuleView fragment3;
    @BindView(R.id.fragment4)
    HomeModuleView fragment4;
    private int[] viewPosition = {0, 1, 2, 3}; // 用于记录模块的位置
    private boolean isAnimEnd = true;
    private int fragmentCount = 4;

    private FragmentManager fragmentManager;
    private List<HomeModuleView> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        fragmentManager = getSupportFragmentManager();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        TotalFragment totalFragment = new TotalFragment();
        FoodFragment foodFragment = new FoodFragment();
        ShopFragment shopFragment = new ShopFragment();
        PartFragment partFragment = new PartFragment();

        // 模块的索引，唯一，固定。用于更新模块的位置信息
        fragment1.setTag(0);
        fragment2.setTag(1);
        fragment3.setTag(2);
        fragment4.setTag(3);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container1, totalFragment);
        fragmentTransaction.add(R.id.fragment_container2, foodFragment);
        fragmentTransaction.add(R.id.fragment_container3, shopFragment);
        fragmentTransaction.add(R.id.fragment_container4, partFragment);
        fragmentTransaction.commitAllowingStateLoss();

        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        registerListener();
    }

    private void registerListener() {
        for (HomeModuleView view : fragments) {
            view.setChildOnClickListener(childOnClickListener);
        }
    }

    HomeModuleView.ChildOnclickListener childOnClickListener = new HomeModuleView.ChildOnclickListener() {
        @Override
        public void onDownClick(View view) {
            downView((int) view.getTag());
        }

        @Override
        public void onUpClick(View view) {
            upView((int) view.getTag());
        }
    };

    private void upView(int index) {
        if (isAnimEnd) {
            int position = viewPosition[index];
            // 第一个不能上移
            if (position > 0) {
                upExchangeView(position, index);
                //upUpdatePosition(0);
            }
        }
    }

    private void downView(int index) {
        if (isAnimEnd) {
            int position = viewPosition[index];
            // 最后一个不能下移
            if (position < fragmentCount - 1) {
                downExchangeView(position, index);
                //downUpdatePosition(0);
            }
        }
    }

    /**
     * 向上移动被选择的View
     *
     * @param position
     * @param index
     */
    private void upExchangeView(int position, int index) {
        View thisView = fragments.get(position);
        View upView = fragments.get(position - 1);
        ObjectAnimator thisViewAnim = ObjectAnimator.ofFloat(thisView, "TranslationY"
                , thisView.getTranslationY() - getViewHeight(upView)).setDuration(1000);
        thisViewAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimEnd = true;
            }
        });
        isAnimEnd = false;
        thisViewAnim.start();
        ObjectAnimator.ofFloat(upView, "TranslationY"
                , upView.getTranslationY() + getViewHeight(thisView)).setDuration(1000).start();
        fragments.add(position - 1, fragments.remove(position));
        --viewPosition[index];
        ++viewPosition[(int) fragments.get(position).getTag()];
    }

    /**
     * 向下移动View
     *
     * @param position
     * @param curIndex
     */
    private void downExchangeView(int position, int curIndex) {
        View thisView = fragments.get(position);
        View downView = fragments.get(position + 1);
        ObjectAnimator thisViewAnim = ObjectAnimator.ofFloat(thisView, "TranslationY"
                , thisView.getTranslationY() + getViewHeight(downView)).setDuration(1000);
        thisViewAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimEnd = true;
            }
        });
        isAnimEnd = false;
        thisViewAnim.start();
        ObjectAnimator.ofFloat(downView, "TranslationY"
                , downView.getTranslationY() - getViewHeight(thisView)).setDuration(1000).start();
        fragments.add(position + 1, fragments.remove(position));
        ++viewPosition[curIndex];
        --viewPosition[(int) fragments.get(position).getTag()];
    }

    /**
     * 获取View的高度
     *
     * @param view
     * @return
     */
    public int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    // View宽，高
    public int[] getLocation(View v) {
        int[] loc = new int[4];
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        loc[0] = location[0];
        loc[1] = location[1];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);

        loc[2] = v.getMeasuredWidth();
        loc[3] = v.getMeasuredHeight();

        //base = computeWH();
        return loc;
    }
}
