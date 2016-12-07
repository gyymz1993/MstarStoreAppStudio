package cn.mstar.store.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import cn.mstar.store.R;
import cn.mstar.store.app.MyApplication;
import cn.mstar.store.functionutils.SpUtils;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        /**
         * 设置为竖屏 1233
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
//	//设置沉浸式标题 把标题布局放进去
//    public void setImmerseLayout(View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Window window = getWindow();
//                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            int statusBarHeight = Screen.getStatusBarHeight(this.getBaseContext());
//            view.setPadding(0, statusBarHeihoght, 0, 0);
//        }
//    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 添加弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author wenjundu
     */
    class poponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            //Log.v("List_noteTypeActivity:", "我是关闭事件");  
            backgroundAlpha(1f);
        }
    }

    public interface IsLoginListener {
        public void OK(String result);

        public void No(String result);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.custom_in_anim, R.anim.custom_out_anim);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
