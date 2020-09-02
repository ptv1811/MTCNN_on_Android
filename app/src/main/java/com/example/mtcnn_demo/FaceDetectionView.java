package com.example.mtcnn_demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import static android.graphics.Paint.Style.STROKE;

public class FaceDetectionView extends View{
    private Paint mCirclePaint;
    private Paint mEyeAndMouthPaint;
    private float mCenterX;
    private float mCenterY;
    private float mRadius;

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


    @Override
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
    }

    private void initPaints(@Nullable AttributeSet set) {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.YELLOW);
        mEyeAndMouthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEyeAndMouthPaint.setStyle(STROKE);
        mEyeAndMouthPaint.setStrokeWidth(16 * getResources().getDisplayMetrics().density);
        mEyeAndMouthPaint.setStrokeCap(Paint.Cap.ROUND);
        mEyeAndMouthPaint.setColor(Color.BLACK);
    }
}
