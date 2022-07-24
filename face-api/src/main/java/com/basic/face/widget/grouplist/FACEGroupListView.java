
package com.basic.face.widget.grouplist;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.IntDef;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.basic.face.R;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.ViewUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 通用的列表, 常用于 App 的设置界面。
 * <p>
 * 注意其父类不是 {@link android.widget.ListView}, 而是 {@link LinearLayout}, 一般需要在外层包多一个 {@link android.widget.ScrollView} 来支持滚动。
 * </p>
 * <p>
 * 提供了 {@link Section} 的概念, 用来将列表分块。 具体见 {@link FACEGroupListView.Section}
 * </p>
 * <p>
 * usage:
 * <pre>
 *         FACEGroupListView groupListView = new FACEGroupListView(context);
 *         // section 1
 *         FACEGroupListView.newSection(context)
 *                 .setTitle("Section Title 1")
 *                 .setDescription("这是Section 1的描述")
 *                 .addItemView(groupListView.createItemView("item 1"), new OnClickListener() {
 *                     {@literal @}Override
 *                     public void onClick(View v) {
 *                         Toast.makeText(context, "section 1 item 1", Toast.LENGTH_SHORT).show();
 *                     }
 *                 })
 *                 .addItemView(groupListView.createItemView("item 2"), new OnClickListener() {
 *                     {@literal @}verride
 *                     public void onClick(View v) {
 *                         Toast.makeText(context, "section 1 item 2", Toast.LENGTH_SHORT).show();
 *                     }
 *                 })
 *                 // 设置分隔线的样式
 *                 .setSeparatorDrawableRes(
 *                         R.drawable.list_group_item_single_bg,
 *                         R.drawable.personal_list_group_item_top_bg,
 *                         R.drawable.list_group_item_bottom_bg,
 *                         R.drawable.personal_list_group_item_middle_bg)
 *                 // 如果没有title,加上默认title【Section n】
 *                 .setUseDefaultTitleIfNone(true)
 *                 // 默认使用TitleView的padding作section分隔,可以设置为false取消它
 *                 .setUseTitleViewForSectionSpace(false)
 *                 .addTo(groupListView);
 *
 *         // section 2
 *         FACEGroupListView.newSection(context)
 *                 .setTitle("Section Title 2")
 *                 .setDescription("这是Section 2的描述")
 *                 .addItemView(groupListView.createItemView("item 1"), new OnClickListener() {
 *                     {@literal @}@Override
 *                     public void onClick(View v) {
 *                         Toast.makeText(context, "section 2 item 1", Toast.LENGTH_SHORT).show();
 *                     }
 *                 })
 *                 .addItemView(groupListView.createItemView("item 2"), new OnClickListener() {
 *                     {@literal @}Override
 *                     public void onClick(View v) {
 *                         Toast.makeText(context, "section 2 item 2", Toast.LENGTH_SHORT).show();
 *                     }
 *                 })
 *                 .addTo(groupListView);
 * </pre>
 *

 * @since 2019/1/3 上午10:24
 */
public class FACEGroupListView extends LinearLayout {

    public static final int SEPARATOR_STYLE_NORMAL = 0;
    public static final int SEPARATOR_STYLE_NONE = 1;
    private int mSeparatorStyle;
    private SparseArray<Section> mSections;

    public FACEGroupListView(Context context) {
        this(context, null, R.attr.FACEGroupListViewStyle);
    }

