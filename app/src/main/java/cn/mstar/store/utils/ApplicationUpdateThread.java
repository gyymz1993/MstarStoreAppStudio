//package cn.mstar.store.utils;
//
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//
//import cn.mstar.store.app.MyApplication;
//import cn.mstar.store.entity.UpgradeItem;
//import cn.mstar.store.functionutils.LogUtils;
//import cn.mstar.store.functionutils.UpdateUtil;
//
///**
// * Created by UlrichAbiguime at Shenzhen.
// */
//public class ApplicationUpdateThread extends Thread implements Runnable {
//
//    MyApplication app;
//    private Gson gson;
//
//    //
//    public ApplicationUpdateThread (MyApplication app) {
//
//        this.app = app;
//        gson = new Gson();
//    }
//
//
//
//
//    @Override
//    public void run() {
//        super.run();
//        VolleyRequest.GetCookieRequest(app.getApplicationContext(), NewLink.CHECK_UPGRADE, new VolleyRequest.HttpStringRequsetCallBack() {
//
//            @Override
//            public void onSuccess(String result) {
//                L.d("update:::", result);
//                try {
//                    JsonObject elm = gson.fromJson(result, JsonElement.class).getAsJsonObject().get("data").getAsJsonObject();
//                    boolean ifUpdate = elm.get("ifupdate").getAsBoolean();
//                    UpgradeItem it = gson.fromJson(elm, UpgradeItem.class);
//                    LogUtils.i("服务器版本号"+Double.valueOf(it.version)+"--------"+"本地版本号"+ UpdateUtil.getVersionCode(app.getApplicationContext()));
//                  //  if (Double.valueOf(it.version) > UpdateUtil.getVersionCode(app.getApplicationContext())){
//                        if (UpdateUtil.verisonCompare(Double.valueOf(it.version),UpdateUtil.getVersionCode(app.getApplicationContext()))){
//                            MyApplication.needUpgrade = true;
//                            LogUtils.i("当前更新值"+MyApplication.needUpgrade);
//
//                            app.upgradeEntity = it;
//                            L.d("update:::", "needs update from " + cn.mstar.store.app.Constants.VERSION_NAME + " to " + it.version);
//                        }
//
//                 //   }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//            }
//        });
//    }
//}
