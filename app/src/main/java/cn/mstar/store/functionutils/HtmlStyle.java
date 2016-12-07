package cn.mstar.store.functionutils;

import android.app.Activity;
import android.util.DisplayMetrics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Yangshao on 2015/10/15.
 */
public class HtmlStyle {
    /**
     * @action:设置Html样式
     * @author: YangShao
     * @date: 2015/10/19 @time: 10:53
     */
    public static String setHtmlStyle(Activity activity, String url) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDp = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        try {
            Document doc = Jsoup.parse(url);
            Elements elementp = doc.getElementsByTag("p");//获取所有p标
            elementp.attr("style", "text-align:center");  //设置P标签样式
            elementp.attr("style", elementp.attr("style").toString() + ("max-width:" + "100%"));  //在原有属性上添加一个属性
            Elements elementImgs = doc.getElementsByTag("img");//获取所有img标
            for (Element img : elementImgs) {
                String imageUrl = img.attr("src");
                if (imageUrl.endsWith(".gif")) {
                    img.removeAttr("src");
                    // img.removeAttr("width");
                    //img.removeAttr("style");
                    img.removeAttr("alt");
                } else {
                    elementImgs.attr("width", width + "px");//设置width属性  可让图片显示占满全屏
                    elementImgs.attr("style", "max-width:" + "100%");  //设置style属性  可让带样式的图片占满全屏
                }
            }
            //LogUtils.e("改变HTML样式后"+doc.toString());
            return doc.toString();
        } catch (Exception e) {
            //LogUtils.e("解析XML异常");
            return "";
        }

    }

}
