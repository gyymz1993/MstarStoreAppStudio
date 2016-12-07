package cn.mstar.store.fragments;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.mstar.store.R;
import cn.mstar.store.activity.MockActivity;
import cn.mstar.store.utils.Utils;


public class ExchangePointsFragment extends Fragment {

	private static final String[] LINK = {"http://d.hiphotos.baidu.com/baike/s%3D235/sign=a9a9149ea8d3fd1f3209a539054f25ce/38dbb6fd5266d01622061998972bd40735fa3566.jpg",
		"http://a0.att.hudong.com/48/29/01200000023857134402296829922_02_250_250.jpg",
		"http://p14.qhimg.com/dr/360_210_/t014f8930da9b79ebf1.png",
		"http://a.hiphotos.baidu.com/baike/s%3D220/sign=032c42d9ddc451daf2f60be986ff52a5/4b90f603738da97721eb5cacb051f8198718e375.jpg",
	"http://a.hiphotos.baidu.com/baike/c0%3Dbaike150%2C5%2C5%2C150%2C50/sign=e4839cb4f8edab64607f4592965fc4a6/9f2f070828381f30a9a86fd6aa014c086e06f018.jpg"};
	ImageView imageview;
	Context mContext = null;

	ListView lv_side;
	boolean first = false;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.exchangepointsfragment, null);

		initViews (view);
		first = true;
		lv_side.setAdapter(new SideListAdapter(getActivity(), null));
		// tell the activity to hide all the content, and to show loading...

		// when the data is retrieven, the adapters are set.
		remplaceFragment (0);
		// inflate the content		
		// set a default fragment (the first one)...

		lv_side.performItemClick(
				lv_side.getAdapter().getView(0, null, null),
			    0,
			    lv_side.getAdapter().getItemId(0));

		
		return view;
	}


	private void initViews(View view) {
		lv_side = (ListView) view.findViewById(R.id.lv_exchange);
	}


	public  int getScreenHeigth () {

		DisplayMetrics metrics = new DisplayMetrics();
		((MockActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public   int getScreenWidth () {

		DisplayMetrics metrics = new DisplayMetrics();
		((MockActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}


	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public void remplaceFragment (int position) {
		// get the range from the adapter and show the data inside the 
		// fragments...
		// make simple fragments that already have the data in parameter that just show ~ 
		ExchangeItemsListFragment frg =   ExchangeItemsListFragment.getInstance (LINK[position]);
		FragmentManager trans = getActivity().getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = trans.beginTransaction();
		fragmentTransaction.add(R.id.frame_exchange_items, frg, "image_fragment");
		fragmentTransaction.show(frg);
		fragmentTransaction.commit();
	}


	private class SideListAdapter extends BaseAdapter {


		private List<String> data;
		private LayoutInflater inf;
		private Context mContext;
		private int DEFAULT_COLOR = R.color.light_gray;
		private TextView previous_view;

		public SideListAdapter (Context mc, List<String> d) {

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
		public View getView(final int position, View convertView, ViewGroup parent) {

			/*
			View view = null;
			if (convertView == null) {
			}*/
			final TextView tv = new TextView(mContext);
			tv.setText((position+1)+"000 ~ "+(position+2)+"000");
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(pxToDp(5), pxToDp(10), pxToDp(5), pxToDp(10));
			tv.setBackgroundColor(getResources().getColor(DEFAULT_COLOR));


			if (first && position == 0) {
				tv.setBackgroundColor(getResources().getColor(R.color.white));
				first = false;
				previous_view = tv;
			}

			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (previous_view != null)
						previous_view.setBackgroundColor(getResources().getColor(DEFAULT_COLOR));
					tv.setBackgroundColor(getResources().getColor(R.color.white));
					previous_view = tv;		
					remplaceFragment (position);
				}
			});

			return tv;
		}

		class ViewHolder {
			public TextView tv;
		}
	}

	public int pxToDp (int px) {
		return Utils.convertPxtoDip(px, getActivity());
	}

}
