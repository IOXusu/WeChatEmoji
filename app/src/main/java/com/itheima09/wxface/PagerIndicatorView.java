package com.itheima09.wxface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sanpi on 2016/6/15.
 */

public class PagerIndicatorView extends View {

    private Paint paint;
    private float padding;
    private float radius;
    private int mHeight;
    private int mWidth;

    public PagerIndicatorView(Context context) {
        this(context, null);
    }

    public PagerIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        paint = new Paint();
        paint.setAntiAlias(true);
        float density = getResources().getDisplayMetrics().density;
        //间距
        padding = 5 * density;
        //半径
        radius = 5 * density;
    }

    private int mTotalPage;
    private int mCurrentPage;

    public void initData(int totalPage, int currentPage) {
        mTotalPage = totalPage;
        mCurrentPage = currentPage;
        invalidate();
    }
    public void notifiyDataSetChanged(int totalPage, int currentPage){
        initData(totalPage,currentPage);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = (mWidth - (mTotalPage * radius * 2) - (mTotalPage - 1) * padding) * 0.5f + radius;
        float cy = mHeight / 2;

        for (int i = 0; i < mTotalPage; i++) {
            if (i == mCurrentPage) {
                paint.setColor(Color.parseColor("#8C8C8C"));
            } else {
                paint.setColor(Color.parseColor("#BCBCBC"));
            }
            canvas.drawCircle(cx, cy, radius, paint);
            cx += padding + 2 * radius;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }
}
