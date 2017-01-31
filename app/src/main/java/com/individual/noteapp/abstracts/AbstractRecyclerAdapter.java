package com.individual.noteapp.abstracts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blackout on 1/27/2017.
 */

public abstract class AbstractRecyclerAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mDataSet;

    public void setAnimation(View viewToAnimate) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(300);
        viewToAnimate.startAnimation(alphaAnimation);
    }

    public int getItemCount() {
        return mDataSet.size();
    }

    public T getItem(int position) {
        return mDataSet.get(position);
    }

    public void setData(List<T> mDataSet) {
        this.mDataSet = mDataSet;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mDataSet;
    }

    public void changeItem(T item, int index) {
        mDataSet.set(index,item);
        notifyItemChanged(index);
    }

    public void insertItem(T item , int index) {
        mDataSet.add(index, item);
        notifyItemInserted(index);
    }

    public void removeItem(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }
}
