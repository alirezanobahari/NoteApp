package com.individual.noteapp.models;

import java.io.Serializable;

/**
 * Created by Blackout on 1/27/2017.
 */

public class Image implements Serializable{

    private String noteName;
    private String base64Image;

    public Image(String noteName, String base64Image) {
        this.noteName = noteName;
        this.base64Image = base64Image;
    }

    public Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getNoteName() {
        return noteName;
    }

    public Image setNoteName(String noteName) {
        this.noteName = noteName;
        return this;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
