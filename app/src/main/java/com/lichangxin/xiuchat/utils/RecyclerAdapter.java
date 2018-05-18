package com.lichangxin.xiuchat.utils;

/**
 * RecyclerView 数据适配器
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private int layout;
    private int id;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecyclerAdapter(int layout, int id) {
        this.layout = layout;
        this.id = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnItemClickListener, id);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }
    @Override
    public int getItemCount() {
        return 0;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener mOnItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener mOnItemClickListener, int id) {
            super(itemView);

            this.mOnItemClickListener = mOnItemClickListener;

            if (id == 0) {
                return;
            }

            itemView = itemView.findViewById(id);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
