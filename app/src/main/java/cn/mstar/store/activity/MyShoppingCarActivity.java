package cn.mstar.store.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.db.entityToSave.ProAndSpecialIdz;
import cn.mstar.store.entity.ShoppingCartItem;
import cn.mstar.store.fragments.ShoppingCartFragment;
import cn.mstar.store.interfaces.OnGoodsCheckedListener;
import cn.mstar.store.utils.L;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;
import cn.mstar.store.utils.VolleyRequest;

public class MyShoppingCarActivity extends MockActivity {


	//	LoadingDialog dialog;
	private int SCREENWIDTH;
	private android.support.v4.app.Fragment frgContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.setNavigationBarColor(this, getResources().getColor(R.color.status_bar_color));
		Utils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_color));

		// set title
		super.setMyTitle(getString(R.string.myshoppingcar));
		SCREENWIDTH = getScreenWidth(this);
		// then i could add the fragment item inside the layout of this.
		setLoading(true);
//		dialog = new LoadingDialog(this);

		VolleyRequest.checkLogStatus((MyApplication) getApplication(), new VolleyRequest.LogonStatusLinstener() {
			@Override
			public void OK(String reason) {

				// 登录完成后
				afterLogin();
			}

			@Override
			public void NO() {

				// 未登录
				beforeLogin();
			}
		});

	}

	private void afterLogin() {

		String link = NewLink.LIST_SHOPPING_CART+"&key="+ Utils.getTokenKey((MyApplication) getApplication());
		L.d("XXX 登陆后", link);
		inflateData(link);
	}

	private void beforeLogin() {
		List<ProAndSpecialIdz> localItems = ProAndSpecialIdz.getAll();
		String proId = "";
		for (ProAndSpecialIdz mp : localItems) {
			if (!proId.equals(""))
				proId+="|";
			proId+=mp.proId;
		}
		String link = NewLink.LOCAL_SHOPPING_CART+"&proid="+proId;
		inflateData(link);
	}

	private void inflateData(String link) {
		link+="&size="+(SCREENWIDTH < 700 ? "60" : "240");
		VolleyRequest.GetCookieRequest(this, link, new VolleyRequest.HttpStringRequsetCallBack() {
			@Override
			public void onSuccess(String result) {
				// get the result as a JsonObject and
				// turn the result into the ShoppingCardObject.
				try {
					L.d("XXX", result);
					Gson gson = new Gson();
					JsonArray item = gson.fromJson(result, JsonObject.class).getAsJsonArray("data");
					Type type = new TypeToken<ShoppingCartItem[]>() {
					}.getType();
					ShoppingCartItem[] itemz = gson.fromJson(item, type);
					if (itemz == null || itemz.length == 0)
						setNoResult(true);
					else {
						setFragment(itemz);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setNoResult(true);
				}
			}

			@Override
			public void onFail(String error) {
				// network error certainly
				setNetworkError(true);
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	public void setFragment(ShoppingCartItem[] itemz) {

		// send the itemz to the fragment as a data list.
		List<ShoppingCartItem> data = new ArrayList<>();
		for (ShoppingCartItem tmp: itemz) {
			data.add(tmp);
		}
		// create the fragment and add it to the layout.
		if (frgContent == null)
			frgContent = ShoppingCartFragment.getInstance((new Gson()).toJson(data));
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(framelayout_main.getId(), frgContent);
		fragmentTransaction.commit();
		all_invisible();
		framelayout_main.setVisibility(View.VISIBLE);
	}

	/*public static List<ShoppingCartItem> buildlFakeData() {
		List<ShoppingCartItem> data = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			ShoppingCartItem item = new ShoppingCartItem();
			item.cartId = 0;
			item.className = "掉队";
			item.name = "千禧之星3d 租金掉队滴大幅度发圣诞节";
			item.pic = "http://211.162.71.165:8082.UploadFiles/2015-7/324343434.jpg";
			item.price = 122.332;
			data.add(item);
		}
		return data;
	}*/


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyApplication.requestQueue.cancelAll(this);
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void updateBottomTotal(List<ShoppingCartItem> checkedGoods) {
		// look for the fragment ...
		if (frgContent != null)
			((OnGoodsCheckedListener) frgContent).updateBottom(checkedGoods);
	}
}
