package com.individual.noteapp.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.individual.noteapp.R;
import com.individual.noteapp.abstracts.BaseActivity;
import com.individual.noteapp.adapters.FolderRecyclerAdapter;
import com.individual.noteapp.adapters.NoteRecyclerAdapter;
import com.individual.noteapp.common.AppSharePrefs;
import com.individual.noteapp.common.Constants;
import com.individual.noteapp.controller.FolderController;
import com.individual.noteapp.controller.NoteController;
import com.individual.noteapp.controls.AddFolderDialog;
import com.individual.noteapp.controls.AddToFolderDialog;
import com.individual.noteapp.models.ExportModel;
import com.individual.noteapp.models.Folder;
import com.individual.noteapp.models.Image;
import com.individual.noteapp.models.Note;
import com.individual.noteapp.utils.AppUtils;
import com.individual.noteapp.utils.Backup;
import com.individual.noteapp.utils.ModernAsyncTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


import static com.individual.noteapp.common.Constants.HOME_FOLDER_ID;

public class MainActivity extends BaseActivity implements NoteRecyclerAdapter.NoteClickListener, FolderRecyclerAdapter.FolderClickListener {

    private DrawerLayout mDrawer;
    private FloatingActionButton btnNewNote, btnNewVisualNote;
    private FloatingActionMenu fabMenu;
    private android.support.design.widget.FloatingActionButton btnAddFolder;
    private ImageView btnOpenDrawer, imgNotFound;
    private RecyclerView lstNotes, lstFolders;
    private NoteRecyclerAdapter noteAdapter;
    private FolderRecyclerAdapter folderAdapter;
    private LinearLayout btnHome;

    private Menu menu;

    private NoteController noteController = NoteController.getInstance();
    private FolderController folderController = FolderController.getInstance();

    private boolean isGrid = true;
    private boolean isFavorites = false;

    private CoordinatorLayout coordinatorContainer;
    private Long lastFolder;

    enum LayoutType {
        GRID, LIST
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        initializeToolbar();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnNewNote = (FloatingActionButton) findViewById(R.id.itemNewNote);
        btnHome = (LinearLayout) findViewById(R.id.btnHome);
        btnNewVisualNote = (FloatingActionButton) findViewById(R.id.itemNewVisualNote);
        fabMenu = (FloatingActionMenu) findViewById(R.id.menu);
        btnOpenDrawer = (ImageView) findViewById(R.id.btnOpenDrawer);
        imgNotFound = (ImageView) findViewById(R.id.imgNotFound);
        lstNotes = (RecyclerView) findViewById(R.id.lstNotes);
        lstFolders = (RecyclerView) findViewById(R.id.lstFolders);
        btnAddFolder = (android.support.design.widget.FloatingActionButton) findViewById(R.id.btnAddFolder);

        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.drawer_header, (ImageView) findViewById(R.id.drawerHeader));


        folderAdapter = new FolderRecyclerAdapter(new ArrayList<Folder>(), this, this);
        noteAdapter = new NoteRecyclerAdapter(new ArrayList<Note>(), this, R.layout.layout_grid_note_item, this);

        btnHome.setOnClickListener(this);
        btnOpenDrawer.setOnClickListener(this);
        btnNewNote.setOnClickListener(this);
        btnNewVisualNote.setOnClickListener(this);
        btnAddFolder.setOnClickListener(this);

        coordinatorContainer = (CoordinatorLayout) findViewById(R.id.coordinatorContainer);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lstNotes.setLayoutManager(layoutManager);
        lstNotes.setAdapter(noteAdapter);

