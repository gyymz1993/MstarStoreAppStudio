package cn.mstar.store.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class TextViewWithLine extends TextView{
    public TextViewWithLine(Context context) {
        super(context);
    }

    public TextViewWithLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = getPaint();
        paint.setAntiAlias(true);
        paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        super.onDraw(canvas);
    }
}
