package com.ustcinfo.mobile.platform.ability.common;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;



public abstract class StickyHeaderAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected ArrayList<T> items = new ArrayList<T>();

    public StickyHeaderAdapter() {
        setHasStableIds(true);
    }

    public void add(T object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, T object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(T... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(T object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        T t = null;
        try {
            t = items.get(position);

        }catch (IndexOutOfBoundsException e){
         String str =    e.getMessage();
        }
        return t;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
