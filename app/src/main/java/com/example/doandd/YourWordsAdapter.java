package com.example.doandd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class YourWordsAdapter extends BaseAdapter {
    private ListItemListener listener;
    private ListItemListener listenerBtnDel;
    Context mContext;
    ArrayList<String> mSource;

    public YourWordsAdapter(Context context, ArrayList<String> source){
      this.mContext = context;
      this.mSource = source;
    }

    @Override
    public int getCount() {
        return mSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.yourwords_layout_items,parent,false);
            viewHolder.textView = convertView.findViewById(R.id.tvWord);
            viewHolder.btnDelete = convertView.findViewById(R.id.ivDel);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(mSource.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                listener.onItemClick(position);
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listenerBtnDel!=null)
                {
                    listenerBtnDel.onItemClick(position);
                }
            }
        });
        return convertView;
    }


    public void  removeItem(int position)
    {
        mSource.remove(position);
    }

    public void clear() {
        mSource.clear();

    }

    class ViewHolder {
        TextView textView;
        ImageView btnDelete;
    }

    public void setOnItemClick(ListItemListener listItemListener)
    {
        this.listener = listItemListener;
    }

    public void setOnItemDeleteClick(ListItemListener listItemListener)
    {
        this.listenerBtnDel = listItemListener;
    }

}
