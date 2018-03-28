package com.example.boss.boss_demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CurveChartColuli extends View {
    private static final String TAG = CurveChartColuli.class.getSimpleName();
    public int mWidth;
    public int mHeight;
    private PaintFlagsDrawFilter drawFilter;
    private Paint paint;
    private Paint textPaint;
    private Paint dashPaint;
    private ArrayList<Float> xValues = new ArrayList<>();
    private ArrayList<Float> yValues = new ArrayList<>();
    private float compareValue;
    private boolean isFillDownLineColor;
    private float scaleLen;
    private float perLengthX; // x轴每个刻度的长度
    private Paint linePaint;
    private boolean stop;
    private int numCount;
    private boolean fillDownLineColor;
    private int fillColor;
    private float fraction;
    private float endCircleX, endCircleY;
    private float startX;
    private float startY;
    // x、y轴坐标值
    private List<Float> pointX, pointY;

    private int countX = 24;//x轴刻度数
    private int countY = 24; //y轴刻度数
    private float lengthX; // x轴的长度
    private float lengthY; // y轴的长度
    private float yScaleDistance = 150; // y周左侧的距离
    private ArrayList<Integer> yValueScales;
    private final int STEP = 6;

    private int textSize = 25;
    private int lineColor = Color.parseColor("#000000");
    private int target = 600;

    public CurveChartColuli(Context context) {
        super(context);
    }

    public CurveChartColuli(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //在画布上去除锯齿

        //在画布上去除锯齿
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        textPaint = new Paint();
        dashPaint = new Paint();

        scaleLen = dip2px(context, scaleLen);
        isFillDownLineColor = true;
        compareValue = 95;
    }


    public CurveChartColuli(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpectMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpectSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpectMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpectSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpectMode == MeasureSpec.AT_MOST
                && heightSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight + 40);
        } else if (widthSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpectSize);
        } else if (heightSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpectSize, mHeight + 40);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(drawFilter);
        initSomething();
        drawPoint(canvas);
        drawCoordinate(canvas);
        drawTargetLine(canvas);
    }

    /**
     * 画目标线
     * @param canvas
     */
    private void drawTargetLine(Canvas canvas) {
        paint.setColor(Color.parseColor("#D33A31"));
        paint.setStrokeWidth(2);
        canvas.drawLine(startX, startY - transformYCoordinate(target), mWidth - getPaddingRight(), startY - transformYCoordinate(target), paint);
        textPaint.setColor(Color.parseColor("#D33A31"));
        canvas.drawText("盈亏金额：" + target , startX + 20, startY - transformYCoordinate(target) - 20, textPaint);
    }

    private void initSomething(){
        Rect rect = new Rect();
        textPaint.getTextBounds("300", 1, 3, rect);
        // 所画的坐标系的原点位置
        startX = getPaddingLeft() + yScaleDistance;
        startY = mHeight - getPaddingBottom() + 20;
        // X轴的长度
        lengthX = mWidth - getPaddingRight() - startX;
        // Y轴的长度
        lengthY = startY - getPaddingTop();
        // x轴每个刻度的长度
        perLengthX = 1.0f * lengthX / countX - 1;
        paint.setColor(Color.parseColor("#000000"));
        yValueScales = getYValueScale();
    }

    /**
     * 画坐标系
     *
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas) {
        // 画横坐标
        canvas.drawLine(startX, startY, mWidth - getPaddingRight(), startY, paint);
        // 画纵坐标
        canvas.drawLine(startX, startY, startX, getPaddingTop(), paint);
        // 画x轴的刻度
        textPaint.setTextSize(textSize);

        // 画X轴刻度和值
        for (int i = 0; i <= countX; i++) {
            if (i % 6 == 0){
                canvas.drawText(String.valueOf(xValues.get(i)), startX + i * perLengthX
                        - textPaint.measureText(String.valueOf(yValues.get(i)))/3, startY + 30, textPaint);
            }
            canvas.drawLine(startX + i * perLengthX, startY, startX + i * perLengthX, startY - lengthY, paint); // 刻度线
        }

        // 画y轴的刻度
        for (int i = 0; i < yValueScales.size(); i++) {
            canvas.drawText(yValueScales.get(i).toString(), startX - textPaint.measureText(String.valueOf(yValueScales.get(i))) - 20
                    , startY - transformYCoordinate(yValueScales.get(i)), textPaint);
            canvas.drawLine(startX, startY - transformYCoordinate(yValueScales.get(i)), mWidth - getPaddingRight(), startY - transformYCoordinate(yValueScales.get(i)), paint);
        }
    }

    // 获取y轴显示的刻度数据
    private ArrayList<Integer> getYValueScale(){
        ArrayList<Integer> values = new ArrayList<>();
        int yValueDigit = getMaxYValueDigit();
        int yValueMaxInt = getMaxYValueMaxInt();
        if (yValueMaxInt < 3){
            for (int i = 0; i <= yValueMaxInt * 4 + 1; i++){
                values.add((int) (2.5 * i * Math.pow(10, yValueDigit - 2)));
            }
        } else {
            for (int i = 0; i <= yValueMaxInt + 1; i++){
                values.add(i * 10 * (int) Math.pow(10, yValueDigit - 2));
            }
        }
       return values;
    }

    /**
     * 获取Y轴最大值的整数位数
     * @return
     */
    private int getMaxYValueDigit(){
        float maxYValue = Collections.max(yValues);
        String str = String.valueOf(maxYValue);
        if (str.contains(".")){
            str = str.split("\\.")[0];
        }
        Log.e(TAG, "getMaxYValueDigit:"+ str.length());
        return str.length();
    }

    /**
     * 获取y轴最大值整数位
     * @return
     */
    private int getMaxYValueMaxInt(){
        float maxYValue = Collections.max(yValues);
        String str = String.valueOf(maxYValue);
        String secondStr = str.substring(1,2);
        str = str.substring(0, 1);
        Log.e(TAG, "getMaxYValueMaxInt:"+ str);
        if (canParseInt(str)){
            return Integer.parseInt(secondStr) >= 5 ? Integer.parseInt(str) + 1 : Integer.parseInt(str);
        } else {
            return 0;
        }
    }

    /**
     * 验证一个字符串是否能解析成整数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseInt(String numberStr) {
        try {
            Integer.parseInt(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void drawPoint(Canvas canvas) {
        // 用于保存y值大于compareValue的值
        if (xValues != null) {
            setPointXY();
            Rect rect = new Rect();
            textPaint.getTextBounds("300", 0, 3, rect);
            linePaint = new Paint();
            linePaint.setColor(fillColor);
            // 把拐点设置成圆的形式，参数为圆的半径，这样就可以画出曲线了
            PathEffect pe = new CornerPathEffect(100);
            //linePaint.setPathEffect(pe);
            if (!isFillDownLineColor) {
                linePaint.setStyle(Paint.Style.STROKE);
            }
            Path path = new Path();
            Path path2 = new Path();
            path.moveTo(pointX.get(0), pointY.get(0));
            int count = xValues.size();
            for (int i = 0; i < count - 1; i++) {
                float x, y, x2, y2, x3, y3, x4, y4;
                x = pointX.get(i);
                Log.e(TAG, "x:" + x + "--> pointX:" + pointX.get(i));
                x4 = pointX.get(i + 1);
                x2 = x3 = (x + x4) / 2;
                // 乘以这个fraction是为了添加动画特效
                y = startY - transformYCoordinate(yValues.get(i)) * fraction;
                Log.e(TAG, "y:" + y + "--> pointY:" + pointY.get(i));
                y4 = startY - transformYCoordinate(yValues.get(i + 1)) * fraction;
                y2 = y + (y4 - y) / 7;
                y3 = y + (y4 - y) / 7 * 5;
                if (!isFillDownLineColor && i == 0) {
                    path2.moveTo(x, y);
                    path.moveTo(x, y);
                    continue;
                }
                // 填充颜色
                if (isFillDownLineColor && i == 0) {
                    // 形成封闭的图形
                    path2.moveTo(x, y);
                    path.moveTo(x, startY);
                    path.lineTo(x, y);
                }

                // // 填充颜色
                // if (isFillDownLineColor && i == count - 1) {
                // path.lineTo(x, startY);
                // }
                path.cubicTo(x2, y2, x3, y3, x4, y4);
                path2.cubicTo(x2, y2, x3, y3, x4, y4);
            }
            if (isFillDownLineColor) {
                // 形成封闭的图形
                path.lineTo(pointX.get(count - 1), startY);
            }
            Log.e(TAG, "drawPoint:" + yValues.toString());
            Log.e(TAG, "drawPointX:" + xValues.toString());
            Paint rectPaint = new Paint();
            rectPaint.setColor(Color.parseColor("#ffffffff"));
            if (isFillDownLineColor) {
                canvas.drawPath(path, linePaint);
            }
            /*float left = pointX.get(0);
            float top = getPaddingTop();
            float right = pointX.get(count - 1);
            float bottom = startY;
            // 渐变的颜色
            LinearGradient lg = new LinearGradient(left, top, left, bottom, Color.parseColor("#33ffffff"),
                    Color.parseColor("#ffffffff"), Shader.TileMode.CLAMP);// CLAMP重复最后一个颜色至最后
            rectPaint.setShader(lg);
            rectPaint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.SRC_ATOP));
            if (isFillDownLineColor) {
                canvas.drawPath(path, linePaint);
            }
            canvas.drawRect(left, top, right, bottom, rectPaint); // 画渐变白色*/
            // canvas.restoreToCount(layerId);
            rectPaint.setXfermode(null);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth((float) 10);
            linePaint.setColor(lineColor);//设置曲线的颜色的
            canvas.drawPath(path2, linePaint);
            //linePaint.setPathEffect(null);
            //drawDashAndPoint(storageX, storageY, startY, canvas);// 画原点和虚线
            if (!stop)
                performAnimator();
            if (fraction > 0.99) {
                performAnimator();
            }
        }
    }

    private void drawLine(Path path2) {
        List<Cubic> calculate_x = calculate(pointX);
        List<Cubic> calculate_y = calculate(pointY);
        if (null != calculate_x && null != calculate_y && calculate_y.size() >= calculate_x.size()) {
            path2.moveTo(calculate_x.get(0).eval(0), calculate_y.get(0).eval(0));
            for (int i = 0; i < calculate_x.size(); i++) {
                Log.e(TAG, "calculate_y:" + pointY);
                for (int j = 1; j <= STEP; j++) {
                    float u = j / (float) STEP;
                    if (i > 15){
                        Log.e(TAG, "eval(u)Y:" + calculate_y.get(i).eval(u));
                    }
                    path2.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i).eval(u));
                }
            }
        }
    }

    private void setPointXY() {
        if (pointX == null) pointX = new ArrayList<>();
        if (pointY == null) pointY = new ArrayList<>();
        if (!pointX.isEmpty()) pointX.clear();
        if (!pointY.isEmpty()) pointY.clear();
        for (int i = 0; i < xValues.size(); i++){
            pointX.add(startX + xValues.get(i) * perLengthX);
            pointY.add(startY - transformYCoordinate(yValues.get(i)));
        }
    }


    /**
     * 将传入的y数据值转换为屏幕坐标值
     *
     * @param y
     * @return
     */
    public float transformYCoordinate(float y) {
        Log.v(TAG, "transformYCoordinate..  viewHeigth = " + mHeight);
        float max = Collections.max(yValueScales);
        return y / max * mHeight;
    }

    private void drawDashAndPoint(float[] x, float[] y, float startY,
                                  Canvas canvas) {
        PathEffect pe = new DashPathEffect(new float[]{10, 10}, 1);
        // 要设置不是填充的，不然画一条虚线是没显示出来的
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(pe);
        dashPaint.setColor(Color.BLACK);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.BLACK);
        for (int i = 0; i < x.length; i++) {
            if (y[i] > 1) {

                canvas.drawCircle(x[i], y[i], 11, pointPaint);
                Path path = new Path();
                path.moveTo(x[i], startY);
                path.lineTo(x[i], y[i]);
                canvas.drawPath(path, dashPaint);
            }
        }

    }

    /**
     *  设置目标值
     * @param target
     */
    private void setTarget(int target){
        this.target = target;
    }

    public void performAnimator() {
        if (numCount > 0)
            return;
        ValueAnimator va = ValueAnimator.ofFloat(0, 1);
        if (numCount == 1) {
            va = ValueAnimator.ofFloat(0, 1);
        } else if (numCount == 2) {
            va = ValueAnimator.ofFloat(0.85f, 1);
        } else if (numCount == 3) {
            va = ValueAnimator.ofFloat(0.95f, 1);
        }
        numCount++;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = (Float) animation.getAnimatedValue();
                stop = true;
                postInvalidate();
            }
        });
        va.setDuration(1000);
        va.start();
    }

    public void setFillDownLineColor(boolean fillDownLineColor) {
        this.fillDownLineColor = fillDownLineColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setLineColor(int lineColor){
        this.lineColor = lineColor;
    }

    public void setCompareValue(float compareValue) {
        this.compareValue = compareValue;
    }

    /**
     * 设置xy轴字体大小
     */
    public void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public static class CurveChartBuilder {
        private static CurveChartColuli curveChart;
        private static CurveChartBuilder cBuilder;

        private CurveChartBuilder() {
        }

        public static CurveChartBuilder createBuilder(CurveChartColuli curve) {
            curveChart = curve;
            synchronized (CurveChartBuilder.class) {
                if (cBuilder == null) {
                    cBuilder = new CurveChartBuilder();
                }
            }
            return cBuilder;
        }

        /**
         * 是否填充曲线下面的颜色，默认值为true，
         *
         * @param isFillDownLineColor
         * @return
         */
        public static CurveChartBuilder setIsFillDownColor(boolean isFillDownLineColor) {
            curveChart.setFillDownLineColor(isFillDownLineColor);
            return cBuilder;
        }

        /**
         * 设置填充的颜色
         *
         * @param fillColor
         * @return
         */
        public static CurveChartBuilder setFillDownColor(int fillColor) {
            curveChart.setFillColor(fillColor);
            return cBuilder;
        }

        /**
         * 设置曲线的颜色
         *
         * @param lineColor
         * @return
         */
        public static CurveChartBuilder setLineColor(int lineColor) {
            curveChart.setLineColor(lineColor);
            return cBuilder;
        }

        /**
         * 比较的值，比这个值大就把这个点也绘制出来
         *
         * @param compareValue
         * @return
         */
        public static CurveChartBuilder setCompareValue(float compareValue) {
            curveChart.setCompareValue(compareValue);
            return cBuilder;
        }

        public static CurveChartBuilder setXYValues(ArrayList<Float> xValues, ArrayList<Float> yValues) {
            curveChart.setxValues(xValues);
            curveChart.setyValues(yValues);
            return cBuilder;
        }

        public void show() {
            if (curveChart == null) {
                throw new NullPointerException("CurveChart is null");
            }
            cBuilder.show();
        }
    }

    /**
     * 计算曲线.
     * @param x
     * @return
     */
    private List<Cubic> calculate(List<Float> x) {
        if (null != x && x.size() > 0) {
            int n = x.size() - 1;
            float[] gamma = new float[n + 1];
            float[] delta = new float[n + 1];
            float[] D = new float[n + 1];
            int i;
            /*
             * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 |
             * |D[1]| |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . |
             * | . | | 1 4 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] -
             * x[n-1])]
             *
             * by using row operations to convert the matrix to upper triangular
             * and then back sustitution. The D[i] are the derivatives at the
             * knots.
             */

            gamma[0] = 1.0f / 2.0f;
            for (i = 1; i < n; i++) {
                gamma[i] = 1 / (4 - gamma[i - 1]);
            }
            gamma[n] = 1 / (2 - gamma[n - 1]);

            delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
            for (i = 1; i < n; i++) {
                delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1]) * gamma[i];
            }
            delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];

            D[n] = delta[n];
            for (i = n - 1; i >= 0; i--) {
                D[i] = delta[i] - gamma[i] * D[i + 1];
            }

            /* now compute the coefficients of the cubics */
            List<Cubic> cubics = new LinkedList<Cubic>();
            for (i = 0; i < n; i++) {
                Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i)) - 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i] + D[i + 1]);
                cubics.add(c);
            }
            return cubics;
        }
        return null;
    }

    public class Cubic {

        float a, b, c, d; /* a + b*u + c*u^2 +d*u^3 */

        public Cubic(float a, float b, float c, float d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        /** evaluate cubic */
        public float eval(float u) {
            return (((d * u) + c) * u + b) * u + a;
        }
    }

    public void setxValues(ArrayList<Float> xValues) {
        this.xValues = xValues;
        Log.e(TAG, "setxValues:" + xValues.toString());
    }

    public void setyValues(ArrayList<Float> yValues) {
        this.yValues.clear();
        this.yValues.addAll(yValues);
        Log.e(TAG, "setyValues:" + yValues.toString());
    }

    private void setyStart(float yStart, float yEnd) {

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