    public FACEGroupListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.FACEGroupListViewStyle);
    }

    public FACEGroupListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FACEGroupListView, defStyleAttr, 0);
        mSeparatorStyle = array.getInt(R.styleable.FACEGroupListView_face_separatorStyle, SEPARATOR_STYLE_NORMAL);
        array.recycle();

        mSections = new SparseArray<>();
        setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 创建一个 Section。
     *
     * @return 返回新创建的 Section。
     */
    public static Section newSection(Context context) {
        return new Section(context);
    }

    public
    @SeparatorStyle
    int getSeparatorStyle() {
        return mSeparatorStyle;
    }

    /**
     * 设置分割线风格，具体风格可以在 {@link SeparatorStyle} 中选择。
     *
     * @param separatorStyle {@link #SEPARATOR_STYLE_NORMAL} 或 {@link #SEPARATOR_STYLE_NONE} 其中一个值。
     */
    public void setSeparatorStyle(@SeparatorStyle int separatorStyle) {
        mSeparatorStyle = separatorStyle;
    }

    public int getSectionCount() {
        return mSections.size();
    }

    public FACECommonListItemView createItemView(Drawable imageDrawable, CharSequence titleText, String detailText, int orientation, int accessoryType, int height) {
        FACECommonListItemView itemView = new FACECommonListItemView(getContext());
        itemView.setOrientation(orientation);
        itemView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        itemView.setImageDrawable(imageDrawable);
        itemView.setText(titleText);
        itemView.setDetailText(detailText);
        itemView.setAccessoryType(accessoryType);
        return itemView;
    }

    public FACECommonListItemView createItemView(Drawable imageDrawable, CharSequence titleText, String detailText, int orientation, int accessoryType) {
        int height;
        if (orientation == FACECommonListItemView.VERTICAL) {
            height = ThemeUtils.resolveDimension(getContext(), R.attr.face_list_item_height_higher);
            return createItemView(imageDrawable, titleText, detailText, orientation, accessoryType, height);
        } else {
            height = ThemeUtils.resolveDimension(getContext(), R.attr.face_list_item_height);
            return createItemView(imageDrawable, titleText, detailText, orientation, accessoryType, height);
        }
    }

    public FACECommonListItemView createItemView(CharSequence titleText) {
        return createItemView(null, titleText, null, FACECommonListItemView.HORIZONTAL, FACECommonListItemView.ACCESSORY_TYPE_NONE);
    }

    public FACECommonListItemView createItemView(int orientation) {
        return createItemView(null, null, null, orientation, FACECommonListItemView.ACCESSORY_TYPE_NONE);
    }

    /**
     * private, use {@link Section#addTo(FACEGroupListView)}
     * <p>这里只是把section记录到数组里面而已</p>
     */
    private void addSection(Section section) {
        mSections.append(mSections.size(), section);
    }

    /**
     * private，use {@link Section#removeFrom(FACEGroupListView)}
     * <p>这里只是把section从记录的数组里移除而已</p>
     */
    private void removeSection(Section section) {
        for (int i = 0; i < mSections.size(); i++) {
            Section each = mSections.valueAt(i);
            if (each == section) {
                mSections.remove(i);
            }
        }
    }

    public Section getSection(int index) {
        return mSections.get(index);
    }

    @IntDef({SEPARATOR_STYLE_NORMAL, SEPARATOR_STYLE_NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SeparatorStyle {
    }

    /**
     * Section 是组成 {@link FACEGroupListView} 的部分。
     * <ul>
     * <li>每个 Section 可以有多个 item, 通过 {@link #addItemView(FACECommonListItemView, OnClickListener)} 添加。</li>
     * <li>Section 还可以有自己的一个顶部 title 和一个底部 description, 通过 {@link #setTitle(CharSequence)} 和 {@link #setDescription(CharSequence)} 设置。</li>
     * </ul>
     */
    public static class Section {
        private Context mContext;
        private FACEGroupListSectionHeaderFooterView mTitleView;
        private FACEGroupListSectionHeaderFooterView mDescriptionView;
        private SparseArray<FACECommonListItemView> mItemViews;
        private boolean mUseDefaultTitleIfNone;
        private boolean mUseTitleViewForSectionSpace = true;

        private int mSeparatorDrawableForSingle = 0;
        private int mSeparatorDrawableForTop = 0;
        private int mSeparatorDrawableForBottom = 0;
        private int mSeparatorDrawableForMiddle = 0;
        private int mLeftIconWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int mLeftIconHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        public Section(Context context) {
            mContext = context;
            mItemViews = new SparseArray<>();
        }

        /**
         * 对 Section 添加一个 {@link FACECommonListItemView}
         *
         * @param itemView        要添加的 ItemView
         * @param onClickListener ItemView 的点击事件
         * @return Section 本身,支持链式调用
         */
        public Section addItemView(FACECommonListItemView itemView, OnClickListener onClickListener) {
            return addItemView(itemView, onClickListener, null);
        }

        /**
         * 对 Section 添加一个 {@link FACECommonListItemView}
         *
         * @param itemView            要添加的 ItemView
         * @param onClickListener     ItemView 的点击事件
         * @param onLongClickListener ItemView 的长按事件
         * @return Section 本身, 支持链式调用
         */
        public Section addItemView(final FACECommonListItemView itemView, OnClickListener onClickListener, OnLongClickListener onLongClickListener) {
            // 如果本身带有开关控件，点击item时要改变开关控件的状态（开关控件本身已经disable掉）
            if (itemView.getAccessoryType() == FACECommonListItemView.ACCESSORY_TYPE_SWITCH) {
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemView.getSwitch().toggle();
                    }
                });
            } else if (onClickListener != null) {
                itemView.setOnClickListener(onClickListener);
            }

            if (onLongClickListener != null) {
                itemView.setOnLongClickListener(onLongClickListener);
            }

            mItemViews.append(mItemViews.size(), itemView);
            return this;
        }

        /**
         * 设置 Section 的 title
         *
         * @return Section 本身, 支持链式调用
         */
        public Section setTitle(CharSequence title) {
            mTitleView = createSectionHeader(title);
            return this;
        }

        /**
         * 设置 Section 的 description
         *
         * @return Section 本身, 支持链式调用
         */
        public Section setDescription(CharSequence description) {
            mDescriptionView = createSectionFooter(description);
            return this;
        }

        public Section setUseDefaultTitleIfNone(boolean useDefaultTitleIfNone) {
            mUseDefaultTitleIfNone = useDefaultTitleIfNone;
            return this;
        }

        public Section setUseTitleViewForSectionSpace(boolean useTitleViewForSectionSpace) {
            mUseTitleViewForSectionSpace = useTitleViewForSectionSpace;
            return this;
        }

        public Section setSeparatorDrawableRes(int single, int top, int bottom, int middle) {
            mSeparatorDrawableForSingle = single;
            mSeparatorDrawableForTop = top;
            mSeparatorDrawableForBottom = bottom;
            mSeparatorDrawableForMiddle = middle;
            return this;
        }

        public Section setSeparatorDrawableRes(int middle) {
            mSeparatorDrawableForMiddle = middle;
            return this;
        }

        public Section setLeftIconSize(int width, int height) {
            mLeftIconHeight = height;
            mLeftIconWidth = width;
            return this;
        }

        /**
         * 将 Section 添加到 {@link FACEGroupListView} 上
         */
        public void addTo(FACEGroupListView groupListView) {
            if (mTitleView == null) {
                if (mUseDefaultTitleIfNone) {
                    setTitle("Section " + groupListView.getSectionCount());
                } else if (mUseTitleViewForSectionSpace) {
                    setTitle("");
                }
            }
            if (mTitleView != null) {
                groupListView.addView(mTitleView);
            }

            if (groupListView.getSeparatorStyle() == SEPARATOR_STYLE_NORMAL) {
                if (mSeparatorDrawableForSingle == 0) {
                    mSeparatorDrawableForSingle = R.drawable.face_list_item_bg_with_border_double_selector;
                }

                if (mSeparatorDrawableForTop == 0) {
                    mSeparatorDrawableForTop = R.drawable.face_list_item_bg_with_border_double_selector;
                }

                if (mSeparatorDrawableForBottom == 0) {
                    mSeparatorDrawableForBottom = R.drawable.face_list_item_bg_with_border_bottom_selector;
                }

                if (mSeparatorDrawableForMiddle == 0) {
                    mSeparatorDrawableForMiddle = R.drawable.face_list_item_bg_with_border_bottom_selector;
                }
            }

            final int itemViewCount = mItemViews.size();
            FACECommonListItemView.LayoutParamConfig leftIconLpConfig = new FACECommonListItemView.LayoutParamConfig() {
                @Override
                public RelativeLayout.LayoutParams onConfig(RelativeLayout.LayoutParams lp) {
                    lp.width = mLeftIconWidth;
                    lp.height = mLeftIconHeight;
                    return lp;
                }
            };
            for (int i = 0; i < itemViewCount; i++) {
                FACECommonListItemView itemView = mItemViews.get(i);
                int resDrawableId;
                if (groupListView.getSeparatorStyle() == SEPARATOR_STYLE_NORMAL) {
                    if (itemViewCount == 1) {
                        resDrawableId = mSeparatorDrawableForSingle;
                    } else if (i == 0) {
                        resDrawableId = mSeparatorDrawableForTop;
                    } else if (i == itemViewCount - 1) {
                        resDrawableId = mSeparatorDrawableForBottom;
                    } else {
                        resDrawableId = mSeparatorDrawableForMiddle;
                    }
                } else {
                    resDrawableId = R.drawable.face_list_item_bg_with_border_none_selector;
                }
                itemView.updateImageViewLp(leftIconLpConfig);
                ViewUtils.setBackgroundKeepingPadding(itemView, resDrawableId);
                groupListView.addView(itemView);
            }

            if (mDescriptionView != null) {
                groupListView.addView(mDescriptionView);
            }
            groupListView.addSection(this);
        }

        public void removeFrom(FACEGroupListView parent) {
            if (mTitleView != null && mTitleView.getParent() == parent) {
                parent.removeView(mTitleView);
            }
            for (int i = 0; i < mItemViews.size(); i++) {
                FACECommonListItemView itemView = mItemViews.get(i);
                parent.removeView(itemView);
            }
            if (mDescriptionView != null && mDescriptionView.getParent() == parent) {
                parent.removeView(mDescriptionView);
            }
            parent.removeSection(this);
        }

        /**
         * 创建 Section Header，每个 Section 都会被创建一个 Header，有 title 时会显示 title，没有 title 时会利用 header 的上下 padding 充当 Section 分隔条
         */
        public FACEGroupListSectionHeaderFooterView createSectionHeader(CharSequence titleText) {
            return new FACEGroupListSectionHeaderFooterView(mContext, titleText);
        }

        /**
         * Section 的 Footer，形式与 Header 相似，都是显示一段文字
         */
        public FACEGroupListSectionHeaderFooterView createSectionFooter(CharSequence text) {
            return new FACEGroupListSectionHeaderFooterView(mContext, text, true);
        }
    }

}
