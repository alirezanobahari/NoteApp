package com.individual.noteapp.models;


import com.orm.SugarRecord;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Blackout on 1/26/2017.
 */

public class Note extends SugarRecord implements Serializable {


    private String title;
    private String note;
    private Date noteDate;
    private Long folderId;
    private boolean isFavorite;
    private boolean isVisual;

    public Note() {
    }


    public Note(String title, String note, Date noteDate, Long folderId, boolean isFavorite, boolean isVisual) {
        this.title = title;
        this.note = note;
        this.noteDate = noteDate;
        this.folderId = folderId;
        this.isFavorite = isFavorite;
        this.isVisual = isVisual;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isVisual() {
        return isVisual;
    }

    public void setVisual(boolean visual) {
        isVisual = visual;
    }


}
