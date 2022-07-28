
package com.basic.code.fragment.expands.alibaba;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import com.tmall.ultraviewpager.UltraViewPager;
import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.UltraPagerAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.display.ScreenUtils;

import butterknife.BindView;

@Page(name = "UltraViewPager\n阿里巴巴轮播控件")
public class UltraViewPagerFragment extends BaseFragment {

    @BindView(R.id.ultra_view_pager)
    UltraViewPager ultraViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ultraviewpager;
    }

    @Override
    protected void initViews() {
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        float scale = 0.5625F;
        UltraPagerAdapter adapter = new UltraPagerAdapter(DemoDataProvider.getBannerList(), scale);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMaxHeight((int) (ScreenUtils.getScreenWidth() * scale));
        //指示器
        ultraViewPager.initIndicator();
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(ThemeUtils.getMainThemeColor(getContext()))
                .setNormalColor(Color.WHITE)
                .setMargin(DensityUtils.dp2px(10), DensityUtils.dp2px(10), DensityUtils.dp2px(10), DensityUtils.dp2px(10))
                .setRadius(DensityUtils.dp2px(5));
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraViewPager.getIndicator().build();

        ultraViewPager.setInfiniteLoop(true);
        ultraViewPager.setAutoScroll(2000);

        adapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("position--->" + position));
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/alibaba/UltraViewPager");
            }
        });
        return titleBar;
    }
}
