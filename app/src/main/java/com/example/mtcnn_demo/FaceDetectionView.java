package com.example.mtcnn_demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Vector;

import static android.graphics.Paint.Style.STROKE;

public class FaceDetectionView extends SurfaceView implements SurfaceHolder.Callback{
    private Paint mCirclePaint;
    private Paint mEyeAndMouthPaint;
    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    protected SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private boolean mIsCreated = false;


    private RectF mArcBounds = new RectF();

    public FaceDetectionView(Context context) {
//        this(context, null);
        super(context);
        initPaints(null);
    }

    public FaceDetectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints(attrs);
    }

    public FaceDetectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints(null);
    }


    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = View.MeasureSpec.getSize(widthMeasureSpec);
        int h = View.MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(100, 100, 200, 200, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = w / 2f;
        mCenterY = h / 2f;
        mRadius = Math.min(w, h) / 2f;
    }*/

    private void initPaints(@Nullable AttributeSet set) {
        /*mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.YELLOW);
        mEyeAndMouthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEyeAndMouthPaint.setStyle(STROKE);
        mEyeAndMouthPaint.setStrokeWidth(16 * getResources().getDisplayMetrics().density);
        mEyeAndMouthPaint.setStrokeCap(Paint.Cap.ROUND);
        mEyeAndMouthPaint.setColor(Color.BLACK);*/

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5f);
        mPaint.setStyle(STROKE);


    }
    public void setResults(Vector<Box> boxes){

        if (!mIsCreated) {
            return;
        }
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.TRANSPARENT);
        try {
            int size = boxes.size();
            for (int i = 0; i < size;++i){
                Rect rect =boxes.get(i).transform2Rect();
                canvas.drawRect(rect, mPaint);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Surface surface = mSurfaceHolder.getSurface();
        if (surface != null && surface.isValid()){
            try {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mIsCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsCreated = false;
    }
}
