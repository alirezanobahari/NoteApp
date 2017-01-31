package com.individual.noteapp.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.individual.noteapp.R;
import com.individual.noteapp.abstracts.BaseActivity;
import com.individual.noteapp.activities.VisualNoteActivity;
import com.individual.noteapp.application.AppController;
import com.individual.noteapp.utils.UiUtils;

/**
 * Created by Blackout on 1/25/2017.
 */

public class DrawingView extends View {

    private final int ERASER_SIZE = (int)UiUtils.convertDpToPixel(14,getContext());
    public static final int DEFAULT_PEN_SIZE = (int)UiUtils.convertDpToPixel(1, AppController.getContext());

    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;

    private ImageView imageView;

    private Paint mPaint;

    private boolean isEraserMode = false;

    private int tempColor;
    private int tempPenSize;


    public DrawingView(Context context) {
        super(context);
        init(context);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c) {
        imageView = new ImageView(c);

        setBackgroundColor(Color.TRANSPARENT);

        context=c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getResources().getColor(R.color.colorAccent));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        setPenSize(DEFAULT_PEN_SIZE);
    }

    public void setPainColor(int color) {
        tempColor = color;
        mPaint.setColor(color);
    }

    public void setPenSize(int penSize) {
        tempPenSize = penSize;
        mPaint.setStrokeWidth(penSize);
    }

    public void setEraserMode(boolean isEraserMode) {
        if(isEraserMode) {
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(ERASER_SIZE);
        } else {
            setPainColor(tempColor);
            setPenSize(tempPenSize);
        }
        this.isEraserMode = isEraserMode;
    }

    public boolean isEraserMode() {
        return isEraserMode;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            if(isEraserMode) {
                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }


    }


    private void touch_up() {
        mPath.lineTo(mX, mY);
        if(isEraserMode)
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}
