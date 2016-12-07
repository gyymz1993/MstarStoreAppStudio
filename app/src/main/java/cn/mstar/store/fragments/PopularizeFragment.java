package cn.mstar.store.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.adapter.PopularizeAdapter;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.customviews.PullToRefreshView;
import cn.mstar.store.entity.PopEntity;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class PopularizeFragment extends Fragment implements PullToRefreshView.OnFooterRefreshListener,
        PullToRefreshView.OnHeaderRefreshListener {

    private static final int HEAD_REFRESH = 1;
    private static final int FOOTER_LOAD = 2;
    private List<PopEntity> data;
    private PopularizeAdapter adapter;
    private String url;
    private String url2;
    private LoadingDialog dialog;
    private boolean isFirstAccess = true;
    private PullToRefreshView refreshView;
    private ListView list;
    private TextView notify;
    private int curpage = 1;
    private int prepage;
    private int state = 0;
    private int page = 10;

    public PopularizeFragment(String url) {
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url2 = url + "&page=10&curpage=1";
        data = new ArrayList<>();
        adapter = new PopularizeAdapter(getActivity(), data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_comment_list_fragment, container, false);
        refreshView = (PullToRefreshView) view.findViewById(R.id.comment_refresh_view);
        refreshView.setOnFooterRefreshListener(this);
        refreshView.setOnHeaderRefreshListener(this);
        list = (ListView) view.findViewById(R.id.comment_list);
        list.setDivider(getResources().getDrawable(R.color.transparent));
        list.setDividerHeight(1);
        notify = (TextView) view.findViewById(R.id.comment_list_notify);
        notify.setText("暂无数据");
        list.setAdapter(adapter);
        if (isFirstAccess) {
            isFirstAccess = false;
            showDialog();
            inflateData();
        }
        return view;
    }

    private void inflateData() {
        VolleyRequest.GetCookieRequest(getActivity(), url2, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    Gson gson = new Gson();
                    int totalItem = gson.fromJson(result, JsonObject.class).get("data").getAsJsonObject().get("list_count").getAsInt();
                    if (totalItem == 0){
                        notify.setVisibility(View.VISIBLE);
                        return;
                    }
                    else if (curpage <= (totalItem + page - 1) / page){
                        JsonArray item = gson.fromJson(result, JsonObject.class).get("data").getAsJsonObject().get("tgInfo").getAsJsonArray();
                        Type type = new TypeToken<PopEntity[]>() {
                        }.getType();
                        PopEntity[] arr = gson.fromJson(item, type);
                        for (PopEntity p : arr) {
                            data.add(p);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        curpage--;
                        Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                    }
                    notify.setVisibility(View.GONE);
                } catch (Exception e) {
                    data.clear();
                    adapter.notifyDataSetChanged();
                    notify.setVisibility(View.VISIBLE);
                } finally {
                    if (state == HEAD_REFRESH){
                        refreshView.onHeaderRefreshComplete();
                    }
                    else if (state == FOOTER_LOAD){
                        refreshView.onFooterRefreshComplete();
                    }
                    if (dialog.isShowing())
                        hideDialog();
                }
            }

            @Override
            public void onFail(String error) {
                if (state == FOOTER_LOAD)
                    curpage--;
                if (state == HEAD_REFRESH)
                    curpage = prepage;
                Toast.makeText(getActivity(), "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
                if (dialog.isShowing())
                    hideDialog();
            }
        });
    }

    private void showDialog() {
        dialog = new LoadingDialog(getActivity());
        dialog.show();
    }

    private void hideDialog() {
        dialog.dismiss();
    }

    private void changeURL() {
        url2 = url + "&page=" + page + "&curpage=" + curpage;
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        state = FOOTER_LOAD;
        curpage++;
        changeURL();
        inflateData();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        state = HEAD_REFRESH;
        prepage = curpage;
        curpage = 1;
        changeURL();
        data.clear();
        inflateData();
    }
}