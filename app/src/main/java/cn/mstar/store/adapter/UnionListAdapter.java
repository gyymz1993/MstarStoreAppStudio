package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.mstar.store.R;
import cn.mstar.store.entity.Union;
import cn.mstar.store.entity.Zhifubao;

/**
 * Created by Administrator on 2015/10/30.
 */
public class UnionListAdapter extends BaseAdapter{

    private Context context;
    private List<Union> list;
    private LayoutInflater inflater;
    public UnionListAdapter(Context context,List<Union> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size()+1;
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

        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_union_info, null);
            viewHolder.bank_cart_text=(TextView) convertView.findViewById(R.id.bank_cart_text);
            viewHolder.account_name_text=(TextView) convertView.findViewById(R.id.account_name_text);
            viewHolder.bank_name_text=(TextView)convertView.findViewById(R.id.bank_name_text);
            convertView.setTag(viewHolder);
        }else{

                viewHolder=(ViewHolder) convertView.getTag();

        }
        if (list.size()>= 1){
            if (position<=list.size()-1){
                Union union=list.get(position);
                viewHolder.bank_cart_text.setText(union.getBank_cart());
                viewHolder.account_name_text.setText(union.getAccount_name());
                viewHolder.bank_name_text.setText(union.getBank_name());
            }else {

                viewHolder.bank_name_text.setText("添加账号");
                viewHolder.account_name_text.setText("+");
                viewHolder.bank_cart_text.setText("");
            }

        }


        return convertView;
    }
    public class ViewHolder{
        TextView bank_cart_text,account_name_text,bank_name_text;
    }
}
