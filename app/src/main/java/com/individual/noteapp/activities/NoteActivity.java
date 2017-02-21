package com.individual.noteapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import android.support.design.widget.AppBarLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.individual.noteapp.R;
import com.individual.noteapp.abstracts.BaseActivity;
import com.individual.noteapp.common.Constants;
import com.individual.noteapp.controller.NoteController;
import com.individual.noteapp.controls.CustomEditText;
import com.individual.noteapp.models.Note;
import com.individual.noteapp.utils.AppUtils;
import com.individual.noteapp.utils.ResizeAnimation;
import com.individual.noteapp.utils.UiUtils;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Date;

/**
 * Created by Blackout on 1/25/2017.
 */

public class NoteActivity extends BaseActivity {


    private CustomEditText txtNote;
    private EditText txtNoteTitle;
    private ImageView btnBold, btnUnderLine, btnItalic, btnTextColor;
    private AppBarLayout appBar;
    private boolean isMenuOpen = false;
    private Long noteId = null;
    private Long folderId = null;
    private int notePosition;
    private Note note = null;

    private NoteController noteController = NoteController.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initViews();
        loadData();


    }

    private void initViews() {
        initializeToolbar();

        findViewById(R.id.containerLayout).setOnClickListener(this);

        btnBold = (ImageView) findViewById(R.id.btnBold);
        btnUnderLine = (ImageView) findViewById(R.id.btnUnderLine);
        btnItalic = (ImageView) findViewById(R.id.btnItalic);
        btnTextColor = (ImageView) findViewById(R.id.btnTextColor);

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        txtNoteTitle = (EditText) findViewById(R.id.txtNoteTitle);
        txtNote = (CustomEditText) findViewById(R.id.txtNote);

        btnBold.setOnClickListener(this);
        btnUnderLine.setOnClickListener(this);
        btnItalic.setOnClickListener(this);
        btnTextColor.setOnClickListener(this);

    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            folderId = bundle.getLong(Constants.FOLDER_ID);
            noteId = bundle.getLong(Constants.NOTE_ID);
            notePosition = bundle.getInt(Constants.NOTE_POSITION);
            if(noteId != null) {
                note = noteController.getNoteById(noteId);
                if (note != null) {
                    txtNoteTitle.setText(note.getTitle());
                    txtNote.setText(Html.fromHtml(note.getNote()));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                saveNote();
            }
            break;
            case R.id.action_cancel: {
                cancel();
            }
            break;
            case R.id.action_text_style: {
                openCloseStyleMenu();
            }
            break;
        }
        return true;
    }

    private boolean validate() {
        boolean titleValidation = true;
        boolean noteValidation = true;

        if (TextUtils.isEmpty(txtNoteTitle.getText().toString())) {
            titleValidation = false;
            txtNoteTitle.setError(getResources().getString(R.string.titleNeeded));
        }
        if (TextUtils.isEmpty(txtNote.getText().toString())) {
            noteValidation = false;
            txtNote.setError(getResources().getString(R.string.enterNote));
        }

        if (titleValidation && noteValidation)
            return true;
        else return false;
    }

    private void saveNote() {
        if (validate()) {
            Intent returnIntent = new Intent();
            if (this.note == null) {
                Long noteId = noteController.save(new Note(txtNoteTitle.getText().toString(), txtNote.getFinalText(), new Date(), folderId, false, false));
                returnIntent.putExtra(Constants.NOTE_ID, noteId);
                returnIntent.putExtra(Constants.IS_IN_EDIT, false);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                // edit mode
                note.setTitle(txtNoteTitle.getText().toString());
                note.setNote(txtNote.getFinalText());
                note.setNoteDate(new Date());
                Long noteId = noteController.update(note);
                returnIntent.putExtra(Constants.NOTE_ID, noteId);
                returnIntent.putExtra(Constants.NOTE_POSITION, notePosition);
                returnIntent.putExtra(Constants.IS_IN_EDIT, true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void openCloseStyleMenu() {
        if (isMenuOpen) {
            appBar.startAnimation(new ResizeAnimation(appBar, appBar.getWidth(),
                    appBar.getHeight(), appBar.getWidth(), getAppBarSize(), 300));
            isMenuOpen = false;
        } else {
            appBar.startAnimation(new ResizeAnimation(appBar, appBar.getWidth(), appBar.getHeight(),
                    appBar.getWidth(), getAppBarSize() + UiUtils.convertDpToPixel(60, this), 300));
            isMenuOpen = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.containerLayout: {
                AppUtils.showKeyboard(txtNote, this);
                txtNote.requestFocus();
            }
            break;
            case R.id.btnBold: {
                if (!txtNote.changeTextStyle(CustomEditText.TextStyle.BOLD_STYLE)) {
                    Toast.makeText(this, getResources().getString(R.string.selectTextError), Toast.LENGTH_LONG).show();
                    AppUtils.vibrate(200, this);
                }
            }
            break;
            case R.id.btnItalic: {
                if (!txtNote.changeTextStyle(CustomEditText.TextStyle.ITALIC_STYLE)) {
                    Toast.makeText(this, getResources().getString(R.string.selectTextError), Toast.LENGTH_LONG).show();
                    AppUtils.vibrate(200, this);
                }
            }
            break;
            case R.id.btnUnderLine: {
                if (!txtNote.changeTextStyle(CustomEditText.TextStyle.UNDER_LINE_STYLE)) {
                    Toast.makeText(this, getResources().getString(R.string.selectTextError), Toast.LENGTH_LONG).show();
                    AppUtils.vibrate(200, this);
                }
            }
            break;
        }
    }


}
