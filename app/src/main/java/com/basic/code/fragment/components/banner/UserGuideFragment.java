package com.basic.code.fragment.components.banner;

import androidx.viewpager.widget.ViewPager;

import com.basic.page.annotation.Page;
import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.banner.anim.select.ZoomInEnter;
import com.basic.face.widget.banner.widget.banner.SimpleGuideBanner;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import static com.basic.code.fragment.components.banner.UserGuideFragment.POSITION;


/**
 * 可使用Applink打开:https://zhiqiang.club/page/transfer?pageName=UserGuide&position=2
 */
@Page(name = "UserGuide", params = {POSITION})
public class UserGuideFragment extends BaseFragment {

    public final static String POSITION = "position";

    private Class<? extends ViewPager.PageTransformer> transformerClass;

    @AutoWired
    int position;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_guide;
    }

    @Override
    protected void initArgs() {
        Router.getInstance().inject(this);

        if (position >= 0 && position <= DemoDataProvider.transformers.length - 1) {
            transformerClass = DemoDataProvider.transformers[position];
        } else {
            transformerClass = DemoDataProvider.transformers[0];
        }
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        sgb();
    }


    @Override
    protected void initListeners() {

    }


    private void sgb() {
        SimpleGuideBanner sgb = findViewById(R.id.sgb);

        sgb
                .setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(transformerClass)
                .barPadding(0, 10, 0, 10)
                .setSource(DemoDataProvider.getUserGuides())
                .startScroll();

        sgb.setOnJumpClickListener(this::popToBack);
    }
}
