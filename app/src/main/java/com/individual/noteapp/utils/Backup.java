package com.individual.noteapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.widget.ListView;

import com.individual.noteapp.R;
import com.individual.noteapp.activities.MainActivity;
import com.individual.noteapp.common.Constants;
import com.individual.noteapp.controller.FolderController;
import com.individual.noteapp.controller.NoteController;
import com.individual.noteapp.models.ExportModel;
import com.individual.noteapp.models.Folder;
import com.individual.noteapp.models.Image;
import com.individual.noteapp.models.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.individual.noteapp.common.Constants.HOME_FOLDER_ID;
import static com.individual.noteapp.common.Constants.fileName;

/**
 * Created by nobahari on 1/31/2017.
 */

public class Backup {

    private Context context;
    private NoteController noteController;
    private FolderController folderController;
    private CoordinatorLayout coordinatorContainer;


    public static Backup getInstance(Context context, CoordinatorLayout coordinatorContainer, NoteController noteController, FolderController folderController) {
        return new Backup(context, coordinatorContainer, noteController, folderController);
    }

    private Backup(Context context, CoordinatorLayout coordinatorContainer, NoteController noteController, FolderController folderController) {
        this.context = context;
        this.noteController = noteController;
        this.folderController = folderController;
        this.coordinatorContainer = coordinatorContainer;
    }

    public void exportNotes() {
        new ExportAsync().execute();
    }

    public void importNotes(String path) {
        new ImportAsync().execute(path);
    }

    private boolean export(String fileName) {
        List<Note> notes = noteController.getAllNotes();
        List<Folder> folders = folderController.getAllFolders();
        List<ExportModel.ExportFolder> exportFolders = new ArrayList<>();
        if(folders.size() > 0) {
            for (Folder folder: folders) {
                exportFolders.add(new ExportModel.ExportFolder(folder.getFolderName(),folder.getId(),Constants.HOME_FOLDER_ID));
            }
        }
        return AppUtils.exportNotes(new ExportModel(exportFolders, notes, noteController.getAllImagesByNotes(this.context, notes)),fileName);
    }

    private boolean _import(String path) {
        ExportModel exportModel = AppUtils.getExportModelFromPath(path);
        if(exportModel != null) {
            try {
                List<ExportModel.ExportFolder> exportFolders = exportModel.getExportFolders();
                List<Note> notes = exportModel.getNotes();
                List<Image> images = exportModel.getImages();
                if(exportFolders.size() > 0) {
                    for (ExportModel.ExportFolder exportFolder : exportFolders) {
                        Folder folder = new Folder(exportFolder.getFolderName());
                        exportFolder.setNewId(folderController.save(folder));
                    }
                }
                if (notes.size() > 0) {
                    for (Note note : notes) {
                        if(exportFolders.size() > 0) {
                            for (ExportModel.ExportFolder exportFolder : exportFolders) {
                                if (note.getFolderId() == exportFolder.getLastId()) {
                                    note.setFolderId(exportFolder.getNewId());
                                }
                            }
                        } else {
                            note.setFolderId(HOME_FOLDER_ID);
                        }
                        noteController.save(note);
                    }
                }
                if (images.size() > 0) {
                    for (Image image : images) {
                        AppUtils.saveVisualNote(this.context, AppUtils.getBitmapFromBase64String(image.getBase64Image()), image.getNoteName());
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        } else return false;
    }

    private class ExportAsync extends ModernAsyncTask<Void, Boolean, Boolean> {

        ProgressDialog loading;
        String exportName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exportName = fileName + AppUtils.getDateFormat(new Date(), Constants.FILE_DATE_FORMAT);
            loading = AppUtils.showProgressDialog(Backup.this.context, Backup.this.context.getResources().getString(R.string.expoting));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return export(exportName);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            loading.dismiss();
            if (aBoolean) {
                AppUtils.showSnackBar(coordinatorContainer, Backup.this.context.getResources().getString(R.string.exportedSuccessfully) +
                        Constants.BACKUP_DIRECTORY + exportName);
            } else {
                AppUtils.showSnackBar(coordinatorContainer, Backup.this.context.getResources().getString(R.string.expotingUnSuccessful));
            }
        }
    }

    private class ImportAsync extends ModernAsyncTask<String, Boolean, Boolean> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = AppUtils.showProgressDialog(Backup.this.context, Backup.this.context.getResources().getString(R.string.importingNotes));
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return _import(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            loading.dismiss();
            if (aBoolean) {
                AppUtils.showSnackBar(coordinatorContainer, Backup.this.context.getResources().getString(R.string.importingSuccessful));
                ((MainActivity) Backup.this.context).loadHome();
                ((MainActivity) Backup.this.context).loadFolders();
            } else {
                AppUtils.showSnackBar(coordinatorContainer, Backup.this.context.getResources().getString(R.string.importingUnsuccessful));
            }
        }
    }

}
