package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.entity.StoreInfo;
import cn.mstar.store.entity.Zhifubao;
import cn.mstar.store.utils.ImageLoadOptions;

/**
 * Created by Administrator on 2015/10/30.
 */
public class ZhifubaoListAdapter extends BaseAdapter {


    private Context context;
    private List<Zhifubao> list;
    private LayoutInflater inflater;

    public ZhifubaoListAdapter(Context context, List<Zhifubao> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_zhifubao_info, null);
            viewHolder.bank_cart_text = (TextView) convertView.findViewById(R.id.bank_cart_text);
            viewHolder.account_name_text = (TextView) convertView.findViewById(R.id.account_name_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list.size() >= 1) {
            if (position <= list.size() - 1) {
                Zhifubao zhifubao = list.get(position);
                viewHolder.bank_cart_text.setText(zhifubao.getBank_cart());
                viewHolder.account_name_text.setText(zhifubao.getAccount_name());
            }
            else{

                viewHolder.account_name_text.setText("+");
                viewHolder.bank_cart_text.setText("添加账号");
            }
        }


        return convertView;
    }

    public class ViewHolder {
        TextView bank_cart_text, account_name_text;
    }
}
