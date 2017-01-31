package com.individual.noteapp.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.individual.noteapp.R;
import com.individual.noteapp.abstracts.AbstractRecyclerAdapter;
import com.individual.noteapp.models.Folder;

import java.util.List;

/**
 * Created by Blackout on 1/26/2017.
 */

public class FolderRecyclerAdapter extends AbstractRecyclerAdapter<Folder,FolderRecyclerAdapter.ObjectHolder> {

    private FolderClickListener folderClickListener;

    private Context mContext;

    public static class ObjectHolder extends RecyclerView.ViewHolder
    {

        TextView txtFolderName;

        public ObjectHolder(View itemView) {
            super(itemView);

            txtFolderName = (TextView) itemView.findViewById(R.id.txtFolderName);

        }

    }

    public FolderRecyclerAdapter(List<Folder> mDataset , Context context, FolderClickListener clickListener) {
        this.setData(mDataset);
        this.mContext = context;
        folderClickListener = clickListener;
    }

    @Override
    public ObjectHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_folder_item, parent, false);

        ObjectHolder dataObjectHolder = new ObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(ObjectHolder holder, final int position) {

        holder.txtFolderName.setText(getItem(position).getFolderName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folderClickListener.onFolderItemClick(position,getItem(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                folderClickListener.onLongFolderItemClick(position,getItem(position));
                return true;
            }
        });

        setAnimation(holder.itemView);
    }

    @Override
    public void onViewDetachedFromWindow(ObjectHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public interface FolderClickListener {
        void onFolderItemClick(int position, Folder folder);
        void onLongFolderItemClick(int position , Folder folder);
    }

}

