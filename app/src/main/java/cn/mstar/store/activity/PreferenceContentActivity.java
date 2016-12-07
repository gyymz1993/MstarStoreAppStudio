package cn.mstar.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.Preference_Wcl_Adapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.PreferenceListItem;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by wcl at Shenzhen.
 */
public class PreferenceContentActivity extends BaseActivity {
    private View loading, noResult, networkErr, reload;
    private ImageView preference_content_back;
    private TextView preference_content_title;
    private ListView preference_content_list;
    private String link;
    private String rece_title;
    private int rece_activityId;
    private List<PreferenceListItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_content_wcl);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        Intent intent = getIntent();
        rece_title = intent.getStringExtra("title");
        rece_activityId = intent.getIntExtra("activity_id", -1);
        link = NewLink.PREFERENCE_CONTENT_LIST + "&activity_id=" + rece_activityId;
        initView();
        inflateDatas();
    }

    private void initView() {
        preference_content_back = (ImageView) findViewById(R.id.title_back);
        preference_content_title = (TextView) findViewById(R.id.title_name);
        preference_content_list = (ListView) findViewById(R.id.preference_wcl_list);
        preference_content_title.setText(rece_title);
        preference_content_back.setVisibility(View.VISIBLE);
        preference_content_back.setOnClickListener(new View.OnClickListener() {
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

    private void inflateDatas() {
        VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            Gson gson = new Gson();
                            JsonArray jArr = gson.fromJson(result, JsonObject.class).getAsJsonObject("data").get("search").getAsJsonArray();
                            Type type = new TypeToken<PreferenceListItem[]>() {
                            }.getType();
                            PreferenceListItem[] preArr = gson.fromJson(jArr, type);
                            data = new ArrayList<PreferenceListItem>();
                            for (PreferenceListItem item : preArr) {
                                data.add(item);
                            }
                            if (data.size() == 0) {
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
                }

        );
    }

    private void setAdapter(List<PreferenceListItem> data) {
        Preference_Wcl_Adapter adapter = new Preference_Wcl_Adapter(this, data);
        preference_content_list.setAdapter(adapter);
        setResult();
    }

    private void hideAllView() {
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        preference_content_list.setVisibility(View.GONE);
    }

    private void setResult() {
        hideAllView();
        preference_content_list.setVisibility(View.VISIBLE);
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
