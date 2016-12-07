package cn.mstar.store.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.app.Constants2;
import cn.mstar.store.entity.CommonJson;
import cn.mstar.store.entity.NearstoreEntity;
import cn.mstar.store.interfaces.HttpRequestCallBack;
import cn.mstar.store.interfaces.MstarLocationListener;
import cn.mstar.store.adapter.StoreAdapter;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.entity.MstarGeoPoint;
import cn.mstar.store.entity.StoreInfo;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.L;
import cn.mstar.store.customviews.MarqueeText;
import cn.mstar.store.utils.VolleyRequest;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.json.JSONObject;

/**
 * 附近门店
 * @author wenjundu 2015-7-16
 *
 */
public class NearStoreActivity extends BaseActivity implements OnClickListener{
	private LocationClient mLocationClient;
	private LocationMode tempMode =  LocationMode.Hight_Accuracy;//定位精度
	private MarqueeText locationTV; //显示位置文字
    private double longitude;
	private double latitude;
	private TextView refreshBtn;
	private MyApplication application;
	private ImageView titleBack;//返回
	private TextView titleName;//标题
	private ListView listView; //门店信息list
	private List<StoreInfo> list;
	private StoreAdapter adapter;//门店信息adapter
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_store);
		Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
		Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));
		application=((MyApplication)getApplication());
		application.addActivity(this);
		mLocationClient=application.mLocationClient;

		initView();
		initListener();
		initData();
		InitLocation();
		mLocationClient.start();
		//mLocationClient.requestLocation();
	}
	//初始化数据
	private void initData() {
		// TODO Auto-generated method stub
		list=new ArrayList<StoreInfo>();
//		for(int i=0;i<4;i++){
//			StoreInfo storeInfo=new StoreInfo("", "广东省深圳市罗湖区翠竹路直营店", "22.22km", "营业时间:06:00--22:00", "联系电话:0755-88888888",24.323235,28.888888);
//			list.add(storeInfo);
//		}

		adapter=new StoreAdapter(this, list);
		listView.setAdapter(adapter);
	//	getNearStoreInfo();
	}

	private void getNearStoreInfo() {
		//String nearstoreInfoUrl= Constants2.NEARSTORE_URL+"&longitude="+"114.131255"+"&latitude="+"22.57389"+"&limitNum="+"2";
		String nearstoreInfoUrl= NewLink.NEAR_STORE+"&longitude="+longitude+"&latitude="+latitude+"&limitNum=2";
		L.e("logisticsInfoUrl:" + nearstoreInfoUrl);
		VolleyRequest.GetRequest(this, nearstoreInfoUrl, new HttpRequestCallBack() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				L.e(jsonObject.toString());
				CommonJson<NearstoreEntity> cj = CommonJson.fromJson(jsonObject.toString(), NearstoreEntity.class);
				if (cj.getError().equals("0")) {
					NearstoreEntity nearstoreEntity = cj.getData();
					StoreInfo[] shopInfo = nearstoreEntity.shopInfo;
//					ShipInfo[] shipInfos = logisticsEntity.shipInfo;
//					companyTV.setText(expressInfo.geteName());
//					waybillTV.setText(expressInfo.getShippingCode());
//					orderTimeTV.setText(expressInfo.getAddTime());
					list.addAll(Arrays.asList(shopInfo));
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(String fail) {

			}
		});

	}



	//初始化监听
	private void initListener() {
		// TODO Auto-generated method stub
		refreshBtn.setOnClickListener(this);
		titleBack.setOnClickListener(this);
		application.setMstarLocationListener(new MstarLocationListener() {

			@Override
			public void successed(MstarGeoPoint point) {
				// TODO Auto-generated method stub

				locationTV.setText(point.getAddress());
				longitude = point.getLongtitude();
				latitude = point.getLatitude();
				list.clear();
				getNearStoreInfo();
				mLocationClient.stop();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				String storename=list.get(position).getStore_name();
				String tel=list.get(position).getTel();
				String store_label=list.get(position).getStore_label();
				Double longitude=list.get(position).getLongitude();//经度
				Double latitude=list.get(position).getLatitude();//纬度
				Intent intent=new Intent(NearStoreActivity.this,NavigationActivity.class);
				Bundle bundle = new Bundle();                           //创建Bundle对象
				bundle.putString("storename",storename );     //装入数据
				bundle.putString("longitude",String.valueOf(longitude));
				bundle.putString("tel",tel);
				bundle.putString("latitude",String.valueOf(latitude) );
				bundle.putString("store_label",store_label);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		locationTV=(MarqueeText) findViewById(R.id.tv_location);
		refreshBtn=(TextView) findViewById(R.id.tv_refresh);
		titleBack=(ImageView) findViewById(R.id.title_back);
		titleName=(TextView) findViewById(R.id.title_name);
		listView=(ListView) findViewById(R.id.store_list);
		
		titleBack.setVisibility(View.VISIBLE);
		titleName.setText(getString(R.string.near_store));

	}
	//初始化定位参数
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		option.setOpenGps(true);
		option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyApplication.requestQueue.cancelAll(this);
		mLocationClient.stop();
		super.onDestroy();
		L.e("onDestroy");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_refresh://重新定位
			InitLocation();
			locationTV.setText(getString(R.string.locationing));
			mLocationClient.start();

			break;

		case R.id.title_back:
			finish();
			break;
		}
	}


	
}
