
package com.basic.code.widget.calendar;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.basic.face.utils.DensityUtils;

/**

 * @since 2019-06-27 16:30
 */
public class SimpleWeekView extends WeekView {

    private int mRadius;
    private int mPadding;
    /**
     * 新数据标记的文本画笔
     */
    private Paint mCirclePaint = new Paint();
    private float mCircleRadius;
    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();
    /**
     * 圆点半径
     */
    private float mPointRadius;

    public SimpleWeekView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
        mCircleRadius = DensityUtils.dp2px(getContext(), 3);
        mPadding = DensityUtils.dp2px(getContext(), 2);
        mPointRadius = DensityUtils.dp2px(context, 2);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint.setFakeBoldText(true);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        boolean isSelected = isSelected(calendar);
        if (isSelected) {
            mPointPaint.setColor(Color.WHITE);
        } else {
            mPointPaint.setColor(Color.GRAY);
        }
        canvas.drawCircle(x + (mItemWidth >> 1), mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        if (hasScheme) {
            mCirclePaint.setColor(calendar.getSchemeColor());
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, mPadding + mCircleRadius, mCircleRadius, mCirclePaint);
        }

        float baselineY = mTextBaseLine;
        int cx = x + mItemWidth / 2;
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mSchemeTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mCurMonthTextPaint);
        }
    }
}
