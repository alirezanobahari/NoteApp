package com.individual.noteapp.controls;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.individual.noteapp.R;
import com.individual.noteapp.adapters.FolderRecyclerAdapter;
import com.individual.noteapp.models.Folder;

import java.util.List;

/**
 * Created by Blackout on 1/29/2017.
 */

public class AddToFolderDialog extends Dialog implements View.OnClickListener , FolderRecyclerAdapter.FolderClickListener {

    private RecyclerView lstFoldersToAdd;
    private RelativeLayout btnCancel;
    private List<Folder> folders;
    private TextView txtNoFolderFound;
    private FolderRecyclerAdapter folderAdapter;
    private AddToFolderDialogInterface dialogInterface;

    public static AddToFolderDialog getInstance(Context context, List<Folder> folders, AddToFolderDialogInterface dialogInterface) {
        return new AddToFolderDialog(context, folders, dialogInterface);
    }

    private AddToFolderDialog(Context context, List<Folder> folders, AddToFolderDialogInterface dialogInterface) {
        super(context, R.style.DialogNoTitleTheme);
        this.folders = folders;
        this.dialogInterface = dialogInterface;
        init(context);
    }

    private AddToFolderDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private AddToFolderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.layout_add_to_folder_dialog);

        lstFoldersToAdd = (RecyclerView) findViewById(R.id.lstFoldersToAdd);
        btnCancel = (RelativeLayout) findViewById(R.id.btnCancel);
        txtNoFolderFound = (TextView) findViewById(R.id.txtNoFolderFound);

        if(folders.size() == 0 )
            txtNoFolderFound.setVisibility(View.VISIBLE);

        folderAdapter = new FolderRecyclerAdapter(folders, context, this);
        lstFoldersToAdd.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        lstFoldersToAdd.setAdapter(folderAdapter);

        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel: {
                dialogInterface.onCancel(this);
            }
            break;
        }
    }

    @Override
    public void onFolderItemClick(int position, Folder folder) {
        dialogInterface.onSelect(folder.getId(), this);
    }

    @Override
    public void onLongFolderItemClick(int position, Folder folder) {

    }

    public interface AddToFolderDialogInterface {
        void onSelect(Long folderId, Dialog dialog);
        void onCancel(Dialog dialog);
    }


}
