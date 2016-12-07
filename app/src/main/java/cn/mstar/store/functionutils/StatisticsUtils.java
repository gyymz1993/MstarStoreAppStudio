package cn.mstar.store.functionutils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by yangshao on 2015/10/13.
 *
 * 友盟统计页面
 */
public class StatisticsUtils {

    private static StatisticsUtils statisticsUtils;
    public static  StatisticsUtils getInstance(){
        if(statisticsUtils==null){
            statisticsUtils=new StatisticsUtils();
        }
        return statisticsUtils;
    }
   private StatisticsUtils(){

    }

    public void init(){
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // 当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)，
        // 例如用户回到home，或进入其他程序，经过一段时间后再返回之前的应用。可通过接口：
        // obclickAgent.setSessionContinueMillis(long interval)
        // 来自定义这个间隔（参数单位为毫秒）。
        // MobclickAgent.setSessionContinueMillis(1000);
    }

   // MobclickAgent.onResume() 和MobclickAgent.onPause()
    //方法是用来统计应用时长的(也就是Session时长,当然还包括一些其他功能)
    //MobclickAgent.onPageStart()
    //和 MobclickAgent.onPageEnd() 方法是用来统计页面跳转的
    //调用 MobclickAgent.openActivityDurationTrack(false)
    // 禁止默认的页面统计方式，这样将不会再自动统计Activity。
    public void onResume(Context mContext,String mPageName) {
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
    }

    public void onPause(Context mContext,String mPageName) {
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }

    public void otherMethod(){
        // 用户登录
        MobclickAgent.onProfileSignIn("example_id");
        // 用户退出
        MobclickAgent.onProfileSignOff();
    }

}
