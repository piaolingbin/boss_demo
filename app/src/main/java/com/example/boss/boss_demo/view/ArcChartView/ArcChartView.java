package com.example.boss.boss_demo.view.ArcChartView;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.boss.boss_demo.R;
import com.example.boss.boss_demo.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ArcChartView extends View {
    private Context mContext;

    private int mArcWidth;
    private int mCircleRadius;
    private Paint arcPaint;
    private Paint arcCirclePaint;
    private RectF arcRectF;
    private float[] mCurData;
    private List<Arc> data;

    public ArcChartView(Context context) {
        this(context, null);
    }

    public ArcChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcChartView, defStyleAttr, 0);
        mArcWidth = typedArray.getDimensionPixelSize(R.styleable.ArcChartView_arcWidth, StringUtils.dp2px(context, 20));
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.ArcChartView_circleRadius, StringUtils.dp2px(context, 100));
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        arcCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcCirclePaint.setStyle(Paint.Style.STROKE);
        arcCirclePaint.setStrokeWidth(mArcWidth);
        arcCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.colorCirclebg));
        arcCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(mArcWidth);
        arcPaint.setColor(0xff0000);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        //圓弧的外接矩形
        arcRectF = new RectF();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = mCircleRadius * 2;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArc(canvas);
    }

    private void drawArc(Canvas canvas) {
        for (int i = 0; i < mCurData.length; i++) {
            final int left = getWidth() / 2 - mCircleRadius + mArcWidth / 2 + mArcWidth * i;
            final int right = getWidth() / 2 + mCircleRadius - mArcWidth / 2 - mArcWidth * i;
            final int top = getHeight() / 2 - mCircleRadius + mArcWidth / 2 + mArcWidth * i;
            final int bottom = getHeight() / 2 + mCircleRadius - mArcWidth / 2 - mArcWidth * i;
            arcRectF.set(left, top, right, bottom);
            //arcPaint.setShader(new SweepGradient(getWidth()/2,getHeight()/2,arcStartColor,arcEndColor));
            arcPaint.setColor(getResources().getColor(data.get(i).getColor()));
            arcPaint.setStrokeWidth(mArcWidth);
            canvas.drawArc(arcRectF, -90, -360 * mCurData[i] / 100, false, arcPaint);
        }
    }

    public void setData(List<Arc> data, TimeInterpolator interpolator) {
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = data;
        }
        mCurData = new float[this.data.size()];
        for (int i = 0; i < mCurData.length; i++) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurData[i], this.data.get(i).getValue());
            valueAnimator.setDuration(1200);
            final int finalI = i;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    mCurData[finalI] = (float) (Math.round(value * 10)) / 10;
                    invalidate();
                }
            });
            valueAnimator.setInterpolator(interpolator);
            valueAnimator.start();
        }
    }
}
