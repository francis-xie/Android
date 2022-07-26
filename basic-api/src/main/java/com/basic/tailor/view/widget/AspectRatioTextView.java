package com.basic.tailor.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.basic.R;
import com.basic.tailor.model.AspectRatio;
import com.basic.tailor.view.TailorImageView;

import java.util.Locale;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class AspectRatioTextView extends TextView {

    private final Rect mCanvasClipBounds = new Rect();
    private Paint mDotPaint;
    private int mDotSize;
    private float mAspectRatio;

    private String mAspectRatioTitle;
    private float mAspectRatioX, mAspectRatioY;

    public AspectRatioTextView(Context context) {
        this(context, null);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tailor_AspectRatioTextView);
        init(a);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tailor_AspectRatioTextView);
        init(a);
    }

    /**
     * @param activeColor the resolved color for active elements
     */

    public void setActiveColor(@ColorInt int activeColor) {
        applyActiveColor(activeColor);
        invalidate();
    }

    public void setAspectRatio(@NonNull AspectRatio aspectRatio) {
        mAspectRatioTitle = aspectRatio.getAspectRatioTitle();
        mAspectRatioX = aspectRatio.getAspectRatioX();
        mAspectRatioY = aspectRatio.getAspectRatioY();

        if (mAspectRatioX == TailorImageView.SOURCE_IMAGE_ASPECT_RATIO || mAspectRatioY == TailorImageView.SOURCE_IMAGE_ASPECT_RATIO) {
            mAspectRatio = TailorImageView.SOURCE_IMAGE_ASPECT_RATIO;
        } else {
            mAspectRatio = mAspectRatioX / mAspectRatioY;
        }

        setTitle();
    }

    public float getAspectRatio(boolean toggleRatio) {
        if (toggleRatio) {
            toggleAspectRatio();
            setTitle();
        }
        return mAspectRatio;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isSelected()) {
            canvas.getClipBounds(mCanvasClipBounds);
            canvas.drawCircle((mCanvasClipBounds.right - mCanvasClipBounds.left) / 2.0f, mCanvasClipBounds.bottom - mDotSize,
                    mDotSize / 2, mDotPaint);
        }
    }

    @SuppressWarnings("deprecation")
    private void init(@NonNull TypedArray a) {
        setGravity(Gravity.CENTER_HORIZONTAL);

        mAspectRatioTitle = a.getString(R.styleable.tailor_AspectRatioTextView_tailor_artv_ratio_title);
        mAspectRatioX = a.getFloat(R.styleable.tailor_AspectRatioTextView_tailor_artv_ratio_x, TailorImageView.SOURCE_IMAGE_ASPECT_RATIO);
        mAspectRatioY = a.getFloat(R.styleable.tailor_AspectRatioTextView_tailor_artv_ratio_y, TailorImageView.SOURCE_IMAGE_ASPECT_RATIO);

        if (mAspectRatioX == TailorImageView.SOURCE_IMAGE_ASPECT_RATIO || mAspectRatioY == TailorImageView.SOURCE_IMAGE_ASPECT_RATIO) {
            mAspectRatio = TailorImageView.SOURCE_IMAGE_ASPECT_RATIO;
        } else {
            mAspectRatio = mAspectRatioX / mAspectRatioY;
        }

        mDotSize = getContext().getResources().getDimensionPixelSize(R.dimen.tailor_size_dot_scale_text_view);
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL);

        setTitle();

        int activeColor = getResources().getColor(R.color.tailor_color_widget_active);
        applyActiveColor(activeColor);

        a.recycle();
    }

    private void applyActiveColor(@ColorInt int activeColor) {
        if (mDotPaint != null) {
            mDotPaint.setColor(activeColor);
        }
        ColorStateList textViewColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{0}
                },
                new int[]{
                        activeColor,
                        ContextCompat.getColor(getContext(), R.color.tailor_color_widget)
                }
        );

        setTextColor(textViewColorStateList);
    }

    private void toggleAspectRatio() {
        if (mAspectRatio != TailorImageView.SOURCE_IMAGE_ASPECT_RATIO) {
            float tempRatioW = mAspectRatioX;
            mAspectRatioX = mAspectRatioY;
            mAspectRatioY = tempRatioW;

            mAspectRatio = mAspectRatioX / mAspectRatioY;
        }
    }

    private void setTitle() {
        if (!TextUtils.isEmpty(mAspectRatioTitle)) {
            setText(mAspectRatioTitle);
        } else {
            setText(String.format(Locale.US, "%d:%d", (int) mAspectRatioX, (int) mAspectRatioY));
        }
    }

}
