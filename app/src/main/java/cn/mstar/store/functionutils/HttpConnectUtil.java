package cn.mstar.store.functionutils;

import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import cn.mstar.store.R;
import cn.mstar.store.callback.OnSucceedListener;
import cn.mstar.store.entity.JsonResult;
import cn.mstar.store.entity.JsonWeixinPay;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.Utils;

/**
 * 与服务器交互的类
 * 
 *
 */
public class HttpConnectUtil {

	private String USER_URL ;
	public HttpConnectUtil(String url) {
		this.USER_URL=url;
	}
	public void suggest(Map<String, Object> map, final OnSucceedListener<JsonResult> listener) {
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LogUtils.i(arg0.result);
				Gson gson = new Gson();
				try {
					JsonResult jsonResult = null;
					jsonResult = gson.fromJson(arg0.result, JsonWeixinPay.class);
					if (jsonResult.getError().equals("0")) {
						LogUtils.i("请求成功");
						if (listener!=null){
							listener.onResult(true,jsonResult);
						}
					} else {
						LogUtils.i("请求失败"+jsonResult.getMessage());
						if (listener!=null){
							listener.onResult(false,null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtils.i("返回数据问题");
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.i("网络错误");
			}
			@Override
			public void onStart() {
				super.onStart();
				LogUtils.i("请求路径"+this.getRequestUrl());
			}
		};
		realHttpConnect(map, USER_URL, callBack);// 调用真正的网络方法
	}


	/**
	 * 调用xutils的post方法
	 *
	 * @param map
	 *            不能是final类型(会调用entrySet()方法)
	 * @param url
	 * @param callBack
	 */
	private void realHttpConnect(Map<String, Object> map, String url, RequestCallBack<String> callBack) {
		HttpUtils http = new HttpUtils();
		HttpMethod method = HttpMethod.POST;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Entry<String, Object> entry : map.entrySet()) {
			NameValuePair pars = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
			list.add(pars);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter(list);
		http.send(method, url, params, callBack);
	}

}
