package com.example.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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
     * 中心点坐标
     */
    private float centerX;
    private float centerY;
    /**
     * 多边形的半径长度
     */
    private int radius;
    /**
     * 绘制正多边形边数
     */
    private int sideCount = 7;
    /** 存储正多边形每个顶点坐标 */
    private ArrayList<Point> points = new ArrayList<>();
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
    private Path centerPath;


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
        centerX = getLeft() + width / 2;
        centerY = getTop() + width / 2;

        radius = width / 12;

        setMeasuredDimension(width, width);
    }

    private void init() {
        netPaint = new Paint();
        netPaint.setColor(getResources().getColor(R.color.colorBlack));
        netPaint.setAntiAlias(true);
        netPaint.setStyle(Paint.Style.STROKE);
        netPaint.setStrokeWidth(3);

        netPath = new Path();
        centerPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        perAngle = (double) 2 * Math.PI / sideCount;  //每个顶点到中心之间夹角弧度

        netPath.reset();

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

        for (int i = 0; i <= sideCount; i++) {
            float x = (float) (centerX + radius * Math.sin(startAngle));
            float y = (float) (centerY - radius * Math.cos(startAngle));

            //path的连线
            if (i == 0) {
                netPath.moveTo(x, y);
            } else {
                netPath.lineTo(x, y);
            }

            if (layer == layerCount-1) {
                //最外层多边形需要绘制顶点与中心连线
                centerPath.moveTo(centerX, centerY);
            }

            startAngle += perAngle;
        }

        canvas.drawPath(netPath, netPaint);
    }

    /**
     * 重设边的数量
     *
     * @param sideCount
     */
    public void setSideCount(int sideCount) {
        this.sideCount = sideCount;
        invalidate();
    }

    /**
     * 记录每个顶点的坐标
     */
    class Point {
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
