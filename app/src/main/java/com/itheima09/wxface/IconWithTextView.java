package com.itheima09.wxface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sanpi on 2016/6/15.
 */

public class IconWithTextView extends View {

    private Bitmap iconBitmap;
    private String tabText;
    private int tabTextColor;
    private float tabTextSize;
    private Paint paint;
    private int mWidth;
    private int mHeight;
    private Rect textBounds;
    private RectF iconRectF;
    private float textX;
    private float textY;
    private Bitmap maskBitmap;

    public IconWithTextView(Context context) {
        this(context, null);
        //在代码中new出来的时候调用的
    }

    public IconWithTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //布局在xml中，会调用
    }

    public IconWithTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //需要手动在第二个构造方法中调用
        //初始化
        //获取自定义的属性
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.IconWithTextView, defStyleAttr, 0);
        //获取icon
        BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(R.styleable.IconWithTextView_wx_icon);
        iconBitmap = drawable.getBitmap();
        //获取文字
        tabText = a.getString(R.styleable.IconWithTextView_wx_text);
        //获取文字的颜色
        tabTextColor = a.getColor(R.styleable.IconWithTextView_wx_text_color, Color.parseColor("#4cc222"));
        //获取文字的大小
        tabTextSize = a.getDimension(R.styleable.IconWithTextView_wx_text_size, 18);

        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿，必须设置的
        paint.setTextSize(tabTextSize);
        paint.setColor(Color.BLACK);

    }

    public void setIconTextAlpha(float alpha) {
        this.mAlpha = alpha;
        //重绘界面
        if(Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }

    float mAlpha = 0.0f;//0.0f -> 1.0f

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画icon
        canvas.drawBitmap(iconBitmap, null, iconRectF, null);
        int alpha = (int) (mAlpha * 255);
        drawTargetBitmap(alpha);
        //画变色文字
        paint.setColor(tabTextColor);
        paint.setAlpha(alpha);
        canvas.drawText(tabText, textX, textY, paint);
        //画文字
        paint.setColor(Color.BLACK);
        paint.setAlpha(255 - alpha);
        canvas.drawText(tabText, textX, textY, paint);
        //画maskBitmap
        canvas.drawBitmap(maskBitmap, 0, 0, null);
    }

    //在内存中绘制变色的icon
    public void drawTargetBitmap(int alpha) {
        Paint paint = new Paint();
        //先设置颜色再设置透明度
        paint.setColor(tabTextColor);
        paint.setAlpha(alpha);
        paint.setAntiAlias(true);
        //创建空白的bitmap
        maskBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(maskBitmap);
        //色块所在区域
        RectF rectF = new RectF(iconRectF.left +1, iconRectF.top + 1, iconRectF.right - 1, iconRectF.bottom - 1);//修正图片问题

        canvas.drawRect(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        paint.setAlpha(255);
        canvas.drawBitmap(iconBitmap,null,iconRectF,paint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控件宽
        mWidth = getMeasuredWidth();
        //控件的高
        mHeight = getMeasuredHeight();
        //计算文字的宽高
        textBounds = new Rect();
        paint.getTextBounds(tabText, 0, tabText.length(), textBounds);

        //计算icon的宽高
        int iconWidth = mHeight - getPaddingTop() - getPaddingBottom() - textBounds.height();
        //计算icon的left
        float left = (mWidth - iconWidth) * 0.5f;
        //计算icon的top
        float top = getPaddingTop();
        //icon所在的的矩形区域
        iconRectF = new RectF(left, top, left + iconWidth, top + iconWidth);
        //文字的坐标，文字的坐标在文字的左下角
        textX = (mWidth - textBounds.width()) * 0.5f;
        textY = iconRectF.bottom + textBounds.height();
    }


}