        lstFolders.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstFolders.setAdapter(folderAdapter);

    }

    private void loadNotes(Long folderId, boolean firstLoad) {

        if (folderId.intValue() != HOME_FOLDER_ID.intValue()) {
            // load from folder
            List<Note> notes;
            if (firstLoad)
                setToolbarTitle(folderController.getFolderById(folderId).getFolderName());
            if ((notes = noteController.getNotesByFolderId(folderId)).size() != 0) {
                Collections.reverse(notes);
                noteAdapter.setData(notes);
                showNoteNotFound(false, false);
            } else {
                Collections.reverse(notes);
                noteAdapter.setData(notes);
                showNoteNotFound(true, false);
            }
        } else {
            List<Note> notes;
            setToolbarTitle(getResources().getString(R.string.home));
            if ((notes = noteController.getNotesByFolderId(HOME_FOLDER_ID)).size() != 0) {
                Collections.reverse(notes);
                noteAdapter.setData(notes);
                showNoteNotFound(false, false);
            } else {
                Collections.reverse(notes);
                noteAdapter.setData(notes);
                showNoteNotFound(true, false);
            }
        }
    }

    public void loadFolders() {
        List<Folder> folders = folderController.getAllFolders();
        folderAdapter.setData(folders.size() > 0 ? folders : new ArrayList<Folder>());
    }

    private void changeListLayout(LayoutType layoutType) {
        switch (layoutType) {
            case GRID: {
                lstNotes.post(new Runnable() {
                    @Override
                    public void run() {
                        noteAdapter = new NoteRecyclerAdapter(noteAdapter.getData(), MainActivity.this, R.layout.layout_grid_note_item, MainActivity.this);
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                        lstNotes.setLayoutManager(layoutManager);
                        lstNotes.setAdapter(noteAdapter);
                    }
                });
            }
            break;
            case LIST: {
                lstNotes.post(new Runnable() {
                    @Override
                    public void run() {
                        noteAdapter = new NoteRecyclerAdapter(noteAdapter.getData(), MainActivity.this, R.layout.layout_list_note_item, MainActivity.this);
                        lstNotes.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                        lstNotes.setAdapter(noteAdapter);
                    }
                });
            }
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        loadNotes(lastFolder = AppSharePrefs.getInstance().getLastFolderId(), true);
        loadFolders();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_layout: {
                changeListLayout(item);
            }
            break;
            case R.id.action_import: {
                checkPermission();
                pickImportFile();
            }
            break;
            case R.id.action_export: {
                if(noteController.getAllNotes().size() > 0) {
                    checkPermission();
                    AppUtils.showAlertDialog(MainActivity.this,
                            getResources().getString(android.R.string.dialog_alert_title),
                            getResources().getString(R.string.exportAlert),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Backup.getInstance(MainActivity.this, coordinatorContainer,
                                            noteController,
                                            folderController).exportNotes();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                } else {
                    AppUtils.showSnackBar(coordinatorContainer, getResources().getString(R.string.noNoteFoundToExport));
                }
            }
            break;
            case R.id.action_favorites: {
                showFavorites(item);
            }
            break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.itemNewNote: {
                Intent i = new Intent(this, NoteActivity.class);
                i.putExtra(Constants.FOLDER_ID, lastFolder);
                startActivityForResult(i, Constants.INTENT_NOTE);
                fabMenu.close(true);
            }
            break;
            case R.id.itemNewVisualNote: {
                Intent i = new Intent(this, VisualNoteActivity.class);
                i.putExtra(Constants.FOLDER_ID, lastFolder);
                startActivityForResult(i, Constants.INTENT_NOTE);
                fabMenu.close(true);
            }
            break;
            case R.id.btnOpenDrawer: {
                mDrawer.openDrawer(Gravity.LEFT);
            }
            break;
            case R.id.btnAddFolder: {
                addFolder();
            }
            break;
            case R.id.btnHome: {
                loadHome();
            }
        }
    }

    public void loadHome() {
        mDrawer.closeDrawer(Gravity.LEFT);
        loadNotes(lastFolder = HOME_FOLDER_ID, false);
        AppSharePrefs.getInstance().saveLastFolderId(lastFolder);
    }

    private void changeListLayout(MenuItem item) {
        if (isGrid) {
            //change to list
            item.setIcon(R.drawable.ic_grid);
            isGrid = false;
            changeListLayout(LayoutType.LIST);
        } else {
            //change to grid
            item.setIcon(R.drawable.ic_list);
            isGrid = true;
            changeListLayout(LayoutType.GRID);
        }
    }

    private void showFavorites(MenuItem item) {
        if (!isFavorites) {
            isFavorites = true;
            item.setIcon(R.drawable.ic_heart_padding);
            List<Note> notes = noteAdapter.getData();
            if (notes.size() > 0) {
                ArrayList<Note> favorites = new ArrayList<>();
                for(Note note : notes) {
                    if(note.isFavorite())
                        favorites.add(note);
                }
                noteAdapter.setData(favorites);
                if (noteAdapter.getItemCount() == 0)
                    showNoteNotFound(true, true);
            }
        } else {
            isFavorites = false;
            item.setIcon(R.drawable.ic_heart_border_padding);
            loadNotes(lastFolder, false);
        }
    }

    private void addFolder() {
        AddFolderDialog.getInstance(MainActivity.this, false, null, new AddFolderDialog.AddFolderDialogInterface() {
            @Override
            public void onAdd(String folderName, Dialog dialog) {
                dialog.dismiss();
                Folder folder = new Folder(folderName);
                folderController.save(folder);
                folderAdapter.insertItem(folder, folderAdapter.getItemCount());

            }

            @Override
            public void onRename(String folderName, Dialog dialog) {
                // not use in here
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    private void renameFolder(final Folder folder, final int position) {
        AddFolderDialog.getInstance(MainActivity.this, true, folder.getFolderName(), new AddFolderDialog.AddFolderDialogInterface() {
            @Override
            public void onAdd(String folderName, Dialog dialog) {
                // not use in here
            }

            @Override
            public void onRename(String folderName, Dialog dialog) {
                dialog.dismiss();
                folder.setFolderName(folderName);
                folderController.update(folder);
                folderAdapter.changeItem(folder, position);
                setToolbarTitle(folderName);
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.INTENT_NOTE: {
                switch (resultCode) {
                    case RESULT_OK: {
                        if (data.getBooleanExtra(Constants.IS_IN_EDIT, false)) {
                            noteAdapter.changeItem(noteController.getNoteById(data.getLongExtra(Constants.NOTE_ID, 0)), data.getIntExtra(Constants.NOTE_POSITION, 0));
                            AppUtils.showSnackBar(coordinatorContainer, getResources().getString(R.string.noteEdited));
                        } else {
                            noteAdapter.insertItem(noteController.getNoteById(data.getLongExtra(Constants.NOTE_ID, 0)), noteAdapter.getItemCount());
                            AppUtils.showSnackBar(coordinatorContainer, getResources().getString(R.string.noteSaved));
                            if (noteAdapter.getItemCount() > 0) {
                                showNoteNotFound(false, false);
                            }
                        }
                    }
                    break;
                    case RESULT_CANCELED: {
                        AppUtils.showSnackBar(coordinatorContainer, getResources().getString(R.string.canceled));
                    }
                    break;
                }
            }
            break;
            case Constants.INTENT_FILE_PICK: {
                switch (resultCode) {
                    case RESULT_OK: {
                        String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                        Backup.getInstance(MainActivity.this, coordinatorContainer,
                                noteController,
                                folderController).importNotes(filePath);
                    }
                    break;
                    case RESULT_CANCELED: {

                    }
                    break;
                }
            }
            break;
        }
    }

    @Override
    public void onNoteItemClick(int position, Note note) {
        if (note.isVisual()) {
            Intent i = new Intent(MainActivity.this, VisualNoteActivity.class);
            i.putExtra(Constants.NOTE_ID, note.getId());
            i.putExtra(Constants.NOTE_POSITION, position);
            startActivityForResult(i, Constants.INTENT_NOTE);
        } else {
            Intent i = new Intent(MainActivity.this, NoteActivity.class);
            i.putExtra(Constants.NOTE_ID, note.getId());
            i.putExtra(Constants.NOTE_POSITION, position);
            startActivityForResult(i, Constants.INTENT_NOTE);
        }
    }

    @Override
    public void onLongNoteItemClick(final int position, final Note note) {
        noteOptions(position, note);
    }

    private void noteOptions(final int position, final Note note) {

        Menu menu = new MenuBuilder(MainActivity.this);
        MenuItem add_remove_favorite;
        MenuItem add_remove_folder;

        if (note.isFavorite()) {
            add_remove_favorite = menu.add(0, R.id.action_remove_from_favorite, 0, R.string.removeFromFavorites);
            add_remove_favorite.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            add_remove_favorite = menu.add(0, R.id.action_add_to_favorite, 0, R.string.addToFavorites);
            add_remove_favorite.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        if (note.getFolderId().intValue() != HOME_FOLDER_ID.intValue()) {
            add_remove_folder = menu.add(0, R.id.action_remove_from_folder, 0, R.string.removeFromFolder);
            add_remove_folder.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            add_remove_folder = menu.add(0, R.id.action_add_to_folder, 0, R.string.addToFolder);
            add_remove_folder.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        menu.add(0, R.id.action_delete_note, 0, R.string.deleteNote);


        BottomSheetMenuDialog dialog = new BottomSheetBuilder(MainActivity.this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setItemTextColor(getResources().getColor(R.color.colorAccent))
                .setMenu(menu)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_add_to_favorite: {
                                note.setFavorite(true);
                                noteController.update(note);
                                noteAdapter.notifyItemChanged(position);
                            }
                            break;
                            case R.id.action_remove_from_favorite: {
                                note.setFavorite(false);
                                noteController.update(note);
                                noteAdapter.notifyItemChanged(position);
                            }
                            break;
                            case R.id.action_add_to_folder: {
                                addToFolder(note, position);
                            }
                            break;
                            case R.id.action_remove_from_folder: {
                                removeFromFolder(note, position);
                            }
                            break;
                            case R.id.action_delete_note: {
                                noteController.delete(note);
                                noteAdapter.removeItem(position);
                                if (noteAdapter.getItemCount() == 0) {
                                    showNoteNotFound(true, false);
                                }
                            }
                            break;
                        }
                    }
                })
                .createDialog();

        dialog.show();
    }

    private void folderOptions(final int position, final Folder folder) {

        Menu menu = new MenuBuilder(MainActivity.this);

        menu.add(0, R.id.action_rename_folder, 0, R.string.renameFolder);
        menu.add(0, R.id.action_delete_folder, 0, R.string.deleteFolder);

        BottomSheetMenuDialog dialog = new BottomSheetBuilder(MainActivity.this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setItemTextColor(getResources().getColor(R.color.colorAccent))
                .setMenu(menu)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_rename_folder: {
                                renameFolder(folder,position);
                            }
                            break;
                            case R.id.action_delete_folder: {
                                AppUtils.showAlertDialog(MainActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.allNotesDelete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        noteController.deleteAllNotesByFolderId(folder.getId());
                                        folderController.delete(folder);
                                        folderAdapter.removeItem(position);
                                        dialogInterface.dismiss();
                                        loadHome();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                            }
                            break;
                        }
                    }
                })
                .createDialog();

        dialog.show();
    }

    private void addToFolder(final Note note, final int position) {
        AddToFolderDialog.getInstance(MainActivity.this, folderAdapter.getData(), new AddToFolderDialog.AddToFolderDialogInterface() {
            @Override
            public void onSelect(Long folderId, Dialog dialog) {
                try {
                    dialog.dismiss();
                    if (folderId.intValue() != HOME_FOLDER_ID.intValue()) {
                        note.setFolderId(folderId);
                        noteController.update(note);
                        noteAdapter.removeItem(position);
                        if (noteAdapter.getData().size() == 0)
                            showNoteNotFound(true, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    private void removeFromFolder(Note note, int position) {
        noteAdapter.removeItem(position);
        if(noteAdapter.getData().size() == 0)
            showNoteNotFound(true, false);
        note.setFolderId(HOME_FOLDER_ID);
        noteController.update(note);
    }

    @Override
    public void onFolderItemClick(int position, Folder folder) {
        mDrawer.closeDrawer(Gravity.LEFT);
        loadNotes(folder.getId(), false);
        setToolbarTitle(folder.getFolderName());
        AppSharePrefs.getInstance().saveLastFolderId(lastFolder = folder.getId());
    }

    @Override
    public void onLongFolderItemClick(int position, Folder folder) {
        folderOptions(position, folder);
    }

    private void showNoteNotFound(boolean show , boolean isFavorites) {
        if (show) {
            imgNotFound.setImageResource(R.drawable.not_found);
            imgNotFound.setVisibility(View.VISIBLE);
            menu.getItem(2).setVisible(false);
            if(!isFavorites)
            menu.getItem(3).setVisible(false);
        } else {
            imgNotFound.setImageDrawable(null);
            imgNotFound.setVisibility(View.GONE);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        }
    }

    private void pickImportFile() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(Constants.INTENT_FILE_PICK)
                .withFilter(Pattern.compile(".*\\.note"))
                .start();
    }



}
