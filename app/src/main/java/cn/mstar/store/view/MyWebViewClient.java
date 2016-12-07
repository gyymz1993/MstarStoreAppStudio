package cn.mstar.store.view;


import android.app.Activity;
import android.util.DisplayMetrics;

public class MyWebViewClient {

    public static int width; // 屏幕宽度（像素）
    public static int height ;  // 屏幕高度（像素）
    public static float density ;  // 屏幕密度（0.75 / 1.0 / 1.5）
    public static int densityDp;

    /**
     * 获取设备的屏幕信息
     * @param activity
     * @return
     */
    public static void  getDevicesPix(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        width = metric.widthPixels;  // 屏幕宽度（像素）
        height = metric.heightPixels;  // 屏幕高度（像素）
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        densityDp = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
    }

}