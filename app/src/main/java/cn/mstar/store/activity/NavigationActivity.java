package cn.mstar.store.activity;

import cn.mstar.store.R;
import cn.mstar.store.utils.ImageLoadOptions;
import cn.mstar.store.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;


import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 线路导航activity
 * @author wenjundu 2015-7-16
 *
 */
public class NavigationActivity extends BaseActivity {
	private LocationMode mCurrentMode;
	private Marker mMarkerA;
	BaiduMap mBaiduMap;
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	LocationClient mLocClient;
	boolean isFirstLoc = true;// 是否首次定位
	public MyLocationListenner myListener = new MyLocationListenner();
	String Store_label;
	String storename;
	String tel;
	Double longitude;//经度
	Double latitude;//纬度
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA;
	private InfoWindow mInfoWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_dynlocateto_layout);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_baidumap_layout);
		Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
		Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
		bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		getData();
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//覆盖物
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {

				View view=getLayoutInflater().inflate(R.layout.baidumap_storeinfo,null);
				ImageView storeimg=(ImageView)view.findViewById(R.id.baidu_store_img);
				TextView astroename=(TextView)view.findViewById(R.id.store_name);
				TextView tele=(TextView)view.findViewById(R.id.store_tel);
				astroename.setText(storename);
				tele.setText(tel);
				ImageLoader.getInstance().displayImage(Store_label, storeimg, ImageLoadOptions.getOptions());
				OnInfoWindowClickListener listener=null;
				if (marker==mMarkerA)
				{
					//button.setText(storename + "/n" + tel);
					view.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							mBaiduMap.hideInfoWindow();
						}
					});
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(view, ll, -47);
					mBaiduMap.showInfoWindow(mInfoWindow);

				}
				return true;
			}
		});

		mCurrentMode = LocationMode.NORMAL;
//		mCurrentMarker =BitmapDescriptorFactory
//				.fromResource(R.drawable.icon_geo);
		mCurrentMarker =null;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();



	}

	private void initOverlay() {
		LatLng llA = new LatLng(latitude,longitude);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
	}

	private void getData() {
		Intent intent = this.getIntent();        //获取已有的intent对象
		Bundle bundle = intent.getExtras();    //获取intent里面的bundle对象
		storename = bundle.getString("storename");
		tel=bundle.getString("tel");
		longitude=Double.parseDouble(bundle.getString("longitude"));
		latitude=Double.parseDouble(bundle.getString("latitude"));
		Store_label=bundle.getString("store_label");
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
							// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
		bdA.recycle();
	}

}
