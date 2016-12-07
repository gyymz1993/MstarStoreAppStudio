package cn.mstar.store.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.HistoryAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.HistoryItem;
import cn.mstar.store.entity.ShoppingCartItem;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class ScanHistoryActivity extends BaseActivity implements View.OnClickListener, HistoryAdapter.OnDeleteListener {

    public static String TAG = "wcl";
    private static int ALL = 1;
    private static int SINGLE = 2;
    private int delete_state;
    private TextView history_title, history_clean;
    private ImageView history_back;
    private ListView history_list;
    private AlertDialog dialog;
    private Button confirm, cancel;
    private TextView dialog_title;
    private String link, delete_link_single, delete_link_all;
    private HistoryAdapter adapter;
    private List<HistoryItem> data;
    private View loading, noResult, networkErr, reload;
    private int SCREENWIDTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_history);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        String tokenKey = Utils.getTokenKey((MyApplication) getApplication());
        link = NewLink.HISTORY_BROWSE + "&key=" + tokenKey;
        delete_link_single = NewLink.HISTORY_DELETE_SINGLE + "&key=" + tokenKey;
        delete_link_all = NewLink.HISTORY_DELETE_ALL + "&key=" + tokenKey;
        SCREENWIDTH = getScreenWidth();
        initView();
        setLoading();
        inflateDatas();
    }

    private void initView() {
        history_title = (TextView) findViewById(R.id.title_name);
        history_clean = (TextView) findViewById(R.id.tv_filter);
        history_back = (ImageView) findViewById(R.id.title_back);
        history_title.setText(getResources().getString(R.string.scan_history_title));
        history_clean.setText(getResources().getString(R.string.scan_history_clean));
        history_back.setVisibility(View.VISIBLE);
        history_clean.setVisibility(View.VISIBLE);
        history_clean.setOnClickListener(this);
        history_back.setOnClickListener(this);
        history_list = (ListView) findViewById(R.id.scan_history_list);
        dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.scan_history_dialog, null, false);
        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        cancel = (Button) view.findViewById(R.id.scan_history_cancel_btn);
        confirm = (Button) view.findViewById(R.id.scan_history_confirm_btn);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        dialog.setView(view);

        loading = findViewById(R.id.lny_loading_layout);
        noResult = findViewById(R.id.lny_no_result);
        networkErr = findViewById(R.id.lny_network_error_view);
        reload = networkErr.findViewById(R.id.wifi_retry);
        reload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filter:
                delete_state = ALL;
                dialog_title.setText("清空所有浏览记录！");
                dialog.show();
                break;
            case R.id.scan_history_cancel_btn:
                dialog.dismiss();
                break;
            case R.id.scan_history_confirm_btn:

                if (adapter != null) {
                    String url = null;
                    if (delete_state == ALL) {
                        url = delete_link_all;
                    } else if (delete_state == SINGLE) {
                        url = delete_link_single + "&browse_id=" + item.browse_id;
                    }
                    VolleyRequest.GetCookieRequest(this, url, new VolleyRequest.HttpStringRequsetCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                            if ("删除成功".equals(message)) {
                                if (delete_state == ALL) {
                                    adapter.clearList();
                                    setNoResult();
                                }
                                else if(delete_state == SINGLE) {
                                    if (size == 1) {
                                        setNoResult();
                                    }
                                    adapter.deleteItem(item);
                                }
                                Toast.makeText(ScanHistoryActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ScanHistoryActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            setNetworkErr();
                        }
                    });

                }
                dialog.dismiss();
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.wifi_retry:
                setLoading();
                inflateDatas();
                break;
        }
    }

    public void inflateDatas() {
        VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.d(TAG, result);
                    Gson gson = new Gson();
                    JsonArray item = gson.fromJson(result, JsonObject.class).getAsJsonArray("data");
                    Type type = new TypeToken<HistoryItem[]>() {
                    }.getType();
                    HistoryItem[] itemz = gson.fromJson(item, type);
                    data = new ArrayList<>();
                    for (HistoryItem tmp : itemz) {
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

    public void setAdapter(List<HistoryItem> data) {
        adapter = new HistoryAdapter(this, data, SCREENWIDTH);
        history_list.setAdapter(adapter);
        adapter.setOnDeleteListener(this);
        setResult();
    }

    private void hideAllView() {
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        history_list.setVisibility(View.GONE);
    }

    private void setResult() {
        hideAllView();
        history_list.setVisibility(View.VISIBLE);
    }

    private void setLoading() {
        hideAllView();
        loading.setVisibility(View.VISIBLE);
    }

    public void setNoResult() {
        hideAllView();
        noResult.setVisibility(View.VISIBLE);
    }

    private void setNetworkErr() {
        hideAllView();
        networkErr.setVisibility(View.VISIBLE);
    }

    public int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.requestQueue.cancelAll(this);
    }

    private HistoryItem item;
    private int size;

    @Override
    public void onDelete(HistoryItem item, int size) {
        dialog_title.setText("删除该浏览记录！");
        delete_state = SINGLE;
        this.item = item;
        this.size = size;
        dialog.show();
    }
}
