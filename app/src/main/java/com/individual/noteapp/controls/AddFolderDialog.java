package com.individual.noteapp.controls;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.individual.noteapp.R;

/**
 * Created by Blackout on 1/29/2017.
 */

public class AddFolderDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout btnCancel, btnAdd;
    private EditText txtFolderName;
    private boolean isRename;
    @Nullable private String folderName;
    private AddFolderDialogInterface dialogInterface;

    public static AddFolderDialog getInstance(Context context, boolean isRename, @Nullable String folderName, AddFolderDialogInterface dialogInterface) {
        return new AddFolderDialog(context, isRename, folderName, dialogInterface);
    }

    private AddFolderDialog(Context context, boolean isRename, @Nullable String folderName, AddFolderDialogInterface dialogInterface) {
        super(context, R.style.DialogNoTitleTheme);
        this.dialogInterface = dialogInterface;
        this.isRename = isRename;
        this.folderName = folderName;
        init(context);
    }

    private AddFolderDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private AddFolderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.layout_add_folder_dialog);

        btnAdd = (RelativeLayout) findViewById(R.id.btnAdd);
        btnCancel = (RelativeLayout) findViewById(R.id.btnCancel);
        txtFolderName = (EditText) findViewById(R.id.txtFolderName);

        if(isRename){
            txtFolderName.setText(folderName);
            ((TextView) findViewById(R.id.txtAdd)).setText(context.getResources().getString(R.string.rename));
        }

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd: {
                if(validation()) {
                    if(isRename) {
                        dialogInterface.onRename(txtFolderName.getText().toString(), this);
                    } else {
                        dialogInterface.onAdd(txtFolderName.getText().toString(), this);
                    }
                }
            }
            break;
            case R.id.btnCancel: {
                    dialogInterface.onCancel(this);
            }
            break;
        }
    }

    private boolean validation() {
        if (TextUtils.isEmpty(txtFolderName.getText())) {
            txtFolderName.setError(getContext().getResources().getString(R.string.folderNameNeeded));
            return false;
        } else
            return true;
    }

    public interface AddFolderDialogInterface {
        void onAdd(String folderName, Dialog dialog);
        void onRename(String folderName, Dialog dialog);
        void onCancel(Dialog dialog);
    }
}
