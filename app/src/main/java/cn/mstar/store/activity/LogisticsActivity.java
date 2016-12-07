package cn.mstar.store.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.LogisticsInfoAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.HistoryItem;
import cn.mstar.store.entity.LogisticsItem;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by ld_wcl at Shenzhen.
 */
public class LogisticsActivity extends BaseActivity{
    private View loading,noResult,networkErr,reload;
    private TextView logisticsTitle;
    private ImageView logisticsBack_btn;
    private ListView logistics_list;
    private String link;
    private List<LogisticsItem> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_information);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        link = NewLink.LOGISTICS_INFO + "&key=" + Utils.getTokenKey((MyApplication) getApplication());
        initView();
        setLoading();
        inflateDatas();
    }

    private void initView(){
        logisticsTitle = (TextView) findViewById(R.id.title_name);
        logisticsBack_btn = (ImageView) findViewById(R.id.title_back);
        logistics_list = (ListView)findViewById(R.id.logistics_info_list);
        logisticsTitle.setText(getResources().getString(R.string.logistics_title_name));
        logisticsBack_btn.setVisibility(View.VISIBLE);
        logisticsBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void inflateDatas(){
        VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    Gson gson = new Gson();
                    JsonArray item = gson.fromJson(result, JsonObject.class).getAsJsonArray("data");
                    Type type = new TypeToken<LogisticsItem[]>() {
                    }.getType();
                    LogisticsItem[] itemz = gson.fromJson(item, type);
                    data = new ArrayList<>();
                    for (LogisticsItem tmp : itemz) {
                        data.add(tmp);
                    }
                    if (data == null || data.size() == 0) {
                        setNoResult();
                    } else {
                        setAdapter(data);
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

    private void setAdapter(List<LogisticsItem> data){
        if (data!=null&&data.size()!=0){
            LogisticsInfoAdapter adapter = new LogisticsInfoAdapter(this,data,getScreenWidth());
            logistics_list.setAdapter(adapter);
            setResult();
        }

    }

    private void hideAllView(){
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        logistics_list.setVisibility(View.GONE);
    }

    private void setResult(){
        hideAllView();
        logistics_list.setVisibility(View.VISIBLE);
    }

    private void setLoading(){
        hideAllView();
        loading.setVisibility(View.VISIBLE);
    }

    private void setNoResult() {
        hideAllView();
        noResult.setVisibility(View.VISIBLE);
    }

    private void setNetworkErr(){
        hideAllView();
        networkErr.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.requestQueue.cancelAll(this);
    }

    private int getScreenWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
