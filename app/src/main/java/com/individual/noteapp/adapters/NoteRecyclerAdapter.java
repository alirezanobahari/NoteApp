package com.individual.noteapp.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.individual.noteapp.R;
import com.individual.noteapp.abstracts.AbstractRecyclerAdapter;
import com.individual.noteapp.common.Constants;
import com.individual.noteapp.models.Note;
import com.individual.noteapp.utils.AppUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Blackout on 1/26/2017.
 */

public class NoteRecyclerAdapter extends AbstractRecyclerAdapter<Note,NoteRecyclerAdapter.ObjectHolder> {

    private static NoteClickListener noteClickListener;
    @LayoutRes private int layoutRes;

    private Context mContext;

    public static class ObjectHolder extends RecyclerView.ViewHolder
             {

        TextView txtNoteTitle,txtNote,txtNoteDate,txtNoteTime;
        ImageView imgNote,imgFavorite;

        public ObjectHolder(View itemView) {
            super(itemView);

            txtNote = (TextView) itemView.findViewById(R.id.txtNote);
            txtNoteTitle = (TextView) itemView.findViewById(R.id.txtNoteTitle);
            txtNoteDate = (TextView) itemView.findViewById(R.id.txtNoteDate);
            txtNoteTime = (TextView) itemView.findViewById(R.id.txtNoteTime);
            imgNote = (ImageView) itemView.findViewById(R.id.imgNote);
            imgFavorite = (ImageView) itemView.findViewById(R.id.imgFavorite);

        }

    }

    public NoteRecyclerAdapter(List<Note> mDataset , Context context, @LayoutRes int layoutRes, NoteClickListener clickListener) {
        this.setData(mDataset);
        this.mContext = context;
        noteClickListener = clickListener;
        this.layoutRes = layoutRes;
    }

    @Override
    public ObjectHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutRes, parent, false);

        ObjectHolder dataObjectHolder = new ObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(ObjectHolder holder, final int position) {

        final Note note = getItem(position);

        if(note.isVisual()) {
            holder.txtNote.setText("");
            holder.imgNote.setImageBitmap(AppUtils.loadVisualNote(mContext,note.getNote(),false));
        }
        else {
            holder.imgNote.setImageResource(R.drawable.back_of_note);
            if(note.getNote().length() > 200)
            holder.txtNote.setText(Html.fromHtml(note.getNote().substring(0,200)+"..."));
            else
                holder.txtNote.setText(Html.fromHtml(note.getNote()));
        }

        if(note.isFavorite()) {
            holder.imgFavorite.setImageResource(R.drawable.ic_heart);
        }
        else {
            holder.imgFavorite.setImageResource(R.drawable.ic_heart_border);
        }

        holder.txtNoteTitle.setText(note.getTitle());

        String date = AppUtils.getDateFormat(note.getNoteDate(), Constants.NORMAL_DATE_FORMAT);

        holder.txtNoteDate.setText(date.split(" ")[0]);
        holder.txtNoteTime.setText(date.split(" ")[1]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteClickListener.onNoteItemClick(position,note);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                noteClickListener.onLongNoteItemClick(position,note);
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

    public interface NoteClickListener {
        void onNoteItemClick(int position, Note note);
        void onLongNoteItemClick(int position , Note note);
    }

}

