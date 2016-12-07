package cn.mstar.store.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.CommentListPagerAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.ProductCommentIndicatorView;
import cn.mstar.store.fragments.CommentListFragment;
import cn.mstar.store.interfaces.OnCommentNumListener;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by wcl at Shenzhen.
 */
public class CommentListActivity extends FragmentActivity implements ViewPager.OnPageChangeListener
        , View.OnClickListener, OnCommentNumListener {

    private static int SCREENWIDTH;
    private ImageView back;
    private TextView title;
    private ViewPager mViewPager;
    private CommentListPagerAdapter adapter;
    private List<CommentListFragment> fragments;

    private ProductCommentIndicatorView slideTap;
    private LinearLayout tab0, tab1, tab2, tab3, tab4;
    private TextView txt0, txt1, txt2, txt3, txt4;
    private LinearLayout[] tabs;

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        setContentView(R.layout.product_comment_list);
        SCREENWIDTH = getScreenWidth();
        String proId = getIntent().getStringExtra("proId");
        link = NewLink.COMMENT_LSIT + "&goods_id=" + proId;
        initView();

    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_name);
        back = (ImageView) findViewById(R.id.title_back);
        title.setText(getString(R.string.product_comment_list));
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化ViewPager
        mViewPager = (ViewPager) findViewById(R.id.product_commment_viewpager);
        mViewPager.addOnPageChangeListener(this);
        //初始化Fragment
        fragments = new ArrayList<>();
        int[] score = {0, 5, 3, 1, 7};
        CommentListFragment fragment;
        String url;
        for (int i = 0; i < 5; i++) {
            url = link + "&scores=" + score[i];
            refreshNum(i, url);
            fragment = new CommentListFragment(url, i);
            fragments.add(fragment);
        }

        //初始化FragmentPagerAdapter
        adapter = new CommentListPagerAdapter(getSupportFragmentManager(), fragments);

        //添加适配器
        mViewPager.setAdapter(adapter);

        slideTap = (ProductCommentIndicatorView) findViewById(R.id.slide_tap);
        initTabs();
    }

    private void initTabs() {
        tab0 = (LinearLayout) findViewById(R.id.tab0);
        tab1 = (LinearLayout) findViewById(R.id.tab1);
        tab2 = (LinearLayout) findViewById(R.id.tab2);
        tab3 = (LinearLayout) findViewById(R.id.tab3);
        tab4 = (LinearLayout) findViewById(R.id.tab4);
        tabs = new LinearLayout[]{tab0, tab1, tab2, tab3, tab4};
        tab0.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);

        txt0 = (TextView) findViewById(R.id.txt0);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        slideTap.setX(SCREENWIDTH / 5 * (position + positionOffset));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPageSelected(int position) {
        slideTap.setX(SCREENWIDTH / 5 * position);

        setTxtColor(tabs[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab0:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.tab1:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.tab2:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.tab3:
                mViewPager.setCurrentItem(3, false);
                break;
            case R.id.tab4:
                mViewPager.setCurrentItem(4, false);
                break;
        }
    }

    private void setTxtColor(LinearLayout tab) {
        TextView txt1, txt2;
        for (LinearLayout t : tabs) {
            txt1 = (TextView) t.getChildAt(0);
            txt2 = (TextView) t.getChildAt(1);
            if (t == tab) {
                txt1.setTextColor(getResources().getColor(R.color.color_gold_nor));
                txt2.setTextColor(getResources().getColor(R.color.color_gold_nor));
            } else {
                txt1.setTextColor(getResources().getColor(R.color.color_grey_st));
                txt2.setTextColor(getResources().getColor(R.color.color_grey_st));
            }
        }
    }

    private void refreshNum(final int index, String url) {
        VolleyRequest.GetCookieRequest(this, url, new VolleyRequest.HttpStringRequsetCallBack() {

            @Override
            public void onSuccess(String result) {
                try {
                    Gson gson = new Gson();
                    int num = gson.fromJson(result, JsonObject.class).getAsJsonObject("data").get("evaluationCount").getAsInt();
                    setNum(index, num);
                } catch (Exception e) {
                    setNum(index, 0);
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(CommentListActivity.this, "网络出现异常，请检查网络再试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setNum(int index, int num) {
        switch (index) {
            case 0:
                txt0.setText(num + "");
                break;
            case 1:
                txt1.setText(num + "");
                break;
            case 2:
                txt2.setText(num + "");
                break;
            case 3:
                txt3.setText(num + "");
                break;
            case 4:
                txt4.setText(num + "");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.requestQueue.cancelAll(this);
    }
}
