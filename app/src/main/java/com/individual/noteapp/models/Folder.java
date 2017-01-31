package com.individual.noteapp.models;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Blackout on 1/26/2017.
 */

public class Folder extends SugarRecord implements Serializable {

    private String folderName;

    public Folder() {
    }

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }


}
