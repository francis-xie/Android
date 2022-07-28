package com.basic.code.fragment.components.tabbar;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.basic.tag.TagBar;
import com.basic.tag.OnTabSelectListener;
import com.basic.page.annotation.Page;
import com.basic.face.FACE;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**

 * @since 2019/4/14 下午8:07
 */
@Page(name = "TagBar\n一个可以显示中心按钮的TabBar")
public class TagBarFragment extends BaseFragment implements OnTabSelectListener {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabbar)
    TagBar mTabbar;

    private int[] mTitles = {R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4};
    private int[] mSelectIcons = {R.drawable.nav_01_pre, R.drawable.nav_02_pre, R.drawable.nav_04_pre, R.drawable.nav_05_pre};
    private int[] mNormalIcons = {R.drawable.nav_01_nor, R.drawable.nav_02_nor, R.drawable.nav_04_nor, R.drawable.nav_05_nor};

    private Map<String, View> mPageMap = new HashMap<>();

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View view = getPageView(getString(mTitles[position]));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };

    private View getPageView(String pageName) {
        View view = mPageMap.get(pageName);
        if (view == null) {
            TextView textView = new TextView(getContext());
            textView.setTextAppearance(getContext(), R.style.TextStyle_Content_Match);
            textView.setGravity(Gravity.CENTER);
            textView.setText(String.format("这个是%s页面的内容", pageName));
            view = textView;
            mPageMap.put(pageName, view);
        }
        return view;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_jptabbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mTabbar.setTitles(mTitles);
        mTabbar.setNormalIcons(mNormalIcons);
        mTabbar.setSelectedIcons(mSelectIcons);
        mTabbar.generate();
        //页面可以滑动
        mTabbar.setGradientEnable(true);
        mTabbar.setPageAnimateEnable(true);
        mTabbar.setTabTypeFace(FACE.getDefaultTypeface());

        mViewPager.setAdapter(mPagerAdapter);
        mTabbar.setContainer(mViewPager);

        if (mTabbar.getMiddleView() != null) {
            mTabbar.getMiddleView().setOnClickListener(v -> XToastUtils.toast("中间点击"));
        }

        mTabbar.showBadge(2, "", true);
    }

    @Override
    protected void initListeners() {
        mTabbar.setTabListener(this);
    }

    /**
     * 用户每次点击不同的Tab将会触发这个方法
     *
     * @param index 当前选择的TAB的索引值
     */
    @Override
    public void onTabSelect(int index) {
        if (index == 2) {
            mTabbar.hideBadge(2);
        }
        XToastUtils.toast("点击了" + getString(mTitles[index]));
    }

    /**
     * 这个方法主要用来拦截Tab选中的事件
     * 返回true,tab将不会被选中,onTabSelect也不会被回调
     * 默认返回false
     *
     * @param index 点击选中的tab下标
     * @return 布尔值
     */
    @Override
    public boolean onInterruptSelect(int index) {
        return false;
    }
}
