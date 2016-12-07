package cn.mstar.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.SelectCouponAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.SelectCouponEntity;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class SelectCouponActivity extends BaseActivity implements SelectCouponAdapter.OnItemClickCallBackListener {
    private View loading, noResult, networkErr, reload;
    private LinearLayout select_coupon_content;
    private ImageView back;
    private TextView title;
    private ListView select_coupon_list;
    private RadioGroup select_coupon_rg;
    private String link;
    private List<SelectCouponEntity> voucheru, voucherno;
    private SelectCouponAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_coupon_wcl);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        link = NewLink.SELECT_COUPON + "&key=" + Utils.getTokenKey((MyApplication) getApplication()) + "&shopId=" + getIntent().getStringExtra("shopId")
                + "&amount=" + getIntent().getStringExtra("totalPrice") + "&userAccount=" + getIntent().getStringExtra("userAccount");
        initView();
        setLoading();
        inflateDatas();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.title_back);
        title = (TextView) findViewById(R.id.title_name);
        select_coupon_rg = (RadioGroup) findViewById(R.id.select_coupon_rg);
        select_coupon_list = (ListView) findViewById(R.id.select_coupon_list);
        title.setText(getResources().getString(R.string.select_coupon));
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        select_coupon_rg.check(R.id.select_coupon_rb_u);
        select_coupon_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_coupon_rb_u:
                        adapter.changeData(voucheru, true);
                        break;
                    case R.id.select_coupon_rb_n:
                        adapter.changeData(voucherno, false);
                        break;
                }
            }
        });

        loading = findViewById(R.id.lny_loading_layout);
        noResult = findViewById(R.id.lny_no_result);
        networkErr = findViewById(R.id.lny_network_error_view);
        reload = networkErr.findViewById(R.id.wifi_retry);
        select_coupon_content = (LinearLayout) findViewById(R.id.select_coupon_content);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading();
                inflateDatas();
            }
        });
    }

    private void inflateDatas() {
        VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SelectCouponEntity[]>() {}.getType();
                    JsonArray item = gson.fromJson(result, JsonObject.class).getAsJsonObject("data").get("voucheru").getAsJsonArray();
                    SelectCouponEntity[] ru = gson.fromJson(item, type);
                    item = gson.fromJson(result, JsonObject.class).getAsJsonObject("data").get("voucherno").getAsJsonArray();
                    SelectCouponEntity[] no = gson.fromJson(item, type);
                    voucheru = new ArrayList<SelectCouponEntity>();
                    voucherno = new ArrayList<SelectCouponEntity>();
                    for (SelectCouponEntity entity : ru) {
                        voucheru.add(entity);
                    }
                    for (SelectCouponEntity entity : no) {
                        voucherno.add(entity);
                    }
                    if (voucherno.size() == 0 && voucheru.size() == 0) {
                        setNoResult();
                    } else {
                        setAdapter(voucheru);
                    }
                } catch (Exception e) {
                    setNoResult();
                }
            }

            @Override
            public void onFail(String error) {
                setNetworkErr();
            }
        });
    }

    private void setAdapter(List<SelectCouponEntity> data) {
        adapter = new SelectCouponAdapter(this, data);
        select_coupon_list.setAdapter(adapter);
        adapter.setOnItemClickCallBackListener(this);
        setResult();
    }

    @Override
    public void onClickCallBack(SelectCouponEntity entity) {
        Intent intent = new Intent();
        intent.putExtra("data", (Serializable) entity);
        setResult(3, intent);
        finish();
    }

    private void hideAllView() {
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        select_coupon_content.setVisibility(View.GONE);
    }

    private void setResult() {
        hideAllView();
        select_coupon_content.setVisibility(View.VISIBLE);
    }

    private void setLoading() {
        hideAllView();
        loading.setVisibility(View.VISIBLE);
    }

    private void setNoResult() {
        hideAllView();
        noResult.setVisibility(View.VISIBLE);
    }

    private void setNetworkErr() {
        hideAllView();
        networkErr.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.requestQueue.cancelAll(this);
    }
}
