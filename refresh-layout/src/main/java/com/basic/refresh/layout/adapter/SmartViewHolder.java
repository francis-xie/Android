package com.basic.refresh.layout.adapter;

import android.content.res.Resources;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自识别的Holder
 *

 * @since 2018/8/2 上午11:43
 */
public class SmartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private final SparseArray<View> mViews;

    private final OnItemClickListener mOnItemClickListener;
    private final OnItemLongClickListener mOnItemLongClickListener;
    private int mPosition = -1;

    public SmartViewHolder(@NonNull View itemView) {
        this(itemView, null, null);
    }

    public SmartViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        super(itemView);
        mViews = new SparseArray<>();
        initBackground(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mOnItemClickListener = onItemClickListener;
        mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 设置水波纹背景
     *
     * @param itemView
     */
    protected void initBackground(View itemView) {
        if (itemView.getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = itemView.getContext().getTheme();
            int top = itemView.getPaddingTop();
            int bottom = itemView.getPaddingBottom();
            int left = itemView.getPaddingLeft();
            int right = itemView.getPaddingRight();
            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                itemView.setBackgroundResource(typedValue.resourceId);
            }
            itemView.setPadding(left, top, right, bottom);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            int position = getAdapterPosition();
            if (position >= 0) {
                mOnItemClickListener.onItemClick(v, position);
            } else if (mPosition > -1) {
                mOnItemClickListener.onItemClick(v, mPosition);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            int position = getAdapterPosition();
            if (position >= 0) {
                mOnItemLongClickListener.onItemLongClick(v, position);
            } else if (mPosition > -1) {
                mOnItemLongClickListener.onItemLongClick(v, mPosition);
            }
        }
        return true;
    }

    /**
     * 寻找控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 寻找控件
     *
     * @param id
     * @return
     */
    public View findView(@IdRes int id) {
        return id == 0 ? itemView : findViewById(id);
    }

    /**
     * 设置文字
     *
     * @param id
     * @param sequence
     * @return
     */
    public SmartViewHolder text(int id, CharSequence sequence) {
        View view = findView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    /**
     * 设置文字
     *
     * @param id
     * @param stringRes
     * @return
     */
    public SmartViewHolder text(@IdRes int id, @StringRes int stringRes) {
        View view = findView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(stringRes);
        }
        return this;
    }

    /**
     * 设置文字的颜色
     *
     * @param id
     * @param colorId
     * @return
     */
    public SmartViewHolder textColorId(@IdRes int id, @ColorRes int colorId) {
        View view = findView(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), colorId));
        }
        return this;
    }

    /**
     * 设置图片
     *
     * @param id
     * @param imageId
     * @return
     */
    public SmartViewHolder image(@IdRes int id, int imageId) {
        View view = findView(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(imageId);
        }
        return this;
    }

    /**
     * 设置布局内控件的点击事件【包含索引】
     *
     * @param id
     * @param listener
     * @param position
     * @return
     */
    public SmartViewHolder viewClick(@IdRes int id, final OnViewItemClickListener listener, final int position) {
        View view = findView(id);
        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onViewItemClick(v, position);
                }
            });
        }
        return this;
    }

    /**
     * 设置控件的点击监听
     *
     * @param id
     * @param listener
     * @return
     */
    public SmartViewHolder click(@IdRes int id, final View.OnClickListener listener) {
        View view = findView(id);
        if (listener != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置控件是否可显示
     *
     * @param id
     * @param visibility
     * @return
     */
    public SmartViewHolder visible(@IdRes int id, int visibility) {
        View view = findView(id);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * 设置输入框是否可编辑
     *
     * @param id
     * @param enable
     * @return
     */
    public SmartViewHolder enable(@IdRes int id, boolean enable) {
        View view = findView(id);
        view.setEnabled(enable);
        if (view instanceof EditText) {
            view.setFocusable(enable);
            view.setFocusableInTouchMode(enable);
        }
        return this;
    }

    /**
     * 这是控件选中状态
     *
     * @param id
     * @param checked
     * @return
     */
    public SmartViewHolder checked(@IdRes int id, boolean checked) {
        View view = findView(id);
        if (view instanceof CompoundButton) {
            ((CheckBox) view).setChecked(checked);
        }
        return this;
    }

    /**
     * 设置控件选择监听
     *
     * @param id
     * @param listener
     * @return
     */
    public SmartViewHolder checkedListener(@IdRes int id, CompoundButton.OnCheckedChangeListener listener) {
        View view = findView(id);
        if (view instanceof CompoundButton) {
            ((CheckBox) view).setOnCheckedChangeListener(listener);
        }
        return this;
    }

    /**
     * 设置文字变化监听
     *
     * @param id
     * @param watcher
     * @return
     */
    public SmartViewHolder textListener(@IdRes int id, TextWatcher watcher) {
        View view = findView(id);
        if (view instanceof TextView) {
            ((TextView) view).addTextChangedListener(watcher);
        }
        return this;
    }

    /**
     * 设置背景
     *
     * @param viewId
     * @param resId
     * @return
     */
    public SmartViewHolder backgroundResId(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    /**
     * 清除控件缓存
     */
    public void clearViews() {
        if (mViews != null) {
            mViews.clear();
        }
    }

    /**
     * 条目点击监听
     */
    public interface OnItemClickListener {
        /**
         * 点击
         *
         * @param itemView
         * @param position
         */
        void onItemClick(View itemView, int position);
    }

    /**
     * 条目长按监听
     */
    public interface OnItemLongClickListener {
        /**
         * 长按
         *
         * @param itemView
         * @param position
         */
        void onItemLongClick(View itemView, int position);
    }


    /**
     * 布局内控件点击事件
     */
    public interface OnViewItemClickListener {
        /**
         * 控件被点击
         *
         * @param view
         * @param position
         */
        void onViewItemClick(View view, int position);
    }
}