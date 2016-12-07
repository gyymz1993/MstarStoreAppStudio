package cn.mstar.store.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by 1 on 2016/1/4.
 */
public class CheckedLinearlayout extends LinearLayout implements Checkable {

    private boolean mChecked;

    public CheckedLinearlayout(Context context) {
        super(context);
    }

    public CheckedLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CheckedLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != mChecked) {
            mChecked = checked;
            View view;
            for (int i = 0; i < getChildCount(); i++) {
                view = getChildAt(i);
                if (view instanceof Checkable) {
                    ((Checkable) view).setChecked(mChecked);
                }
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
