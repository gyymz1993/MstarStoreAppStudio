package cn.mstar.store.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.CouponAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.CouponEntity;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by wcl_ld at Shenzhen.
 */
public class CouponActivity extends BaseActivity {
    private View loading, noResult, networkErr, reload;
    private TextView coupon_title;
    private ImageView coupon_back;
    private RadioGroup coupon_rg;
    private ListView coupon_list;
    private CouponAdapter adapter;
    private ArrayList<CouponEntity> data;
    private int[] radio = {R.id.coupon_btn1, R.id.coupon_btn2, R.id.coupon_btn3, R.id.coupon_btn4};
    private String link;
    private String link2;
    private boolean isFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_layout);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        link = NewLink.MY_COUPON + "&key=" + Utils.getTokenKey((MyApplication) getApplication());
        link2 = link + "&voucherState=" + 0;
        initView();
        setLoading();
        inflateDatas();
    }

    private void initView() {
        coupon_title = (TextView) findViewById(R.id.title_name);
        coupon_back = (ImageView) findViewById(R.id.title_back);
        coupon_rg = (RadioGroup) findViewById(R.id.coupon_btn_group);
        coupon_list = (ListView) findViewById(R.id.coupon_content);
        coupon_title.setText(getResources().getString(R.string.coupon));
        coupon_back.setVisibility(View.VISIBLE);
        coupon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        coupon_rg.check(radio[0]);
        coupon_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.coupon_btn1:
                        link2 = link + "&voucherState=" + 0;
                        break;
                    case R.id.coupon_btn2:
                        link2 = link + "&voucherState=" + 1;
                        break;
                    case R.id.coupon_btn3:
                        link2 = link + "&voucherState=" + 2;
                        break;
                    case R.id.coupon_btn4:
                        link2 = link + "&voucherState=" + 3;
                        break;
                }
                setLoading();
                MyApplication.requestQueue.cancelAll(this);
                inflateDatas();
            }
        });

        loading = findViewById(R.id.lny_loading_layout);
        noResult = findViewById(R.id.lny_no_result);
        networkErr = findViewById(R.id.lny_network_error_view);
        reload = networkErr.findViewById(R.id.wifi_retry);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading();
                inflateDatas();
            }
        });
    }

    private void inflateDatas() {
        VolleyRequest.GetCookieRequest(this, link2, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("wcl",link2);
                    //Gson解析json数据
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<CouponEntity>>() {
                    }.getType();
                    JsonArray item = gson.fromJson(result, JsonObject.class).getAsJsonArray("data");
                    data = new ArrayList<CouponEntity>();
                    data = gson.fromJson(item, type);
                    if (data == null || data.size() == 0) {
                        setNoResult();
                    } else {
                        if (isFlag) {
                            adapter.changeData(data);
                            setResult();
                        } else {
                            setAdapter(data);
                            isFlag = true;
                        }
                    }
                } catch (JsonSyntaxException e) {
                    setNoResult();
                }
            }

            @Override
            public void onFail(String error) {
                setNetworkErr();
            }
        });
    }

    private void setAdapter(List<CouponEntity> data) {
        adapter = new CouponAdapter(this, data);
        coupon_list.setAdapter(adapter);
        setResult();
    }

    private void hideAllView() {
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        coupon_list.setVisibility(View.GONE);
    }

    private void setResult() {
        hideAllView();
        coupon_list.setVisibility(View.VISIBLE);
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
