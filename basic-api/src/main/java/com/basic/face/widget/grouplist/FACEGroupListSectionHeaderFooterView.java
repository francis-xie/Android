
package com.basic.face.widget.grouplist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basic.R;
import com.basic.face.utils.Utils;

/**
 * 用作通用列表 {@link FACEGroupListView} 里每个 {@link FACEGroupListView.Section} 的头部或尾部，也可单独使用。
 */
public class FACEGroupListSectionHeaderFooterView extends LinearLayout {

    private TextView mTextView;

    public FACEGroupListSectionHeaderFooterView(Context context) {
        this(context, null, R.attr.FACEGroupListSectionViewStyle);
    }

    public FACEGroupListSectionHeaderFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.FACEGroupListSectionViewStyle);
    }

    public FACEGroupListSectionHeaderFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FACEGroupListSectionHeaderFooterView(Context context, CharSequence titleText) {
        this(context);
        setText(titleText);
    }

    public FACEGroupListSectionHeaderFooterView(Context context, CharSequence titleText, boolean isFooter) {
        this(context);
        if (isFooter) {
            // Footer View 不需要 padding bottom
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), 0);
        }
        setText(titleText);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.face_layout_group_list_section, this, true);
        setGravity(Gravity.BOTTOM);

        mTextView = findViewById(R.id.group_list_section_header_textView);
    }

    public void setText(CharSequence text) {
        if (Utils.isNullOrEmpty(text)) {
            mTextView.setVisibility(GONE);
        } else {
            mTextView.setVisibility(VISIBLE);
        }
        mTextView.setText(text);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextGravity(int gravity) {
        mTextView.setGravity(gravity);
    }
}
