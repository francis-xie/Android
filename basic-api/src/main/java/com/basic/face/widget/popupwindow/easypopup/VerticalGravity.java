package com.basic.face.widget.popupwindow.easypopup;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 垂直布局方式
 */
@IntDef({
        VerticalGravity.CENTER,
        VerticalGravity.ABOVE,
        VerticalGravity.BELOW,
        VerticalGravity.ALIGN_TOP,
        VerticalGravity.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface VerticalGravity {
    int CENTER = 0;
    int ABOVE = 1;
    int BELOW = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
