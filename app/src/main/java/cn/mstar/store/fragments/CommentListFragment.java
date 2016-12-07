package cn.mstar.store.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.mstar.store.R;
import cn.mstar.store.adapter.CommentListAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.customviews.LoadingDialog;
import cn.mstar.store.customviews.PullToRefreshView;
import cn.mstar.store.entity.CommentListEntity;
import cn.mstar.store.utils.VolleyRequest;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CommentListFragment extends Fragment implements PullToRefreshView.OnFooterRefreshListener,
        PullToRefreshView.OnHeaderRefreshListener {
    private static final int REFRESH = 1;
    private static final int LOADMORE = 2;
    private int from;
    private String url, url2;
    private int index;
    private int page = 5;
    private int maxPage;
    private int curPage = 1;
    private PullToRefreshView refreshView;
    private ListView list;
    private CommentListAdapter adapter;
    private ArrayList data;
    private CommentListEntity entity;
    private boolean isFlag = false;
    private boolean isVisible = false;
    private TextView notify;

    public CommentListFragment(String url, int index) {
        this.url = url;
        this.index = index;
        url2 = url;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList();
        adapter = new CommentListAdapter(getActivity(), data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_comment_list_fragment, container, false);
        refreshView = (PullToRefreshView) view.findViewById(R.id.comment_refresh_view);
        refreshView.setOnFooterRefreshListener(this);
        refreshView.setOnHeaderRefreshListener(this);
        list = (ListView) view.findViewById(R.id.comment_list);
        notify = (TextView) view.findViewById(R.id.comment_list_notify);
        list.setAdapter(adapter);
        refreshView.onHeaderRefreshComplete();
        if (!isFlag) {
            i_showProgressDialog();
            from = 0;
            initData();
            isFlag = true;
        }
        return view;
    }

    private void initData() {
        VolleyRequest.GetCookieRequest(getActivity(), url2, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    Gson gson = new Gson();
                    JsonObject item = gson.fromJson(result, JsonObject.class).getAsJsonObject("data");
                    Type type = new TypeToken<CommentListEntity>() {
                    }.getType();
                    entity = gson.fromJson(item, type);
                    maxPage = (entity.evaluationCount + page - 1) / page;
                    if (from == LOADMORE && curPage > maxPage) {
                        refreshView.onFooterRefreshComplete();
                        Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (entity.sevallsit == null || entity.sevallsit.size() == 0) {
                        isVisible = true;
                        data.clear();
                    } else {
                        isVisible = false;
                        for (CommentListEntity.CommentContent comment : entity.sevallsit) {
                            data.add(comment);
                        }
                    }
                } catch (Exception e) {
                    isVisible = true;
                    data.clear();
                } finally {
                    refreshData();
                    notify.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onFail(String error) {
                i_dismissProgressDialog();
                Toast.makeText(getActivity(), "网络出现异常，请检查网络再试！", Toast.LENGTH_SHORT).show();
                notify.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        notify.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void refreshData() {
        adapter.notifyDataSetChanged();
        i_dismissProgressDialog();
        switch (from) {
            case REFRESH:
                refreshView.onHeaderRefreshComplete();
                break;
            case LOADMORE:
                refreshView.onFooterRefreshComplete();
                break;
        }
    }

    private void changeUrl() {
        url2 = url + "&curpage=" + (++curPage);
    }

    LoadingDialog dialog;

    public void i_showProgressDialog() {
        dialog = new LoadingDialog(getActivity());
        dialog.show();
    }

    public void i_dismissProgressDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        changeUrl();
        from = LOADMORE;
        initData();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        curPage = 0;
        data.clear();
        changeUrl();
        from = REFRESH;
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyApplication.requestQueue.cancelAll(getActivity());
    }

    private int dp2px(float dp) {
        float density = getActivity().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
