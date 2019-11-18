package com.heinhtetoo.myclass.adapters;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.heinhtetoo.myclass.views.viewholders.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aung on 6/13/17.
 */

public abstract class BaseRecyclerAdapter<T extends BaseViewHolder, W> extends RecyclerView.Adapter<T> {

    protected LayoutInflater mLayoutInflater;

    protected List<W> mData = new ArrayList<>();

    public BaseRecyclerAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setNewData(List<W> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public void appendNewData(List<W> newData) {
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public W getItemAt(int position) {
        if (position < mData.size() - 1)
            return mData.get(position);

        return null;
    }

    public List<W> getItems() {
        if (mData == null)
            return new ArrayList<>();

        return mData;
    }

    public void removeData(W data) {
        mData.remove(data);
        notifyDataSetChanged();
    }

    public void addNewData(W data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    // remove data at position
    public void removeDataAt(int position) {
        mData.remove(position);
    }

    public void clearData() {
        mData = new ArrayList<>();
        notifyDataSetChanged();
    }
}
