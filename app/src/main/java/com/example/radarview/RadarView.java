package com.example.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
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
    /** 文字paint */
    private Paint textPaint;
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
    /** 每个角标题 */
    private String[] titleArray;

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
        textPaint.setColor(getResources().getColor(R.color.black));
        textPaint.setTextSize(50);

        netPath = new Path();
        dashPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        perAngle = (double) 2 * Math.PI / titleArray.length;  //每个顶点到中心之间夹角弧度

        netPath.reset();
        dashPath.reset();

        //规律改变半径绘制4个多边形
        int curRadius = radius;
        for (int i = 0; i < layerCount; i++) {
            drawPolygon(canvas, curRadius, i);
            curRadius += radius;
        }

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
        }

        for (int i = 0; i < titleArray.length; i++) {
            //每个顶点的坐标
            float x = (float) (centerX + radius * Math.sin(startAngle));
            float y = (float) (centerY - radius * Math.cos(startAngle));

            //path的连线
            if (i == 0) {
                netPath.moveTo(x, y);
            } else {
                netPath.lineTo(x, y);
            }

            //最外层多边形
            if (layer == layerCount - 1) {
                //最外层多边形需要绘制顶点与中心连线
                dashPath.lineTo(x, y);
                dashPath.moveTo(centerX, centerY);

                canvas.drawText(titleArray[i], x, y, textPaint);
            }

            startAngle += perAngle;
        }

        netPath.close();

        canvas.drawPath(netPath, netPaint);  //画网格
        canvas.drawPath(dashPath, dashPaint);  //画虚线
    }

    public void setTitleArray(String[] titleArray) {
        this.titleArray = titleArray;

        invalidate();
    }

}
