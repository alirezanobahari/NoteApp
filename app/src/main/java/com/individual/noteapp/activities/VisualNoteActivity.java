package com.individual.noteapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.individual.noteapp.R;
import com.individual.noteapp.abstracts.BaseActivity;
import com.individual.noteapp.adapters.PenSizeSpinnerAdapter;
import com.individual.noteapp.application.AppController;
import com.individual.noteapp.common.Constants;
import com.individual.noteapp.controller.NoteController;
import com.individual.noteapp.controls.DrawingLayout;
import com.individual.noteapp.controls.DrawingView;
import com.individual.noteapp.models.Note;
import com.individual.noteapp.utils.AppUtils;
import com.individual.noteapp.utils.ResizeAnimation;
import com.individual.noteapp.utils.UiUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.individual.noteapp.common.Constants.noteName;

/**
 * Created by Blackout on 1/25/2017.
 */

public class VisualNoteActivity extends BaseActivity implements ColorPicker.OnButtonListener, ColorPicker.OnFastChooseColorListener , AdapterView.OnItemSelectedListener {

    private ImageView imgPenColor, btnEraser;
    private DrawingLayout drawingLayout;
    public EditText txtNoteTitle;
    private Spinner spnPenSize;
    private AppBarLayout appBar;
    private boolean isMenuOpen = false;
    private Note note = null;
    private Long noteId = null;
    private Long folderId = null;
    private int notePosition;
    private int drawColor = -14654801;
    private List<Integer> penSizes = Arrays.asList(new Integer[] {(int)UiUtils.convertDpToPixel(1, AppController.getContext()),
            (int)UiUtils.convertDpToPixel(2, AppController.getContext()),
            (int)UiUtils.convertDpToPixel(3, AppController.getContext()),
            (int)UiUtils.convertDpToPixel(4, AppController.getContext()),
            (int)UiUtils.convertDpToPixel(5, AppController.getContext())});

    private NoteController noteController = NoteController.getInstance();

    final String set = "SET";
    final String cancel = "CANCEL";

    private ColorPicker picker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_note);

        initViews();
        loadData();
    }

    private void initViews() {
        initializeToolbar();

        spnPenSize = (Spinner) findViewById(R.id.spnPenSize);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        imgPenColor = (ImageView) findViewById(R.id.imgPenColor);
        drawingLayout = (DrawingLayout) findViewById(R.id.drawLayout);
        txtNoteTitle = (EditText) findViewById(R.id.txtNoteTitle);
        btnEraser = (ImageView) findViewById(R.id.btnEraser);

        imgPenColor.setOnClickListener(this);
        btnEraser.setOnClickListener(this);

        drawingLayout.setPainColor(drawColor);
        drawingLayout.setPenSize(DrawingView.DEFAULT_PEN_SIZE);

        spnPenSize.setAdapter(new PenSizeSpinnerAdapter(this, R.layout.pen_size_spinner_row, penSizes));

        spnPenSize.setOnItemSelectedListener(this);

        imgPenColor.setImageDrawable(UiUtils.createCircleDrawable(this, drawColor));

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
                    drawingLayout.setBackImage(AppUtils.loadVisualNote(VisualNoteActivity.this, note.getNote(), true));
                }
            }
        }
    }

    private void openCloseStyleMenu() {
        if (isMenuOpen) {
            appBar.startAnimation(new ResizeAnimation(appBar, appBar.getWidth(),
                    appBar.getHeight(), appBar.getWidth(), getAppBarSize(), 300));
            isMenuOpen = false;
        } else {
            appBar.startAnimation(new ResizeAnimation(appBar, appBar.getWidth(), appBar.getHeight(),
                    appBar.getWidth(), getAppBarSize() + UiUtils.convertDpToPixel(50, this), 300));
            isMenuOpen = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgPenColor: {
                picker = new ColorPicker(this).setOnFastChooseColorListener(this)
                        .disableDefaultButtons(true).setColumns(5).setRoundColorButton(true);
                picker.show();
            }
            break;
            case R.id.btnEraser: {
                if (drawingLayout.isEraserMode()) {
                    spnPenSize.setEnabled(true);
                    btnEraser.setColorFilter(getResources().getColor(R.color.secondary_text));
                    drawingLayout.setEraserMode(false);
                } else {
                    spnPenSize.setEnabled(false);
                    btnEraser.setColorFilter(Color.WHITE);
                    drawingLayout.setEraserMode(true);
                }
            }
            break;
        }
    }

    @Override
    public void onClick(View v, int position, int color) {
        switch (((Button) v).getText().toString()) {
            case set: {
                imgPenColor.setImageDrawable(UiUtils.createCircleDrawable(this, color));
                drawColor = color;
                picker.dismissDialog();
            }
            break;
            case cancel: {
                picker.dismissDialog();
            }
            break;
        }
    }

    @Override
    public void setOnFastChooseColorListener(int position, int color) {
        imgPenColor.setImageDrawable(UiUtils.createCircleDrawable(this, color));
        drawColor = color;
        drawingLayout.setPainColor(drawColor);
        picker.dismissDialog();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_visual_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                save();
            }
            break;
            case R.id.action_cancel: {
                cancel();
            }
            break;
            case R.id.action_edit_menu: {
                openCloseStyleMenu();
            }
            break;
        }
        return true;
    }

    private boolean validate() {
        boolean titleValidation = true;
        if (TextUtils.isEmpty(txtNoteTitle.getText().toString())) {
            titleValidation = false;
            txtNoteTitle.setError(getResources().getString(R.string.titleNeeded));
        }

        if (titleValidation)
            return true;
        else return false;
    }

    private void save() {
        if (validate()) {
            Intent returnIntent = new Intent();
            if (this.note == null) {
                String noteFileName = AppUtils.saveVisualNote(this, AppUtils.getBitmapFromView(drawingLayout), noteName + AppUtils.getDateFormat(new Date(), Constants.FILE_DATE_FORMAT));
                if (noteFileName != null) {
                    Long noteId = noteController.save(new Note(txtNoteTitle.getText().toString(), noteFileName,
                            new Date(), folderId, false, true));
                    returnIntent.putExtra(Constants.NOTE_ID, noteId);
                    returnIntent.putExtra(Constants.IS_IN_EDIT, false);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    // show error
                }
            } else {
                // edit mode
                String noteFileName = AppUtils.saveVisualNote(this, AppUtils.getBitmapFromView(drawingLayout), note.getNote());
                if (noteFileName != null) {
                    note.setTitle(txtNoteTitle.getText().toString());
                    note.setNote(noteFileName);
                    note.setNoteDate(new Date());
                    Long noteId = noteController.update(note);
                    returnIntent.putExtra(Constants.NOTE_ID, noteId);
                    returnIntent.putExtra(Constants.NOTE_POSITION, notePosition);
                    returnIntent.putExtra(Constants.IS_IN_EDIT, true);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    // show error
                }
            }
        }
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        drawingLayout.setPenSize(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
