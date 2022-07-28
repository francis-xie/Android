package com.basic.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import com.basic.tag.animate.*;
import com.basic.tag.anno.NorIcons;
import com.basic.tag.anno.SeleIcons;
import com.basic.tag.anno.Titles;

import java.lang.reflect.Field;

/**
 * 主要的底部导航操作类,控制导航的行为(显示隐藏徽章等等)
 */
public class TagBar extends LinearLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {
    //默认的图标大小
    private static final int DEFAULT_ICON_SIZE = 25;
    //字体默认大小
    private static final int DEFAULT_TEXT_SIZE = 10;
    //默认的没有选中的文字和图标的颜色
    private static final int DEFAULT_NORMAL_COLOR = 0xFFC2B7B8;
    //默认的上下背景间隔
    private static final int DEFAULT_MARGIN = 3;
    //默认的选中颜色
    private static final int DEFAULT_SELECT_COLOR = 0xFFFF9915;
    //默认是否接受颜色随着字体变化
    private static final boolean DEFAULT_ACCEPT_FILTER = true;
    //默认是否允许按住动画的变化
    private static final boolean DEFAULT_PRESS_ANIMATE = true;
    //默认是否开启字体图标渐变动画
    private static final boolean DEFAULT_GRADIENT = false;
    //默认是否开启页面动画
    private static final boolean DEFAULT_PAGE_ANIMATE = false;
    //默认的徽章背景颜色
    private static final int DEFAULT_BADGE_COLOR = 0xFFFF0000;
    //默认的徽章字体大小
    private static final int DEFAULT_BADGE_TEXT_SIZE = 10;
    //默认的徽章狂站距离
    private static final int DEFAULT_PADDING = 4;
    //默认徽章距离右边间距
    private static final int DEFAULT_BADGE_HORIZONTAL_MARGIN = 20;
    //默认徽章距离上面间距
    private static final int DEFAULT_BADGE_VERTICAL_MARGIN = 3;
    //默认中间图标底部距离
    private static final int DEFAULT_MIDDLE_ICON_BOTTOM = 5;
    //默认中间的左右间距
    private static final int DEFAULT_MIDDLE_MARGIN = 40;

    private Context mContext;

    private TypedArray mAttribute;
    // 选中的当前Tab的位置
    private int mSelectIndex;
    // 标题的数组
    private String[] mTitles;
    // 没有选中的图标数组
    private int[] mNormalIcons;
    // 选中的图标数组
    private int[] mSelectedIcons;
    //所有Tabitem
    private TagItem[] mTagItems;
    // 中间按钮
    private View mMiddleItem;
    // 监听点击Tab回调的观察者
    private OnTabSelectListener mTabSelectLis;
    //判断是否需要动画,解决Viewpager回调onpageChange冲突事件
    private boolean mNeedAnimate = true;
    // Tab对应的ViewPager
    private ViewPager mTabPager;
    // 渐变判断(用于滑动的渐变)
    private boolean mGradientEnable;
    //是否需要页面滚动动画
    private boolean mPageAnimateEnable;
    // 是否需要按住动画
    private boolean mPressAnimateEnable;
    //判断是否拖了徽章
    private boolean mDraggedBadge;

    public TagBar(Context context) {
        super(context);
        init(context, null);
    }

    public TagBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化TabBar
     *
     * @param context 上下文
     * @param set     XML结点集合
     */
    private void init(Context context, AttributeSet set) {
        mContext = context;
        mAttribute = context.obtainStyledAttributes(set, R.styleable.TagBar);
        setMinimumHeight(DensityUtils.dp2px(mContext, 48));
        boolean haveAnno = reflectAnnotation();
        if (haveAnno) {
            initFromAttribute();
        }
    }

