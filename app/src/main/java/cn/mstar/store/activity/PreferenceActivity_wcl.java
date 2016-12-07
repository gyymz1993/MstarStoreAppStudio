package cn.mstar.store.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.PreferenceAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.PreferenceContentItem;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by wcl at Shenzhen.
 */
public class PreferenceActivity_wcl extends BaseActivity {
    private View loading,noResult,networkErr,reload;
    private ImageView preference_back_btn;
    private TextView preference_title_txt;
    private ListView preference_content;
    private String link;
    private List<PreferenceContentItem> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity_layout);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        link = NewLink.PREFERENCE_CONTENT;
        initView();
        setLoading();
        inflateDatas();
    }

    private void initView() {
        preference_back_btn = (ImageView) findViewById(R.id.title_back);
        preference_title_txt = (TextView) findViewById(R.id.title_name);
        preference_content = (ListView) findViewById(R.id.preference_content);
        preference_title_txt.setText(getResources().getString(R.string.preference_activity_title));
        preference_back_btn.setVisibility(View.VISIBLE);
        preference_back_btn.setOnClickListener(new View.OnClickListener() {
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
                    Type type = new TypeToken<List<PreferenceContentItem>>(){}.getType();
                    data = gson.fromJson(item,type);
                    if (data == null || data.size() == 0){
                        setNoResult();
                    }else{
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

    private void setAdapter(List<PreferenceContentItem> data){
        PreferenceAdapter adapter = new PreferenceAdapter(this,data);
        preference_content.setAdapter(adapter);
        setResult();
    }

    private void hideAllView(){
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        preference_content.setVisibility(View.GONE);
    }

    private void setResult(){
        hideAllView();
        preference_content.setVisibility(View.VISIBLE);
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
