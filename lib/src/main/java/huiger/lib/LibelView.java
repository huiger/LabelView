package huiger.lib;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/****************************************************************
 * *     *  * * * *     Created by <huiGer>
 * *     *  *           Time : 2018/4/8 15:36.
 * * * * *  *   * *     Email: zhihuiemail@163.com
 * *     *  *     *     blog : huiGer.top
 * *     *  * * * *     Desc : 标签
 ****************************************************************/
public class LibelView extends View {

    /**
     * 最小宽高
     */
    private int mixHeight, mixWidth;
    /**
     * 线条画笔 / 文字画笔
     */
    private Paint mLinePaint, mTextPaint;
    /**
     * 圆点半径 / 圆心坐标
     */
    private float mRadius, mCircleX, mCircleY;
    /**
     * 阴影
     */
    private RadialGradient radialGradient;
    /**
     * 线条颜色
     */
    private int mLineColor;
    /**
     * 线条path
     */
    private Path[] mPath;
    /**
     * 间隙
     */
    private float disGap;
    /**
     * 文字数组
     */
    private String[] mStrs = new String[]{""};
    /**
     * 线条开始横坐标
     */
    private float lineStartX;
    /**
     * 阴影半径(动画实时改变)
     */
    private float progress;
    /**
     * 动画
     */
    private ObjectAnimator mAnimator;
    /**
     * 是否支持移动
     */
    private boolean mScrollEnabled;
    /**
     * 是否可移动该view(长按激活)
     */
    private boolean isScroll;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                isScroll = false;
                Log.d("msg", "LibelView -> handleMessage: " + "msg.what == 1");
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        msg.what = 2;
                        handleMessage(msg);
                    }
                }, 2000);
            } else if (msg.what == 2) {
                Log.d("msg", "LibelView -> handleMessage: " + "msg.what == 2");
                Log.d("msg", "LibelView -> handleMessage: " + "长按启动");
                isScroll = true;
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                removeCallbacksAndMessages(null);
            }

        }
    };
    /**
     * 原点在哪个方向(大于0, 在左侧, 反之,在右)
     */
    private int asLeft;
    /**
     * 细线与文字的间隙
     */
    private float mLineDisText;


    public LibelView(Context context) {
        this(context, null);
    }
    public LibelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LibelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
        initAnim();

    }

    private void init(Context context, AttributeSet attrs) {

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path[]{new Path(), new Path(), new Path()};

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LibelView);
        mLinePaint.setStrokeWidth(typedArray.getDimensionPixelSize(R.styleable.LibelView_libelViewLineSize, Utils.sp2px(context, 2)));
        mLineColor = typedArray.getColor(R.styleable.LibelView_libelViewLineColor, Color.WHITE);
        mLinePaint.setColor(mLineColor);
        mTextPaint.setTextSize(typedArray.getDimensionPixelSize(R.styleable.LibelView_libelViewTextSize, Utils.sp2px(context, 15)));
        mTextPaint.setColor(typedArray.getColor(R.styleable.LibelView_libelViewTextColor, Color.WHITE));
        mScrollEnabled = typedArray.getBoolean(R.styleable.LibelView_libelViewScrollEnabled, false);
        typedArray.recycle();

        mixHeight = Utils.dp2px(context, 80);
        mixWidth = Utils.dp2px(context, 200);
        disGap = Utils.dp2px(context, 20);
        mRadius = Utils.dp2px(context, 5);
        mLineDisText = mRadius / 2;
        mCircleX = mRadius * 1.5f;

        radialGradient = new RadialGradient(0, mCircleY,
                mRadius, 0x7f000000, 0x3f000000, Shader.TileMode.CLAMP);


    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        mAnimator = ObjectAnimator.ofFloat(this, "progress", 0.6f, 1);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        // 无限循环
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // 重复模式, RESTART: 重新开始 REVERSE:恢复初始状态再开始
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
    }

    public boolean ismScrollEnabled() {
        return mScrollEnabled;
    }

    public void setmScrollEnabled(boolean mScrollEnabled) {
        this.mScrollEnabled = mScrollEnabled;
    }

    public float getmCircleY() {
        return mCircleY;
    }

    public void setmCircleY(float mCircleY) {
        this.mCircleY = mCircleY;
    }

    public float getmCircleX() {
        return mCircleX;
    }

    public void setmCircleX(float mCircleX) {
        this.mCircleX = mCircleX;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mScrollEnabled) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:   // 按下
                mHandle.sendEmptyMessage(1);

                break;
            case MotionEvent.ACTION_MOVE:   // 移动
                if (isScroll) {
                    mCircleX = event.getX();
                    mCircleY = event.getY();
                    invalidate();
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:     // 抬起
                isScroll = false;
                mHandle.removeCallbacksAndMessages(null);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);
        drawLine(canvas);
        drawText(canvas);

    }

    /**
     * 绘制圆点与阴影
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        // 阴影
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setShader(radialGradient);
        canvas.drawCircle(mCircleX, mCircleY, mRadius * 1.5f * progress, mLinePaint);

        // 原点
        mLinePaint.setShader(null);
        canvas.drawCircle(mCircleX, mCircleY, mRadius, mLinePaint);
    }

    /**
     * 绘制细线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        if (mStrs.length == 0) return;
        mLinePaint.setStyle(Paint.Style.STROKE);
        reSetPath();

        if (mCircleX > getMeasuredHeight() / 2) { // 原点在右侧, 文字需要在左
            asLeft = -1;
            lineStartX = mCircleX - mRadius * 0.6f;
        } else {
            asLeft = 1;
            lineStartX = mCircleX + mRadius * 0.6f;
        }

        if (mStrs.length == 1) {
            mPath[0].rLineTo((mTextPaint.measureText(mStrs[0]) + disGap / 2) * asLeft + disGap, 0);
            canvas.drawPath(mPath[0], mLinePaint);
        } else if (mStrs.length == 2) {
            mPath[0].lineTo(lineStartX + disGap * asLeft, mCircleY - disGap);
            mPath[0].rLineTo((mTextPaint.measureText(mStrs[0]) + disGap / 2) * asLeft, 0);
            canvas.drawPath(mPath[0], mLinePaint);

            mPath[1].lineTo(lineStartX + disGap * asLeft, mCircleY + disGap);
            mPath[1].rLineTo((mTextPaint.measureText(mStrs[1]) + disGap / 2) * asLeft, 0);
            canvas.drawPath(mPath[1], mLinePaint);
        } else {
            mPath[0].lineTo(lineStartX + disGap * asLeft, mCircleY - disGap);
            mPath[0].rLineTo((mTextPaint.measureText(mStrs[0]) + disGap / 2) * asLeft, 0);
            canvas.drawPath(mPath[0], mLinePaint);

            mPath[1].rLineTo((mTextPaint.measureText(mStrs[1]) + disGap * 1.5f) * asLeft, 0);
            canvas.drawPath(mPath[1], mLinePaint);

            mPath[2].lineTo(lineStartX + disGap * asLeft, mCircleY + disGap);
            mPath[2].rLineTo((mTextPaint.measureText(mStrs[2]) + disGap / 2) * asLeft, 0);
            canvas.drawPath(mPath[2], mLinePaint);
        }


    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        if (mStrs.length == 0) return;

        if (asLeft > 0) {   // 原点在左侧, 文字需要在右
            mTextPaint.setTextAlign(Paint.Align.LEFT);
        } else {
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
        }

        if (mStrs.length == 1) {
            canvas.drawText(mStrs[0], lineStartX + disGap * asLeft, mCircleY - mLineDisText, mTextPaint);
        } else if (mStrs.length == 2) {
            canvas.drawText(mStrs[0], lineStartX + disGap * asLeft, mCircleY - disGap - mLineDisText, mTextPaint);
            canvas.drawText(mStrs[1], lineStartX + disGap * asLeft, mCircleY + disGap - mLineDisText, mTextPaint);
        } else {
            canvas.drawText(mStrs[0], lineStartX + disGap * asLeft, mCircleY - disGap - mLineDisText, mTextPaint);
            canvas.drawText(mStrs[1], lineStartX + disGap * asLeft, mCircleY - mLineDisText, mTextPaint);
            canvas.drawText(mStrs[2], lineStartX + disGap * asLeft, mCircleY + disGap - mLineDisText, mTextPaint);
        }
    }

    /**
     * 重置path
     */
    private void reSetPath() {
        if (mPath != null) {
            for (Path path : mPath) {
                path.reset();
                path.moveTo(lineStartX, mCircleY);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAnimator != null)
            mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null)
            mAnimator.end();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = Math.max(widthSize, mixWidth); //widthSize > mixWidth ? widthSize : mixWidth;
        } else {
            width = getPaddingLeft() + mixWidth + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = Math.max(heightSize, mixHeight);//heightSize > mixHeight ? heightSize : mixHeight;
        } else {
            height = getPaddingTop() + mixHeight + getPaddingBottom();
        }
        mCircleY = height / 2;
        setMeasuredDimension(width, height);


    }


    /**
     * 设置文字
     *
     * @param str
     */
    public void setText(String... str) {
        if (str.length > 3) {
            mStrs[0] = str[0];
            mStrs[1] = str[1];
            mStrs[2] = str[2];
        } else {
            this.mStrs = str;
        }
        invalidate();
    }

}
