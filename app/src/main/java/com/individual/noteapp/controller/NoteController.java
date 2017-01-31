package com.individual.noteapp.controller;

import android.content.Context;

import com.individual.noteapp.application.AppController;
import com.individual.noteapp.common.Constants;
import com.individual.noteapp.models.Image;
import com.individual.noteapp.models.Note;
import com.individual.noteapp.utils.AppUtils;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blackout on 1/28/2017.
 */

public class NoteController extends BaseController<Note> {


    public static NoteController getInstance() {
        return new NoteController();
    }

    private NoteController() {}

    public boolean deleteNoteById(Long noteId) {
        try {
            return Note.findById(Note.class,noteId).delete();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllNotesByFolderId(Long folderId) {

        List<Note> notes = Select.from(Note.class).where(Condition.prop(Constants.FOLDER_ID).eq(folderId)).list();
        if(notes.size() > 0) {
            for (Note note : notes) {
                if(note.isVisual()) {
                    AppUtils.deleteNoteVisualFile(AppController.getContext(), note.getNote());
                }
                note.delete();
            }
            return true;
        } else
            return false;
    }

    public Note getNoteById(Long noteId) {
        return Note.findById(Note.class,noteId);
    }

    public List<Note> getAllNotes() {
        return Select.from(Note.class).list();
    }

    public List<Note> getNotesByFolderId(Long folderId) {
        return Select.from(Note.class).where(Condition.prop(Constants.FOLDER_ID).eq(folderId)).list();
    }

    public List<Image> getAllImagesByNotes(Context context, List<Note> notes) {
        List<Image> images = new ArrayList<>();
        for(Note note : notes) {
            if(note.isVisual()) {
                images.add(AppUtils.getImageByFileName(context, note.getNote()).setNoteName(note.getNote()));
            }
        }
        return images;
    }

    @Override
    public boolean delete(Note note) {
        if(note.isVisual()) {
            AppUtils.deleteNoteVisualFile(AppController.getContext(), note.getNote());
        }
        return super.delete(note);
    }
}
