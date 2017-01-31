package com.individual.noteapp.controller;

import com.individual.noteapp.models.Folder;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by Blackout on 1/28/2017.
 */

public class FolderController extends BaseController<Folder> {

    public static FolderController getInstance() {
        return new FolderController();
    }

    private FolderController() {}

    public List<Folder> getAllFolders() {
        return Select.from(Folder.class).list();
    }

    public Folder getFolderById(Long folderId) {
        return Folder.findById(Folder.class,folderId);
    }
}
