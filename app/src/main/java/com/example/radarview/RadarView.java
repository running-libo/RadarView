package com.example.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * create by libo
 * create on 2020/7/25
 * description 雷达能力图
 */
public class RadarView extends View {
    /**
     * 网画笔
     */
    private Paint netPaint;
    /**
     * 虚线画笔
     */
    private Paint dashPaint;
    /**
     * 文字paint
     */
    private Paint textPaint;
    /**
     * 面积区域paint
     */
    private Paint regionPaint;
    /**
     * 中心点横坐标
     */
    private float centerX;
    /**
     * 中心点纵坐标
     */
    private float centerY;
    /**
     * 多边形的半径长度
     */
    private int radius;
    /**
     * 多边形的层级数
     */
    private int layerCount = 4;
    /**
     * 多边形每两个顶点之间夹角弧度
     */
    private double perAngle;
    /**
     * 绘制网的path
     */
    private Path netPath;
    /**
     * 中心点到各个顶点的path
     */
    private Path dashPath;
    /**
     * 区域path
     */
    private Path regionPath;
    /**
     * 每个角标题
     */
    private String[] titleArray;
    /**
     * 每个角分数值
     */
    private int[] scoreArray;

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);

        centerX = getLeft() + width / 2;
        centerY = getTop() + width / 2;

        radius = width / 12;

        perAngle = degree2Radian();

    }

    private void init() {
        //网格线paint
        netPaint = new Paint();
        netPaint.setColor(getResources().getColor(R.color.colorBlack));
        netPaint.setAntiAlias(true);
        netPaint.setStyle(Paint.Style.STROKE);
        netPaint.setStrokeWidth(3);

        //虚线paint
        dashPaint = new Paint();
        dashPaint.setColor(getResources().getColor(R.color.colorBlack));
        dashPaint.setAntiAlias(true);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(3);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0)); //绘制长度为10的虚线

        //文字paint
        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.yellow));
        textPaint.setTextSize(50);

        //区域paint
        regionPaint = new Paint();
        regionPaint.setColor(getResources().getColor(R.color.colorAccent));
        regionPaint.setAlpha(150);

        netPath = new Path();
        dashPath = new Path();
        regionPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //规律改变半径绘制多层多边形
        int curRadius = radius;
        for (int i = 0; i < layerCount; i++) {
            drawPolygon(canvas, curRadius, i);
            curRadius += radius;
        }

    }

    /**
     * 角度转弧度
     *
     * @return
     */
    private double degree2Radian() {
        return (double) 2 * Math.PI / titleArray.length;
    }

    /**
     * 根据半径绘制一个多边形
     *
     * @param radius
     */
    private void drawPolygon(Canvas canvas, float radius, int layer) {
        //起点从Y轴正上方开始
        double startAngle = 0;

        if (layer == layerCount - 1) {
            dashPath.moveTo(centerX, centerY);

            drawText(canvas, radius);

            drawRegion(canvas, radius);
        }

        for (int i = 0; i < titleArray.length; i++) {
            //每个顶点的坐标
            Point point = createPoint(radius, startAngle);

            //path的连线
            if (i == 0) {
                netPath.moveTo(point.x, point.y);
            } else {
                netPath.lineTo(point.x, point.y);
            }

            //最外层多边形
            if (layer == layerCount - 1) {
                //最外层多边形需要绘制顶点与中心连线
                dashPath.lineTo(point.x, point.y);
                dashPath.moveTo(centerX, centerY);
            }

            startAngle += perAngle;
        }

        netPath.close();

        canvas.drawPath(netPath, netPaint);  //画网格
        canvas.drawPath(dashPath, dashPaint);  //画虚线
    }

    /**
     * 绘制每个角的文本，根据顶点在在坐标系象限的关系偏移
     */
    private void drawText(Canvas canvas, float radius) {
        double startAngle = 0;
        for (int i = 0; i < titleArray.length; i++) {
            //绘制每个顶点文字，需要加大半径防止与网重叠
            Point point = createPoint(radius + 25, startAngle);

            Rect rect = new Rect();
            textPaint.getTextBounds(titleArray[i], 0, titleArray[i].length(), rect);

            if (point.x == centerX) {  //在Y轴上的顶点，需左移一半
                point.x -= rect.width()/2; //正上方点偏移

                if (point.y > centerY) {
                    point.y += rect.height();  //正下方点偏移
                }
            } else if (point.x < centerX) {
                point.x -= rect.width();  //左上方点偏移
                if (point.y > centerY) {
                    point.y += rect.height()/2;  //左下方点偏移
                }
            } else if (point.x > centerX) {
                if (point.y > centerY) {
                    point.y += rect.height()/2;
                }
            }

            //绘制顶点文字
            canvas.drawText(titleArray[i], point.x, point.y, textPaint);

            startAngle += perAngle;
        }
    }

    /**
     * 绘制面积区域
     * @param canvas
     * @param radius
     */
    private void drawRegion(Canvas canvas, float radius) {
        float startAngle = 0;
        for (int i = 0; i < titleArray.length; i++) {
            Point point = createPoint(radius*scoreArray[i]/100, startAngle);

            //path的连线
            if (i == 0) {
                regionPath.moveTo(point.x, point.y);
            } else {
                regionPath.lineTo(point.x, point.y);
            }

            startAngle += perAngle;
        }

        regionPath.close();

        canvas.drawPath(regionPath, regionPaint);
    }

    public void setTitleArray(String[] titleArray) {
        this.titleArray = titleArray;
    }

    public void setScoreArray(int[] scoreArray) {
        this.scoreArray = scoreArray;
        invalidate();
    }

    private Point createPoint(float radius, double angle) {
        float x = (float) (centerX + radius * Math.sin(angle));
        float y = (float) (centerY - radius * Math.cos(angle));
        return new Point(x, y);
    }

    class Point {
        float x;
        float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
