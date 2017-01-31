package com.individual.noteapp.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by Blackout on 1/27/2017.
 */

public class Constants {

    // region intent constants
    public static final String NOTE_ID = "noteId";
    public static final String NOTE_POSITION = "notePosition";
    public static final String IS_IN_EDIT = "isInEdit";
    public static final int INTENT_NOTE = 1;
    public static final int INTENT_FILE_PICK = 22;

    public static final Long HOME_FOLDER_ID = Long.valueOf(Integer.MAX_VALUE);
    //endregion

    // region file constants
    public static final String noteName = "note_";
    public static final String BACKUP_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "NoteApp/";
    public static final String fileName = "note_backup_";
    public static final String fileSuffix = ".note";
    //endregion

    // region db constants
    public static final String FOLDER_ID = "FOLDER_ID";
    //endregion

    //region dateFormat constrants
    public static final String NORMAL_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public static final String FILE_DATE_FORMAT = "MM_dd_yyyy_HH_mm_ss";
    //endregion



}
