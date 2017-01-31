package com.individual.noteapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.individual.noteapp.R;
import com.individual.noteapp.common.Constants;
import com.individual.noteapp.controls.CustomEditText;
import com.individual.noteapp.models.ExportModel;
import com.individual.noteapp.models.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.individual.noteapp.common.Constants.*;

/**
 * Created by Blackout on 1/25/2017.
 */

public class AppUtils {



    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromBase64String(String base64Bitmap) {
        byte[] decodedString = Base64.decode(base64Bitmap, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap getSubSampleFromBase64(String base64Bitmap) {
        byte[] decodedString = Base64.decode(base64Bitmap, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 6;
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }


    public static void deleteNoteVisualFile(Context context, String fileName) {
        context.deleteFile(fileName);
    }



    public static void showKeyboard(final CustomEditText ettext, final Context context) {
        ettext.requestFocus();
        ettext.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext, 0);
                               }
                           }
                , 200);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }


    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }


    public static void showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener confirmClick, DialogInterface.OnClickListener cancelClick) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, confirmClick)
                .setNegativeButton(android.R.string.no, cancelClick)
                .setIcon(R.drawable.ic_warning)
                .show();
    }


    public static String saveVisualNote(Context context, Bitmap bitmap, String name) {
        try {
            FileOutputStream fos = context.openFileOutput(name,
                    context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new Image(convertBitmapToBase64(bitmap)));
            oos.flush();
            oos.close();
            fos.close();
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ProgressDialog showProgressDialog(Context context, String dialogMessage) {
        ProgressDialog dialog = new ProgressDialog(context); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(dialogMessage);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }



    public static Image getImageByFileName(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Image) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap loadVisualNote(Context context, String fileName, boolean highQuality) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Image image = (Image) ois.readObject();
            if (highQuality) {
                return getBitmapFromBase64String(image.getBase64Image());
            } else {
                return getSubSampleFromBase64(image.getBase64Image());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getBackupDirectory() {
        File f = new File(BACKUP_DIRECTORY);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getPath();
    }


    // Serializes an object and saves it to a file
    public static boolean exportNotes(ExportModel model, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getBackupDirectory() + "/" + fileName + fileSuffix));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(model);
            objectOutputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static ExportModel getExportModelFromPath(String path) {
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            ObjectInputStream ois = new ObjectInputStream(fis);
            ExportModel exportModel = (ExportModel) ois.readObject();
            return exportModel;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static void vibrate(long millisec, Context context) {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(millisec);
    }

    public static String getDateFormat(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

}
