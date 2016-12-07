package cn.mstar.store.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.ExchangePointActivity;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.Utils;

/**
 * @author UlrichAbiguime
 *
 */
public class ExchangeItemsListFragment extends ListFragment {


	// i know how to make a custom parcelable, so i can do it.


	public     int SCREEN_WIDTH = -1;
	public static final String PARAMS = "akljdfnanhfliqanlkmnflakhfnkanflajfrsd";
	private   String LINK = "";

	public static ExchangeItemsListFragment getInstance(String string) {

		ExchangeItemsListFragment frg = new ExchangeItemsListFragment();
		Bundle args = new Bundle();
		args.putString(PARAMS, string);
		frg.setArguments(args);
		return frg;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// the informations about the adapter are directly brought down here...
		if (SCREEN_WIDTH == -1) {
			SCREEN_WIDTH = ((ExchangePointActivity) getActivity()).getScreenWidth(getActivity());
		}
		LINK = getArguments().getString(PARAMS);
		if (LINK == null || LINK.equals("")) {
			LINK = "http://baike.baidu.com/cms/s/majiancai/240x112.jpg";
		}
		getListView().setDividerHeight(1);
		setListAdapter(new ContentListAdapter(getActivity(), null));
	}



	private class ContentListAdapter extends BaseAdapter {



		private List<String> data;
		private LayoutInflater inf;
		private Context mContext;

		public ContentListAdapter (Context mc, List<String> d) {

			mContext = mc;
			data = d;
			inf = LayoutInflater.from(mc);
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {


			View view = null;
			ViewHolder vh = null;
			if (convertView == null) {
				view = inf.inflate(R.layout.exchange_item_custom_item, null);
				vh = (new ViewHolder()).makeUp (view);
			} else {
				view = convertView;
				vh = (ViewHolder) convertView.getTag();
			}
			// get the view height...
			// view / 3 / 4
			view.setBackgroundColor(getResources().getColor(R.color.white));
			LayoutParams layoutParams = vh.iv_item_pic.getLayoutParams();
			layoutParams.width =  SCREEN_WIDTH / 8;
			vh.iv_item_pic.setLayoutParams(layoutParams);

			ImageLoader.getInstance().displayImage(LINK, vh.iv_item_pic, ImageLoadOptions.getOptions());
			view.setTag(vh);
			return view;
		}

		class ViewHolder {
			public TextView tv_name, tv_count, tv_points_count;
			public ImageView iv_item_pic;

			public   ViewHolder makeUp(View view) {
				ViewHolder vh = new ViewHolder();
				vh.tv_name = (TextView) view.findViewById(R.id.tv_name);
				vh.tv_count = (TextView) view.findViewById(R.id.tv_count);
				vh.tv_points_count = (TextView) view.findViewById(R.id.tv_points_count);
				vh.iv_item_pic = (ImageView) view.findViewById(R.id.iv_custom_ex);
				return vh;
			}
		}
	}

	public int pxToDp (int px) {
		return Utils.convertPxtoDip(px, getActivity());
	}



}
