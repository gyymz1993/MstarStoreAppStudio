package cn.mstar.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import cn.mstar.store.R;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.PreferenceMessage;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by wcl at Shenzhen.
 */
public class MessageActivity extends BaseActivity {
    private View loading,noResult,networkErr,reload;
    private View message_layout;
    private ImageView title_img;
    private TextView title_name;
    private TextView preference_total, preference_desc, preference_time;
    private LinearLayout layout;
    private String link;
    private PreferenceMessage item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        setContentView(R.layout.message_layout);
        link = NewLink.PREFERENCE_INFO;
        initView();
        setLoading();
        inflateDatas();
    }

    private void initView() {
        title_img = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        preference_total = (TextView) findViewById(R.id.preference_total_num);
        preference_desc = (TextView) findViewById(R.id.preference_desc);
        preference_time = (TextView) findViewById(R.id.preference_time);
        layout = (LinearLayout) findViewById(R.id.preference_layout);
        title_name.setText(getResources().getString(R.string.message_title_name));
        title_img.setVisibility(View.VISIBLE);
        title_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, PreferenceActivity_wcl.class);
                startActivity(intent);
            }
        });
        
        message_layout = findViewById(R.id.message_layout);
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

        VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<PreferenceMessage>() {
                    }.getType();
                    JsonObject jObj = gson.fromJson(result, JsonObject.class).getAsJsonObject("data");
                    item = gson.fromJson(jObj, type);
                    if (item != null){
                        setData();
                    }else{
                        setNoResult();
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

    private void setData(){
        preference_total.setText(item.getTotalNum() + "");
        preference_desc.setText(item.getDescript());
        preference_time.setText(item.getTime());
        setResult();
    }

    private void hideAllView(){
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        message_layout.setVisibility(View.GONE);
    }

    private void setResult(){
        hideAllView();
        message_layout.setVisibility(View.VISIBLE);
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
}