    /**
     * 从类获取注解,映射值到mTiles,mNormalIcons,mSelectedIcons
     *
     * @return boolean 表示是否有注解的存在
     */
    private boolean reflectAnnotation() {
        int total = 0;//表示获得注解的总数
        //反射注解
        Field[] fields = mContext.getClass().getDeclaredFields();
        //遍历所有字段,寻找标记
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Titles.class)) {
                try {
                    if (field.get(mContext).getClass().equals(String[].class)) {
                        mTitles = (String[]) field.get(mContext);
                    } else if (field.get(mContext).getClass().equals(int[].class)) {
                        int[] title_Res = (int[]) field.get(mContext);
                        mTitles = new String[title_Res.length];
                        for (int i = 0; i < title_Res.length; i++) {
                            mTitles[i] = mContext.getString(title_Res[i]);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (mTitles != null) total++;
            } else if (field.isAnnotationPresent(NorIcons.class)) {
                try {
                    mNormalIcons = (int[]) field.get(mContext);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (mNormalIcons != null) total++;
            } else if (field.isAnnotationPresent(SeleIcons.class)) {
                try {
                    mSelectedIcons = (int[]) field.get(mContext);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (mSelectedIcons != null) total++;
            }
        }
        return total > 0;
    }


    /**
     * 获取所有节点属性
     */
    private void initFromAttribute() {
        int normalColor = mAttribute.getColor(R.styleable.TagBar_tab_normalColor, DEFAULT_NORMAL_COLOR);
        int selectColor = mAttribute.getColor(R.styleable.TagBar_tab_selectColor, DEFAULT_SELECT_COLOR);
        int textSize = DensityUtils.px2sp(mContext, mAttribute.getDimensionPixelSize(R.styleable.TagBar_tab_textSize, DensityUtils.sp2px(mContext, DEFAULT_TEXT_SIZE)));
        int iconSize = mAttribute.getDimensionPixelSize(R.styleable.TagBar_tab_iconSize, DensityUtils.dp2px(mContext, DEFAULT_ICON_SIZE));
        int iconPadding = mAttribute.getDimensionPixelSize(R.styleable.TagBar_tab_iconPadding, 0);
        int margin = mAttribute.getDimensionPixelOffset(R.styleable.TagBar_tab_margin, DensityUtils.dp2px(mContext, DEFAULT_MARGIN));
        AnimationType AnimateType = AnimationType.values()[mAttribute.getInt(R.styleable.TagBar_tab_animate, AnimationType.NONE.ordinal())];
        int BadgeColor = mAttribute.getColor(R.styleable.TagBar_tab_badgeColor, DEFAULT_BADGE_COLOR);
        int BadgeTextSize = DensityUtils.px2sp(mContext, mAttribute.getDimensionPixelSize(R.styleable.TagBar_tab_badgeTextSize, DensityUtils.sp2px(mContext, DEFAULT_BADGE_TEXT_SIZE)));
        int badgePadding = DensityUtils.px2dp(mContext, mAttribute.getDimensionPixelOffset(R.styleable.TagBar_tab_badgePadding, DensityUtils.dp2px(mContext, DEFAULT_PADDING)));
        int badgeVerMargin = DensityUtils.px2dp(mContext, mAttribute.getDimensionPixelOffset(R.styleable.TagBar_tab_badgeVerticalMargin, DensityUtils.dp2px(mContext, DEFAULT_BADGE_VERTICAL_MARGIN)));
        int badgeHorMargin = DensityUtils.px2dp(mContext, mAttribute.getDimensionPixelOffset(R.styleable.TagBar_tab_badgeHorizontalMargin, DensityUtils.dp2px(mContext, DEFAULT_BADGE_HORIZONTAL_MARGIN)));
        mPageAnimateEnable = mAttribute.getBoolean(R.styleable.TagBar_tab_pageAnimateEnable, DEFAULT_PAGE_ANIMATE);
        mGradientEnable = mAttribute.getBoolean(R.styleable.TagBar_tab_gradientEnable, DEFAULT_GRADIENT);
        mPressAnimateEnable = mAttribute.getBoolean(R.styleable.TagBar_tab_pressAnimateEnable, DEFAULT_PRESS_ANIMATE);
        int hMargin = mAttribute.getDimensionPixelOffset(R.styleable.TagBar_tab_middleHMargin, DensityUtils.dp2px(mContext, DEFAULT_MIDDLE_MARGIN));
        String typeFacePath = mAttribute.getString(R.styleable.TagBar_tab_typeface);
        boolean acceptFilter = mAttribute.getBoolean(R.styleable.TagBar_tab_iconFilter, DEFAULT_ACCEPT_FILTER);
        Drawable tabSelectBg = mAttribute.getDrawable(R.styleable.TagBar_tab_selectBg);
        //假如所有都为空默认已经开启了
        if (!isInEditMode()) {
            CheckIfAssertError(mTitles, mNormalIcons, mSelectedIcons);
            //计算Tab的宽度
            mTagItems = new TagItem[mNormalIcons.length];
            //实例化TabItem添加进去
            for (int i = 0; i < mTagItems.length; i++) {
                final int temp = i;
                Animatable animator = AnimateType == AnimationType.SCALE ? new ScaleAnimater() : AnimateType == AnimationType.ROTATE ? new RotateAnimator() :
                        AnimateType == AnimationType.FLIP ? new FlipAnimator() : AnimateType == AnimationType.JUMP ? new JumpAnimator() : AnimateType == AnimationType.SCALE2 ? new Scale2Animator() : null;
                mTagItems[i] = new TagItem.Builder(mContext).setTitle(mTitles == null ? null : mTitles[i]).setIndex(temp).setTextSize(textSize)
                        .setTypeFacePath(typeFacePath).setNormalColor(normalColor).setSelectBg(tabSelectBg).setBadgeColor(BadgeColor)
                        .setBadgeTextSize(BadgeTextSize).setNormalIcon(mNormalIcons[i]).setIconPadding(iconPadding).setSelectedColor(selectColor).setBadgeHorMargin(badgeHorMargin)
                        .setBadgePadding(badgePadding).setIconSize(iconSize).setIconFilter(acceptFilter).setBadgeVerMargin(badgeVerMargin).setMargin(margin).setAnimator(animator)
                        .setSelectIcon(mSelectedIcons == null ? 0 : mSelectedIcons[i]).build();
                mTagItems[i].setTag(temp);
                mTagItems[i].setOnTouchListener(this);
                addView(mTagItems[i]);
                //判断是不是准备到中间的tab,假如设置了中间图标就添加进去
                if (i == (mTagItems.length / 2 - 1) && mAttribute.getResourceId(R.styleable.TagBar_tab_middleView, 0) != 0) {
                    //添加中间的占位距离控件
                    View middleView = new View(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(hMargin, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.leftMargin = hMargin / 2;
                    params.rightMargin =  hMargin / 2;
                    middleView.setLayoutParams(params);
                    addView(middleView);
                }
            }

            for (int i = 1; i < mTagItems.length; i++) {
                mTagItems[i].setSelect(false, false);
            }
            mTagItems[0].setSelect(true, true, false);

        }
    }

    /**
     * 切换Tab页面,是否带动画
     */
    private void setSelectTab(int index, boolean animated) {
        if (mTagItems == null || index > mTagItems.length - 1) return;
        mSelectIndex = index;
        for (int i = 0; i < mTagItems.length; i++) {
            if (i == index) {
                continue;
            }
            if (!mTagItems[i].isSelect()) {
                mTagItems[i].setSelect(false, animated);
            } else {
                mTagItems[i].setSelect(false, animated);
            }
        }
        mTagItems[index].setSelect(true, animated);
        if (mTabSelectLis != null) {
            mTabSelectLis.onTabSelect(index);
        }
    }

    /**
     * 判断有没有声明变量的错误
     *
     * @param titles       标题
     * @param normalIcon   没有选中的图标
     * @param selectedIcon 选中的图标
     */
    private void CheckIfAssertError(String[] titles, int[] normalIcon, int[] selectedIcon) {
        if (normalIcon == null) {
            throw new TabException("you must set the NormalIcon for the TagBar!!!");
        }
        int originlen = normalIcon.length;
        //判断注解里面的数组长度是否一样
        if ((mTitles != null && originlen != titles.length)
                || (selectedIcon != null && originlen != selectedIcon.length)) {
            throw new TabException("Every Array length is not equal,Please Check Your Annotation in your Activity,Ensure Every Array length is equals!");
        }
    }


    private void BuildMiddleView() {
        int layout_res = mAttribute.getResourceId(R.styleable.TagBar_tab_middleView, 0);
        if (layout_res == 0) {
            return;
        }
        mMiddleItem = LayoutInflater.from(mContext).inflate(layout_res, (ViewGroup) getParent(), false);
        //给中间自定义View填充额外参数,令布局在父View的中间和最下边的位置(父View指的是TabBar的父控件)
        fillMiddleParams();
    }

    //判断触摸位置是否在View的区域
    private boolean isInRect(View v, MotionEvent e) {
        float x = e.getRawX();
        float y = e.getRawY();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        RectF rectF = new RectF(location[0], location[1], location[0] + v.getWidth(), location[1] + v.getHeight());
        return rectF.contains(x, y);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mMiddleItem == null) {
            BuildMiddleView();
        }
        mAttribute.recycle();
    }

    private void fillMiddleParams() {
        int bottom_dis = mAttribute.getDimensionPixelSize(R.styleable.TagBar_tab_middleBottomDis, DensityUtils.dp2px(mContext, DEFAULT_MIDDLE_ICON_BOTTOM));
        if (getParent().getClass().equals(RelativeLayout.class)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mMiddleItem.getLayoutParams();
            params.setMargins(0, 0, 0, bottom_dis);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mMiddleItem.setLayoutParams(params);
        } else if (getParent().getClass().equals(FrameLayout.class)) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mMiddleItem.getLayoutParams();
            params.setMargins(0, 0, 0, bottom_dis);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            mMiddleItem.setLayoutParams(params);
        }
        ((ViewGroup) getParent()).addView(mMiddleItem);
    }

    private void updateTitles(String... titles) {
        for (int i = 0; i < mTitles.length; i++) {
            if (!titles[i].equals(mTitles[i])) {
                mTagItems[i].setTitle(titles[i]);
            }
        }
        mTitles = titles;
    }

    /****-------提供给开发者调用的方法---------****/

    /**
     * 获取选中的Tab
     */
    public TagItem getSelectedTab() {
        for (int i = 0; i < mTagItems.length; i++) {
            if (mTagItems[i].isSelect()) {
                return mTagItems[i];
            }
        }
        return null;
    }

    /**
     * 获取第几个Tab
     */
    public TagItem getTabAtPosition(int pos) {
        if (pos < 0 || pos >= mTagItems.length) {
            throw new TabException("invalid position parameter");
        }
        return mTagItems[pos];
    }

    /**
     * 切换Tab页面
     */
    public void setSelectTab(int index) {
        setSelectTab(index, true);
    }

    /**
     * 得到TabItem的数量
     */
    public int getTabsCount() {
        return mTagItems == null ? 0 : mTagItems.length;
    }

    /**
     * 设置容器和TabBar联系在一起
     */
    public void setContainer(ViewPager pager) {
        if (pager != null) {
            mTabPager = pager;
            mTabPager.setOnPageChangeListener(this);
        }
    }

    /**
     * 显示图标的Badge，默认不可拖动
     */
    public void showBadge(int pos, String text) {
        showBadge(pos, text, false);
    }

    /**
     * 显示图标的Badge
     */
    public void showBadge(int pos, String text, boolean draggable) {
        if (mTagItems != null) {
            mTagItems[pos].showTextBadge(text);
            mTagItems[pos].getBadgeViewHelper().setDragable(draggable);
        }
    }

    /**
     * 显示圆点徽章,默认为不可拖动
     */
    public void showCircleBadge(int pos) {
        showCircleBadge(pos, false);
    }

    /**
     * 显示圆点徽章,是否可以拖动
     */
    public void showCircleBadge(int pos, boolean draggable) {
        if (pos < 0 || pos >= mTagItems.length) {
            throw new TabException("invalid position parameter");
        }
        if (mTagItems != null) {
            mTagItems[pos].showCirclePointBadge();
            mTagItems[pos].getBadgeViewHelper().setDragable(draggable);
        }
    }

    /**
     * 隐藏徽章
     *
     * @param position
     */
    public void hideBadge(int position) {
        if (position < 0 || position >= mTagItems.length) {
            throw new TabException("invalid position parameter");
        }
        if (mTagItems != null)
            mTagItems[position].hiddenBadge();
    }

    /**
     * 设置标题数组
     *
     * @param titles
     */
    public TagBar setTitles(String... titles) {
        if (mTitles == null) {
            this.mTitles = titles;
        } else if (mTitles.length <= titles.length) {
            updateTitles(titles);
        }

        return this;
    }

    public TagBar setTitles(int... titles) {
        if (titles != null && titles.length > 0) {
            //转换为字符串数组
            String[] strArray = new String[titles.length];
            for (int i = 0; i < titles.length; i++) {
                strArray[i] = mContext.getString(titles[i]);
            }

            if (mTitles == null) {
                mTitles = strArray;
            } else if (mTitles.length <= titles.length) {
                updateTitles(strArray);
            }

        }
        return this;
    }

    public void setTitle(int pos, String title) {
        if (pos >= 0 && pos < mTagItems.length) {
            mTagItems[pos].setTitle(title);
        }
    }

    /**
     * 设置为选中的图标数组
     */
    public TagBar setNormalIcons(int... normalIcons) {
        if (mNormalIcons == null) {
            this.mNormalIcons = normalIcons;
        } else if (mNormalIcons.length <= normalIcons.length) {
            for (int i = 0; i < mNormalIcons.length; i++) {
                mTagItems[i].setNormalIcon(normalIcons[i]);
            }
            mNormalIcons = normalIcons;
        }

        return this;
    }

    public void setNormalIcon(int pos, int normalIcon) {
        if (pos >= 0 && pos < mTagItems.length) {
            mTagItems[pos].setNormalIcon(normalIcon);
        }
    }

    /**
     * 设置选中图标
     */
    public TagBar setSelectedIcons(int... selectedIcons) {
        if (mSelectedIcons == null) {
            this.mSelectedIcons = selectedIcons;
        } else if (mSelectedIcons.length <= selectedIcons.length) {
            for (int i = 0; i < mSelectedIcons.length; i++) {
                mTagItems[i].setSelectIcon(selectedIcons[i]);
            }
            mSelectedIcons = selectedIcons;
        }
        return this;
    }

    public void setSelectedIcon(int pos, int selectedIcon) {
        if (pos >= 0 && pos < mTagItems.length) {
            mTagItems[pos].setSelectIcon(selectedIcon);
        }
    }

    /**
     * 生成TabItem
     */
    public void generate() {
        if (mTagItems == null) {
            initFromAttribute();
        }
    }

    public void setTabTypeFace(Typeface typeFace) {
        for (TagItem tabItem : mTagItems) {
            tabItem.setTypeFace(typeFace);
        }
    }

    /*
     * 设置Tab标题字体类型
     */
    public void setTabTypeFace(String TypeFacepath) {
        for (TagItem tabItem : mTagItems) {
            tabItem.setTypeFace(Typeface.createFromAsset(mContext.getAssets(), TypeFacepath));
        }
    }

    /**
     * 获得选中的位置
     */
    public int getSelectPosition() {
        return mSelectIndex;
    }

    /**
     * 获取徽章是否在显示
     */
    public boolean isBadgeShow(int index) {
        if (mTagItems != null)
            return mTagItems[index].isBadgeShow();
        return false;
    }

    /**
     * 获得中间的TABItem
     *
     * @return
     */
    public View getMiddleView() {
        //解决开发者在activity引用了注解并且在oncreate方法调用这个方法空指针的问题
        if (mMiddleItem == null) {
            BuildMiddleView();
        }
        return mMiddleItem;
    }

    /**
     * 改变图标大小
     */
    public void setIconSize(int size) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.getIconView().getLayoutParams().width = DensityUtils.dp2px(mContext, size);
                item.getIconView().getLayoutParams().height = DensityUtils.dp2px(mContext, size);
            }
        }
    }

    /**
     * 改变TabBar上边距
     */
    public void setTabMargin(int margin) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                ((RelativeLayout.LayoutParams) item.getIconView().getLayoutParams()).topMargin = DensityUtils.dp2px(mContext, margin);
            }
        }
    }

    /**
     * 改变普通颜色(包括字体和图标)
     */
    public void setNormalColor(@ColorInt int color) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.setNormalColor(color);
            }
        }
    }

    /**
     * 改变选中颜色(包括字体和图标)
     */
    public void setSelectedColor(@ColorInt int color) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.setSelectedColor(color);
            }
        }
    }

    /**
     * 改变文字大小
     */
    public void setTabTextSize(int textSize) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.setTextSize(DensityUtils.sp2px(mContext, textSize));
            }
        }
    }

    public void setBadgeColor(@ColorInt int badgeColor) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.getBadgeViewHelper().setBadgeBgColorInt(badgeColor);
            }
        }
    }

    public void setBadgeHorMargin(@ColorInt int horMargin) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.getBadgeViewHelper().setBadgeHorizontalMarginDp(horMargin);
            }
        }
    }

    public void setBadgeTextSize(@ColorInt int badgeTextSize) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.getBadgeViewHelper().setBadgeTextSizeSp(badgeTextSize);
            }
        }
    }

    public void setBadgePadding(@ColorInt int padding) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.getBadgeViewHelper().setBadgePaddingDp(padding);
            }
        }
    }

    public void setBadgeVerMargin(@ColorInt int verMargin) {
        if (mTagItems != null) {
            for (TagItem item : mTagItems) {
                item.getBadgeViewHelper().setBadgeVerticalMarginDp(verMargin);
            }
        }
    }

    public void setPageAnimateEnable(boolean enable) {
        mPageAnimateEnable = enable;
    }

    public void setGradientEnable(boolean enable) {
        mGradientEnable = enable;
    }

    /**
     * 设置动画
     */
    public void setAnimation(AnimationType animationType) {
        for (int i = 0; i < mTagItems.length; i++) {
            mTagItems[i].setAnimator(animationType == AnimationType.SCALE ? new ScaleAnimater() : animationType == AnimationType.ROTATE ? new RotateAnimator() :
                    animationType == AnimationType.JUMP ? new JumpAnimator() : animationType == AnimationType.FLIP ? new FlipAnimator() : animationType == AnimationType.SCALE2 ? new Scale2Animator() : null);
        }
    }


    /**
     * 设置点击TabBar事件的观察者
     */
    public void setTabListener(OnTabSelectListener listener) {
        mTabSelectLis = listener;
    }

    /**
     * 设置badgeView消失的回调事件
     */
    public void setDismissListener(BadgeDismissListener listener) {
        if (mTagItems != null)
            for (TagItem item : mTagItems) {
                item.setDismissDelegate(listener);
            }
    }

    /****---------------****/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mTagItems == null || position > mTagItems.length - 1 || 1 + position > mTagItems.length - 1) return;
        if (positionOffset > 0f) {
            if (mGradientEnable) {
                mTagItems[position].changeAlpha(1 - positionOffset);
                mTagItems[position + 1].changeAlpha(positionOffset);
            }

            if (mTagItems[position].getAnimator() != null && mPageAnimateEnable) {
                if (mTagItems[position].getAnimator().isNeedPageAnimate()) {
                    mNeedAnimate = false;
                    mTagItems[position].getAnimator().onPageAnimate(mTagItems[position].getIconView(), 1 - positionOffset);
                    mTagItems[position + 1].getAnimator().onPageAnimate(mTagItems[position + 1].getIconView(), positionOffset);
                } else {
                    mNeedAnimate = true;
                }
            } else mNeedAnimate = true;
        }
    }

    @Override
    public void onPageSelected(int position) {
        setSelectTab(position, mNeedAnimate);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            mNeedAnimate = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int temp = (int) v.getTag();
        TagItem tabItem = (TagItem) v;
        if (tabItem.isSelect()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDraggedBadge = (tabItem).getBadgeViewHelper().checkDragging(event);
                if (!mDraggedBadge && mTagItems[mSelectIndex].getAnimator() != null&&mPressAnimateEnable) {
                    mTagItems[mSelectIndex].getAnimator().onPressDown(mTagItems[mSelectIndex].getIconView(), true);
                    tabItem.getAnimator().onPressDown((tabItem).getIconView(), false);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mDraggedBadge) {
                    break;
                }
                if (!isInRect(v, event) || (mTabSelectLis != null && mTabSelectLis.onInterruptSelect(temp))) {
                    if (mTagItems[mSelectIndex].getAnimator() != null&&mPressAnimateEnable) {
                        mTagItems[mSelectIndex].getAnimator().onTouchOut(mTagItems[mSelectIndex].getIconView(), true);
                        tabItem.getAnimator().onTouchOut((tabItem.getIconView()), false);
                    }

                } else {
                    if (mTabPager != null && mTabPager.getAdapter() != null && mTabPager.getAdapter().getCount() >= mTagItems.length) {
                        mNeedAnimate = true;
                        mTabPager.setCurrentItem(temp, false);
                    } else if (mTabPager != null && mTabPager.getAdapter() != null && mTabPager.getAdapter().getCount() <= mTagItems.length) {
                        mNeedAnimate = true;
                        mTabPager.setCurrentItem(temp, false);
                        setSelectTab(temp);
                    } else {
                        setSelectTab(temp, true);
                    }
                }
                break;

        }
        return !mDraggedBadge;
    }
}

