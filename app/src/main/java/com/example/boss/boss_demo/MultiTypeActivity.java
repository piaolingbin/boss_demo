package com.example.boss.boss_demo;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.boss.boss_demo.Event.ModuleDownEvent;
import com.example.boss.boss_demo.Event.ModuleUpEvent;
import com.example.boss.boss_demo.Event.ShowSetButtonEvent;
import com.example.boss.boss_demo.fragments.BaseFragment;
import com.example.boss.boss_demo.fragments.FoodFragment;
import com.example.boss.boss_demo.fragments.Load0Fragment;
import com.example.boss.boss_demo.fragments.Load1Fragment;
import com.example.boss.boss_demo.fragments.Load2Fragment;
import com.example.boss.boss_demo.fragments.Load3Fragment;
import com.example.boss.boss_demo.fragments.Load4Fragment;
import com.example.boss.boss_demo.fragments.Load5Fragment;
import com.example.boss.boss_demo.fragments.Load6Fragment;
import com.example.boss.boss_demo.fragments.Load7Fragment;
import com.example.boss.boss_demo.fragments.PartFragment;
import com.example.boss.boss_demo.fragments.ShopFragment;
import com.example.boss.boss_demo.view.SimpleUpdateDialog;
import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.MultiTypeAdapter;
import com.shizhefei.view.multitype.MultiTypeView;
import com.shizhefei.view.multitype.provider.FragmentData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MultiTypeActivity extends AppCompatActivity {

    private static final String TAG = MultiTypeActivity.class.getSimpleName();

    private MultiTypeAdapter multiTypeAdapter;
    private List<Class> fragments = new ArrayList<>();
    public static boolean setMode = false;
    public static boolean showFoodModule = false;
    public static boolean showPartModule = false;
    public static boolean showShopModule = false;
    private Context mContext;
    private MultiTypeView container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mContext = this;
        EventBus.getDefault().register(this);
        fragments.add(Load0Fragment.class);
        fragments.add(Load1Fragment.class);
        fragments.add(Load2Fragment.class);
        fragments.add(Load3Fragment.class);
        fragments.add(Load4Fragment.class);
        fragments.add(Load5Fragment.class);
        fragments.add(Load6Fragment.class);
        fragments.add(Load7Fragment.class);
        Button refresh = (Button) findViewById(R.id.btn_refresh);
        Button set = (Button) findViewById(R.id.btn_set);

        container = (MultiTypeView) findViewById(R.id.fragment_container);
        ItemBinderFactory itemBinderFactory = new ItemBinderFactory(getSupportFragmentManager());
        container.setAdapter(multiTypeAdapter = new MultiTypeAdapter(loadData(0), itemBinderFactory));
        findViewById(R.id.btn_split).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleUpdateDialog dialog = new SimpleUpdateDialog(mContext, true);
                dialog.setDialogCallback(new SimpleUpdateDialog.Dialogcallback() {
                    @Override
                    public void dialogOk() {

                    }

                    @Override
                    public void install() {

                    }

                    @Override
                    public void dialogCancle() {
                        if (showFoodModule){
                            fragments.add(FoodFragment.class);
                        } else {
                            if (fragments.contains(FoodFragment.class)){
                                fragments.remove(FoodFragment.class);
                            }
                        }
                        if (showPartModule){
                            fragments.add(PartFragment.class);
                        } else {
                            if (fragments.contains(PartFragment.class)){
                                fragments.remove(PartFragment.class);
                            }
                        }
                        if (showShopModule){
                            fragments.add(ShopFragment.class);
                        } else {
                            if (fragments.contains(ShopFragment.class)){
                                fragments.remove(ShopFragment.class);
                            }
                        }
                        if (showFoodModule && showPartModule && showShopModule){
                            if (fragments.contains(Load1Fragment.class)){
                                fragments.remove(Load1Fragment.class);
                            }
                        } else {
                            if (!fragments.contains(Load1Fragment.class)){
                                fragments.add(1, Load1Fragment.class);
                            }
                        }
                        multiTypeAdapter.notifyDataChanged(loadData(0), true);
                        container.scrollToPosition(fragments.size() - 1);
                    }
                });
                dialog.show();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "refresh");
                List<Object> data = loadData(0);
                multiTypeAdapter.notifyDataChanged(data, true);
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode = !setMode;
                multiTypeAdapter.notifyDataChanged(loadData(0), true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private List<Object> loadData(int page) {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            FragmentData fragmentData = new FragmentData(fragments.get(i), fragments.get(i).getSimpleName());
            fragmentData.putInt("index", i);
            fragmentData.putString("1", fragments.get(i).getSimpleName());
            data.add(fragmentData);
        }
        return data;
    }

    @Subscribe
    public void onEvent(ModuleDownEvent event){
        Log.e(TAG, "ModuleDownEvent");
        int index = event.index;
        if (index == fragments.size() - 1)return;
        Collections.swap(fragments, index, index + 1);
        //fragments.add(index + 1, fragments.remove(index));
        multiTypeAdapter.notifyDataChanged(loadData(0), true);
        container.scrollToPosition(index + 1);
    }
    @Subscribe
    public void onEvent(ModuleUpEvent event){
        Log.e(TAG, "ModuleUpEvent");
        int index = event.index;
        if (index == 0)return;
        Collections.swap(fragments, index - 1, index);
        //fragments.add(index - 1, fragments.remove(index));
        multiTypeAdapter.notifyDataChanged(loadData(0), true);
        container.scrollToPosition(index - 1);
    }

    private int[] colors = {Color.parseColor("#F44336"), Color.parseColor("#E91E63"), Color.parseColor("#3F51B5"),
            Color.parseColor("#2196F3"), Color.parseColor("#4CAF50"), Color.parseColor("#CDDC39"),
            Color.parseColor("#FFEB3B"), Color.parseColor("#FF9800"), Color.parseColor("#FF5722"),
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
