package com.tangjd.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tangjd.common.R;
import com.tangjd.common.utils.DisplayUtils;

/**
 * 验证码控件
 */
public class IdentifyView extends View {
    private String mVerifyCode = null;
    private Paint mPaint = new Paint();
    private final int mPointNum;
    private final int mLineNum;
    private final int mTextLength;
    private final float mTextSize;
    private final int mTextColor;
    private final int mBgColor;

    public IdentifyView(Context context) {
        this(context, null);
    }

    public IdentifyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IdentifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IdentifyView);
        // 获取随机点的个数
        mPointNum = a.getInteger(R.styleable.IdentifyView_identify_point_num, 100);
        // 获取随机线的条数
        mLineNum = a.getInteger(R.styleable.IdentifyView_identify_line_num, 5);
        // 获取验证码长度
        mTextLength = a.getInteger(R.styleable.IdentifyView_identify_text_length, 4);
        // 获取验证码字体大小
        mTextSize = a.getDimension(R.styleable.IdentifyView_identify_text_size, DisplayUtils.spToPx(16, context));
        // 获取验证码字体颜色
        mTextColor = a.getColor(R.styleable.IdentifyView_identify_text_color, Color.BLACK);
        // 获取背景颜色
        mBgColor = a.getColor(R.styleable.IdentifyView_identify_bg_color, Color.WHITE);
        a.recycle();

        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(mTextColor);

        mVerifyCode = makeVerifyCode();
    }


    @Override
    public void onDraw(Canvas canvas) {
        // 生成验证码
        // 设置二维码背景色
        canvas.drawColor(mBgColor);

        final int height = getHeight();
        // 获得CheckView控件的高度
        final int width = getWidth();
        // 获得CheckView控件的宽度
        int dx = width / mTextLength / 2;

        char[] checkNum = mVerifyCode.toCharArray();
        for (int i = 0; i < mTextLength; i++) {
            // 绘制验证控件上的文本
            canvas.drawText("" + checkNum[i], dx, getPosition(height), mPaint);
            dx += width / (mTextLength + 1);
        }
        int[] line;
        for (int i = 0; i < mLineNum; i++) {
            // 划线
            line = getLine(height, width);
            canvas.drawLine(line[0], line[1], line[2], line[3], mPaint);
        }
        // 绘制小圆点
        int[] point;
        for (int i = 0; i < mPointNum; i++) {
            // 画点
            point = getPoint(height, width);
            canvas.drawCircle(point[0], point[1], 1, mPaint);
        }
    }

    /**
     * 生成新的验证码
     */
    public void updateVerifyCode() {
        mVerifyCode = makeVerifyCode();
        invalidate();
    }

    public String getVerifyCode() {
        return mVerifyCode;
    }

    /**
     * Make new random verify code
     *
     * @return new verify code
     */
    private String makeVerifyCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mTextLength; i++) {
            int temp = (int) (Math.random() * 10);
            sb.append(temp);
        }
        return sb.toString();
    }

    /**
     * 验证码绘制的y点，保证绘制的视图内
     *
     * @param height IdentifyView的高度
     */
    private int getPosition(int height) {
        int position = (int) (Math.random() * height);
        if (position < mTextSize) {
            position += mTextSize;
        }
        if (position > height) {
            position = height;
        }
        return position;
    }


    /**
     * 随机产生划线的起始点坐标和结束点坐标
     *
     * @param height 控件的高
     * @param width  控件的宽
     * @return 起始点坐标和结束点坐标
     */
    public static int[] getLine(int height, int width) {
        int[] tempCheckNum = {0, 0, 0, 0};
        for (int i = 0; i < 4; i += 2) {
            tempCheckNum[i] = (int) (Math.random() * width);
            tempCheckNum[i + 1] = (int) (Math.random() * height);
        }
        return tempCheckNum;
    }

    /**
     * 随机产生点的圆心点坐标
     *
     * @param height 控件的高
     * @param width  控件的宽
     */
    public static int[] getPoint(int height, int width) {
        int[] tempCheckNum = {0, 0, 0, 0};
        tempCheckNum[0] = (int) (Math.random() * width);
        tempCheckNum[1] = (int) (Math.random() * height);
        return tempCheckNum;
    }
}
