package cn.mstar.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.mstar.store.R;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class DetailsListAdapter extends BaseAdapter{

        Context context;
        String[] data;

        public DetailsListAdapter(Context context, String[] d) {
            this.context = context;
            data = d;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String s = (String) getItem(position);
            ViewHolder viewHolder;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView=LayoutInflater.from(context).inflate(R.layout.textview_item, null);
                viewHolder.tv1=(TextView) convertView.findViewById(R.id.tv1);
                viewHolder.tv2=(TextView) convertView.findViewById(R.id.tv2);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            String[] splitted = s.split(":");
            viewHolder.tv1.setText(splitted[0]);
            viewHolder.tv2.setText(splitted[1]);
            return convertView;
        }
    public class ViewHolder {
        TextView tv1, tv2;
    }
}
