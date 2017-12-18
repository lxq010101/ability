package com.ustcinfo.mobile.platform.ability.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private SparseArray<View> views;

    private BaseAdapter.OnItemClickListener mOnItemClickListener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.mOnItemClickListener = onItemClickListener;
        this.views = new SparseArray<View>();
    }

    public TextView getTextView(int viewId) {
        return retrieveView(viewId);
    }

    public Button getButton(int viewId) {
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return retrieveView(viewId);
    }

    public View getView(int viewId) {
        return retrieveView(viewId);
    }


    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 给TextView设置文字
     *
     * @param viewId
     * @param text
     * @return ViewHolder
     */
    public BaseViewHolder setText(int viewId, String text) {
        TextView textView = retrieveView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * 给ImageView设置图像
     *
     * @param viewId
     * @return ViewHolder
     */
    public BaseViewHolder setImage(Context context, int viewId, Integer resourceId) {
        ImageView imageView = retrieveView(viewId);
        Glide.with(context).load(resourceId).into(imageView);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
