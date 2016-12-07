package cn.mstar.store.functionutils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.math.BigDecimal;

import cn.mstar.store.R;
import cn.mstar.store.callback.OnSucceedListener;
import cn.mstar.store.entity.Version;
import cn.mstar.store.utils.NewLink;
import cn.mstar.store.utils.VolleyRequest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

/**
 * @action:  检查更新的工具类
 * @author: YangShao
 * @date: 2015/10/30 @time: 11:03
 */
public class UpdateUtil {
    public static Version version;
    public static OnSucceedListener<Version> onResultListener;

    public static void setOnResultListener(
            OnSucceedListener<Version> monResultListener) {
        onResultListener = monResultListener;
    }

    public static void getVersion(final Context context) {
      //  LogUtils.i("自动更新。。。");
        VolleyRequest.GetCookieRequest(context, NewLink.UPDATE_APK, new VolleyRequest.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                if (result != null) try {
                    {
                        String error = (new Gson()).fromJson(result, JsonElement.class).getAsJsonObject().get("error").getAsString();
                        if (error.equals("0")) {
                            String VERSION_CODE = new Gson().fromJson(result, JsonObject.class).getAsJsonObject("data").get("version").getAsString();
                            String DOWN_APK_URL = new Gson().fromJson(result, JsonObject.class).getAsJsonObject("data").get("download").getAsString();
                            boolean ISUPDATE = new Gson().fromJson(result, JsonObject.class).getAsJsonObject("data").get("ifupdate").getAsBoolean();
                            DOWN_APK_URL ="http://www.fanershop.com/appdown/faner.apk";
                            version = new Version(DOWN_APK_URL, VERSION_CODE, ISUPDATE);
                            if (onResultListener != null) {
                                onResultListener.onResult(true, version);
                            }
                        } else {
                            String message = new Gson().fromJson(result, JsonObject.class).getAsJsonObject("message").getAsString();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String error) {

            }
        });
    }

    @SuppressWarnings("rawtypes")
    public static HttpHandler handler = null;
    private static HttpUtils http = new HttpUtils();
    public static void downLoadNewApk(final Context context, Version version) {

        /**
         * 点击取消停止下载
         */
        showDownloadDialog(context, new OnSucceedListener<Object>() {
            @Override
            public void onResult(boolean isSecceed, Object obj) {
               // handler.cancel();
               // Intent intent = new Intent(context, LoginActivity.class);
               // context.startActivity(intent);
            }
        });

        LogUtils.e("下载版本地址"+version.getDownloadurl());
        if (version != null) {
            handler = http.download(version.getDownloadurl(), mSavePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                    true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                    new RequestCallBack<File>() {
                        @Override
                        public void onStart() {
                            System.out.println("conn...");
                        }

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {
                            int x = (int) (current * 100 / (int) total);
                            System.out.println(x + "%");
                            textView.setText(x + "%");
                            System.out.println("onLoading" + current + "/"
                                    + total);
                            mProgress.setProgress(x);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            System.out.println("downloaded:"
                                    + responseInfo.result.getPath());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mDownloadDialog.dismiss();
                                    installApk(context);
                                }
                            }).start();
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            System.out.println("onFailure" + msg);
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }


    public static void onDestroy(){
        handler.cancel();
    }

    public static double getVersionCode(Context context) {
        double versionCode = 0;
        try {
            versionCode = Double.valueOf(context.getPackageManager().getPackageInfo(
                    "cn.mstar.store", 0).versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String mSavePath = "/sdcard/faner.apk";

    public static File getApkFile() {
        return new File(mSavePath);
    }

    public static void installApk(Context context) {
        if (!isExis()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + getApkFile().toString()),
                "application/vnd.android.package-archive");
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
        //getApkFile().delete();
    }

    public static boolean isExis() {
        File apkfile = new File(mSavePath);
       // LogUtils.e("下载文件位置。。。。" + mSavePath);
        if (apkfile.exists()) {
            return true;
        }
        return false;
    }

    /**
     *
     *  @action:
     *  @author:  YangShao  服务器 本地
     *  @date: 2015/11/9 @time: 12:10 
     */
    public static boolean verisonCompare(Double mreVersion, Double mloVersion) {
        BigDecimal reVersion = new BigDecimal(mreVersion);
        BigDecimal loVersion = new BigDecimal(mloVersion);
        boolean result = false;
        if (loVersion.compareTo(reVersion) < 0) {
        }
        if (loVersion.compareTo(reVersion) == 0) {
        }
        if (loVersion.compareTo(reVersion) > 0) {
            result = true;
        }
        return result;
    }


    /**
     * 显示软件下载对话框
     */
    public static ProgressBar mProgress;
    public static AlertDialog.Builder builder;
    public static Dialog mDownloadDialog;
    public static TextView textView;
    public static boolean cancelUpdate = false;

    public static void showDownloadDialog(Context mContext,
                                          final OnSucceedListener<Object> listener) {
        // 构造软件下载对话框
        builder = new Builder(mContext);
        builder.setTitle("软件更新");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        textView = (TextView) v.findViewById(R.id.up_text);
        builder.setView(v);
        builder.setCancelable(false);
        // 取消更新
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 设置取消状态
//                dialog.dismiss();
//                listener.onResult(cancelUpdate, null);
//            }
//        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
    }
}
