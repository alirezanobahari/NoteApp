package com.individual.noteapp.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Blackout on 1/26/2017.
 */

public class ExportModel implements Serializable {

    private List<ExportFolder> exportFolders;
    private List<Note> notes;
    private List<Image> images;


    public ExportModel(List<ExportFolder> exportFolders, List<Note> notes, List<Image> images) {
        this.exportFolders = exportFolders;
        this.images = images;
        this.notes = notes;
    }

    public List<ExportFolder> getExportFolders() {
        return exportFolders;
    }

    public void setExportFolders(List<ExportFolder> exportFolders) {
        this.exportFolders = exportFolders;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public static class ExportFolder implements Serializable {
        private Long lastId;
        private Long newId;
        private String folderName;

        public ExportFolder(String folderName, Long lastId, Long newId) {
            this.folderName = folderName;
            this.lastId = lastId;
            this.newId = newId;
        }

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public Long getLastId() {
            return lastId;
        }

        public void setLastId(Long lastId) {
            this.lastId = lastId;
        }

        public Long getNewId() {
            return newId;
        }

        public void setNewId(Long newId) {
            this.newId = newId;
        }
    }
}
