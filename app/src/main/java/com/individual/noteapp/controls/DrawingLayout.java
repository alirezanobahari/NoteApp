package com.individual.noteapp.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.individual.noteapp.R;
import com.individual.noteapp.utils.AppUtils;

/**
 * Created by Blackout on 1/25/2017.
 */

public class DrawingLayout extends RelativeLayout {

    private ImageView imgLastDraw;
    private DrawingView drawingView;


    public DrawingLayout(Context context) {
        super(context);
        init(context);
    }

    public DrawingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View inflatedView = View.inflate(context, R.layout.layout_drawing, this);

        imgLastDraw = (ImageView) inflatedView.findViewById(R.id.imgLastDraw);
        drawingView = (DrawingView) inflatedView.findViewById(R.id.drawView);

    }

    public void setBackImage(Bitmap bitmap) {
        imgLastDraw.setImageBitmap(bitmap);
    }

    public void setEraserMode(boolean isEraserMode) {
        drawingView.setEraserMode(isEraserMode);
    }

    public boolean isEraserMode() {
        return drawingView.isEraserMode();
    }

    public String getBase64Paint() {
        return AppUtils.convertBitmapToBase64(AppUtils.getBitmapFromView(drawingView));
    }


    public void showBase64Paint(String base64Bitmap) {
      //  drawingView.setLastDraw(AppUtils.getBitmapFromBase64String(base64Bitmap));
    }


    public void setPainColor(int color) {
        drawingView.setPainColor(color);
    }

    public void setPenSize(int penSize) {
        drawingView.setPenSize(penSize);
    }

}
