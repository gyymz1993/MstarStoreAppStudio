package cn.mstar.store.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.CollectionCategoryAdapter;
import cn.mstar.store.adapter.CollectionContentAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.FavoriteManagerItem;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class MyCollectionActivity extends BaseActivity implements CollectionCategoryAdapter.OnCategoryItemClickListener,
        CollectionContentAdapter.OnDeleteItemListener, View.OnClickListener {
    private View loading, noResult, networkErr, reload;
    private LinearLayout collection_all_wcl;
    private ImageView collection_back;
    private TextView collection_title;
    private LinearLayout collection_bar;
    private TextView collection_all;
    private TextView collection_num;
    private ImageView collection_arrow;
    private View collection_category;
    private ListView collection_category_list;
    private ListView collection_content_list;
    private CollectionCategoryAdapter categoryAdapter;
    private CollectionContentAdapter contentAdapter;
    private String link, link_delete;
    private AlertDialog dialog;
    private Button confirm, cancel;
    private TextView dialog_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycollection_layout);
        Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
        Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
        String tokenKey = Utils.getTokenKey((MyApplication) getApplication());
        link = NewLink.GET_FAVORITE_LIST + "&key=" + tokenKey + "&page=10000"
                + "&size=" + (getScreenWidth() < 700 ? "60" : "240");
        link_delete = NewLink.DELETE_FAVORITE_ITEM + "&key=" + tokenKey;
        initView();
        setLoading();
        inflateDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inflateDatas();
    }

    private void initView() {
        collection_back = (ImageView) findViewById(R.id.title_back);
        collection_title = (TextView) findViewById(R.id.title_name);
        collection_bar = (LinearLayout) findViewById(R.id.collection_bar);
        collection_all = (TextView) findViewById(R.id.collection_all_txt);
        collection_num = (TextView) findViewById(R.id.collection_num);
        collection_arrow = (ImageView) findViewById(R.id.collection_arrow);
        collection_category = findViewById(R.id.collection_category);
        collection_category_list = (ListView) findViewById(R.id.collection_category_list);
        collection_content_list = (ListView) findViewById(R.id.collection_content_list);
        dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.scan_history_dialog, null, true);
        cancel = (Button) view.findViewById(R.id.scan_history_cancel_btn);
        confirm = (Button) view.findViewById(R.id.scan_history_confirm_btn);
        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_title.setText("删除该收藏记录！");
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        dialog.setView(view);
        collection_title.setText(getResources().getString(R.string.collection_title_name));
        collection_back.setVisibility(View.VISIBLE);
        collection_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collection_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collection_category.getVisibility() == View.VISIBLE) {
                    collection_category.setVisibility(View.GONE);
                    collection_arrow.setImageResource(R.drawable.collection_icon_nor);
                } else {
                    collection_category.setVisibility(View.VISIBLE);
                    collection_arrow.setImageResource(R.drawable.collection_icon_down);
                }
            }
        });
        collection_category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                collection_category.setVisibility(View.GONE);
                collection_arrow.setImageResource(R.drawable.collection_icon_nor);
                return true;
            }
        });

        loading = findViewById(R.id.lny_loading_layout);
        noResult = findViewById(R.id.lny_no_result);
        networkErr = findViewById(R.id.lny_network_error_view);
        reload = networkErr.findViewById(R.id.wifi_retry);
        collection_all_wcl = (LinearLayout) findViewById(R.id.collection_all_wcl);
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
                    FavoriteManagerItem item = (new Gson()).fromJson(result, FavoriteManagerItem.class);
                    List<FavoriteManagerItem.InnerData> dataList = new ArrayList<FavoriteManagerItem.InnerData>();
                    int totalItems = 0;
                    for (FavoriteManagerItem.InnerData data : item.data) {
                        dataList.add(data);
                        totalItems += data.count;
                    }
                    if (totalItems == 0) {
                        setNoResult();
                    } else {
                        collection_all.setText(getResources().getString(R.string.collection_all_category));
                        collection_num.setText(totalItems + "");
                        setAdapter(dataList);
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

    private void setAdapter(List<FavoriteManagerItem.InnerData> data) {
        categoryAdapter = new CollectionCategoryAdapter(this, data);
        categoryAdapter.setOnCategoryItemClickListener(this);
        collection_category_list.setAdapter(categoryAdapter);
        if (contentAdapter == null) {
            contentAdapter = new CollectionContentAdapter(this, categoryAdapter.getFavorites(), getScreenWidth());
            collection_content_list.setAdapter(contentAdapter);
        } else {
            contentAdapter.changeData(categoryAdapter.getFavorites());
        }
        contentAdapter.setOnDeleteItemListener(this);
        setResult();
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    public void onCategoryItemClick(String categoryName, int categoryNum, List<FavoriteManagerItem.InnerData.Favorite> data) {
        collection_all.setText(categoryName);
        collection_num.setText(categoryNum + "");
        collection_arrow.setImageResource(R.drawable.collection_icon_nor);
        collection_category.setVisibility(View.GONE);
        contentAdapter.changeData(data);
    }

    private FavoriteManagerItem.InnerData.Favorite item;
    private int size;

    /**
     * 删除收藏项目
     *
     * @param item
     * @param size
     */
    @Override
    public void onDelete(final FavoriteManagerItem.InnerData.Favorite item, final int size) {
        this.item = item;
        this.size = size;
        dialog.show();
    }

    private void deleteData(FavoriteManagerItem.InnerData.Favorite item, int size) {
        categoryAdapter.changeData(item);
        if (size == 1) {
            contentAdapter.changeData(categoryAdapter.getFavorites());
            collection_all.setText(getResources().getString(R.string.collection_all_category));
            collection_num.setText(categoryAdapter.getFavorites().size() + "");
        } else if (size > 1) {
            contentAdapter.deleteData(item);
            contentAdapter.notifyDataSetChanged();
            collection_num.setText((size - 1) + "");
        }
        if (categoryAdapter.getFavorites().size() == 0) {
            setNoResult();
        }
    }

    private void hideAllView() {
        loading.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);
        networkErr.setVisibility(View.GONE);
        collection_all_wcl.setVisibility(View.GONE);
    }

    private void setResult() {
        hideAllView();
        collection_all_wcl.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_history_cancel_btn:
                dialog.dismiss();
                break;
            case R.id.scan_history_confirm_btn:
                String url = link_delete + "&goods_id=" + item.proId;
                VolleyRequest.GetCookieRequest(MyCollectionActivity.this, url, new VolleyRequest.HttpStringRequsetCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        String response = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                        if ("ok".equals(response)) {
                            deleteData(item, size);
                            Toast.makeText(MyCollectionActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyCollectionActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFail(String error) {
                        setNetworkErr();
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (collection_category.getVisibility() == View.VISIBLE){
            collection_category.setVisibility(View.GONE);
            collection_arrow.setImageResource(R.drawable.collection_icon_nor);
        }else{
            super.onBackPressed();
        }
    }
}
