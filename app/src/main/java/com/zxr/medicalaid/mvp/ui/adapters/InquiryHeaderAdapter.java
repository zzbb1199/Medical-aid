package com.zxr.medicalaid.mvp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxr.medicalaid.R;
import com.zxr.medicalaid.widget.StickyHeaderDecoration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 猿人 on 2017/6/1.
 */

public class InquiryHeaderAdapter implements StickyHeaderDecoration.IStickyHeaderAdapter<InquiryHeaderAdapter.HeaderHolder> {

    private LayoutInflater mInflater;
    private InquiryContentAdapter adapter;
    

    public InquiryHeaderAdapter(Context context, InquiryContentAdapter adapter) {
        mInflater = LayoutInflater.from(context);
        this.adapter = adapter;
    }

    //利用map表来进行id设置
    private Map<String,Integer> map = new HashMap<>();
    @Override
    public long getHeaderId(int position) {
        if (position == 0){
            map.put(adapter.getItem(position).getTime(),position);
            return position;
        }
        //如果没有包含该位置的时间
        if (!map.containsKey(adapter.getItem(position).getTime())){
            map.put(adapter.getItem(position).getTime(),position);
            return position;
        }
        //包含了该位置的时间
        return  map.get(adapter.getItem(position).getTime());
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.header_item, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        viewholder.header.setText(adapter.getItem((int) getHeaderId(position)).getTime());
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView;
        }
    }
}
