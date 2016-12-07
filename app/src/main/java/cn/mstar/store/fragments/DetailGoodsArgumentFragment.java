package cn.mstar.store.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.ProductDetailsActivity;
import cn.mstar.store.adapter.DetailsListAdapter;
public class DetailGoodsArgumentFragment extends Fragment {
    private View view;
    public static Fragment getInstance(String attr) {
        Fragment fragment = new DetailGoodsArgumentFragment();
        Bundle args = new Bundle();
        args.putString("attr", attr);
        fragment.setArguments(args);
        return fragment;
    }
    private DetailsListAdapter adapter;
    private List<String> dataList=new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_details_layout, container, false);
        ProductDetailsActivity superActivity = (ProductDetailsActivity)getActivity();
        initView();
        return view;
    }

    private void initView(){
        ListView lv = (ListView) view.findViewById(R.id.list_content);
        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        String attr = getArguments().getString("attr");
        Gson gson = new Gson();
        String[] data = gson.fromJson(attr, String[].class);
        lv.setAdapter(new DetailsListAdapter(getActivity(), data));
    }
}
