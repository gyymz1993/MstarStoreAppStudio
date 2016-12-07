package cn.mstar.store.functionutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import cn.mstar.store.callback.OnSucceedListener;
import cn.mstar.store.entity.JsonResult;

/**
 * 与服务器交互的类
 *
 */
public class HttpRequestUtil {
	private Map<String, Integer> map;
	private String USER_URL;

	public HttpRequestUtil(String url, Map<String, Integer> map) {
		this.USER_URL = url;
		this.map = map;
	}

	private void sendHandleJsonResult(String result, OnSucceedListener<JsonResult> listener) {
		try {
			// 分发模块处理
			Gson gson = new Gson();
			JsonResult jsonResult = null;
			switch (map.get("code")) {
			case 1:
				jsonResult = gson.fromJson(result, JsonResult.class);// 实例化对象与json文件key对应
				break;
			default:
				break;
			}
			if (listener != null) {
				if (jsonResult.getError().equals("0")) {
					LogUtils.i("请求成功");
					listener.onResult(true, jsonResult);
				}else{
					LogUtils.i("参数错误" + jsonResult.getMessage());
					listener.onResult(false, jsonResult);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.i("返回数据问题");
		}
	}

	public void suggest(final Map<String, Object> map, final OnSucceedListener<JsonResult> listener) {// 登陆
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LogUtils.i(arg0.result);
				// 分发模块处理
				String result=arg0.result.replace("\\", "");//去掉'/'
				result=result.substring(1, result.length()-1);
				sendHandleJsonResult(arg0.result, listener);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.i("网络错误");
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtils.i("请求路径" + this.getRequestUrl());
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
		for(int i=0;i<list.size();i++){
			LogUtils.e("上传参数"+list.get(i));
		}
		params.addBodyParameter(list);
		http.send(method, url, params, callBack);
	}

}
