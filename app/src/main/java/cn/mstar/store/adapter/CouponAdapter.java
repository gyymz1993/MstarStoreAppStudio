package cn.mstar.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.activity.StoreDetailsActivity;
import cn.mstar.store.entity.CouponEntity;

/**
 * Created by wcl_ld at Shenzhen.
 */
public class CouponAdapter extends BaseAdapter {

    private Context mContext;
    private List<CouponEntity> mData;

    public CouponAdapter(Context mContext, List<CouponEntity> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void changeData(List<CouponEntity> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CouponEntity getItem(int position) {
        return (CouponEntity) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_wcl, parent, false);
            vh.coupon_bg = (ImageView) convertView.findViewById(R.id.coupon_item_bg);
            vh.coupon_num = (TextView) convertView.findViewById(R.id.coupon_num);
            vh.coupon_com = (TextView) convertView.findViewById(R.id.coupon_com_name);
            vh.coupon_condition = (TextView) convertView.findViewById(R.id.coupon_condition);
            vh.coupon_use_range = (TextView) convertView.findViewById(R.id.use_range);
            vh.coupon_use_range_valve = (TextView) convertView.findViewById(R.id.coupon_use_range);
            vh.coupon_use_flag = (TextView) convertView.findViewById(R.id.coupon_use_flag);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        int state = getItem(position).getVoucherState();
        int bg_pic = 0;  //背景图片
        int range_color = 0; //'使用期限'文字颜色
        int range_value_color = 0; //'2015-07-01~2015-07-20'文字颜色
        int use_flag_color = 0;// '未使用'文字颜色
        int use_flag_txt = 0;
        if (state == 1){
            bg_pic = R.drawable.coupons_yellow;
            range_color = R.color.color_black_st;
            range_value_color = R.color.color_black_st;
            use_flag_color = R.color.color_black_st;
            use_flag_txt = R.string.coupon_no_use;
        }
        else{
            bg_pic = R.drawable.coupons_grey;
            range_color = R.color.color_grey_st;
            range_value_color = R.color.color_grey_st;
            use_flag_color = R.color.color_grey_st;
            switch (state) {
                case 3:
                    use_flag_txt = R.string.coupon_out_of_date;
                    break;
                case 2:
                    use_flag_txt = R.string.coupon_used;
                    break;
            }
        }
        /*if (state == 3) {
            bg_pic = R.drawable.coupons_grey;
            range_color = R.color.color_grey_st;
            range_value_color = R.color.color_grey_st;
            use_flag_color = R.color.color_grey_st;
            use_flag_txt = R.string.coupon_out_of_date;
        } else {
            bg_pic = R.drawable.coupons_yellow;
            range_color = R.color.color_black_st;
            range_value_color = R.color.color_black_st;
            use_flag_color = R.color.color_black_st;
            switch (state) {
                case 1:
                    use_flag_txt = R.string.coupon_no_use;
                    break;
                case 2:
                    use_flag_txt = R.string.coupon_used;
                    break;
            }
        }*/
        vh.coupon_bg.setImageResource(bg_pic);
        vh.coupon_use_range.setTextColor(mContext.getResources().getColor(range_color));
        vh.coupon_use_range_valve.setTextColor(mContext.getResources().getColor(range_value_color));
        vh.coupon_use_flag.setTextColor(mContext.getResources().getColor(use_flag_color));
        vh.coupon_use_flag.setText(mContext.getResources().getString(use_flag_txt));
        vh.coupon_num.setText(getItem(position).getVoucherPrice() + "");
        vh.coupon_com.setText(getItem(position).getStore_name());
        vh.coupon_condition.setText(getItem(position).getVoucherLimitMsg());
        vh.coupon_use_range_valve.setText(getItem(position).getValidDate());

        vh.coupon_condition.setTextSize(atuoChangeTextViewSize(vh.coupon_condition, 140, 16));
        vh.coupon_com.setTextSize(atuoChangeTextViewSize(vh.coupon_com, 140, 16));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*跳转到商铺*/
                Intent intent = new Intent(mContext,StoreDetailsActivity.class);
                intent.putExtra("shopid",getItem(position).getStoreId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * 自动改变字体大小
     * 
     * @param textView     要改变的TextView
     * @param limit        限制长度 单位dp
     * @param initTextSize 初始字体大小 单位sp
     * @return 返回字体大小
     */
    private float atuoChangeTextViewSize(TextView textView, int limit, float initTextSize) {
        Paint paint = new Paint();
        float length = 0;
        float textSize = initTextSize + 1;
        String content = textView.getText().toString();
        do {
            textSize = textSize - 1;
            paint.setTextSize(textSize);
            length = paint.measureText(content);
        } while (length > limit);
        return textSize;
    }

    class ViewHolder {
        ImageView coupon_bg;
        TextView coupon_num;
        TextView coupon_com;
        TextView coupon_condition;
        TextView coupon_use_range;
        TextView coupon_use_range_valve;
        TextView coupon_use_flag;
    }
}
