
package com.basic.code.fragment.components.banner;

import android.os.Bundle;
import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.banner.anim.select.RotateEnter;
import com.basic.face.widget.banner.anim.select.ZoomInEnter;
import com.basic.face.widget.banner.anim.unselect.NoAnimExist;
import com.basic.face.widget.banner.transform.ZoomOutSlideTransformer;
import com.basic.face.widget.banner.widget.banner.BannerItem;
import com.basic.face.widget.banner.widget.banner.SimpleImageBanner;
import com.basic.face.widget.banner.widget.banner.SimpleTextBanner;
import com.basic.face.widget.banner.widget.banner.base.BaseBanner;
import com.basic.face.widget.dialog.materialdialog.MaterialDialog;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;
import com.basic.code.widget.RadiusImageBanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.basic.code.fragment.components.banner.UserGuideFragment.POSITION;

/**
 * 使用ViewPager实现的图片轮播
 *

 * @date 2017/10/15 下午1:17
 */
@Page(name = "使用ViewPager实现的Banner")
public class ViewPagerBannerFragment extends BaseFragment implements BaseBanner.OnItemClickListener<BannerItem> {
    private List<BannerItem> mData;

    @BindView(R.id.sib_simple_usage)
    SimpleImageBanner sib_simple_usage;

    @BindView(R.id.rib_simple_usage)
    RadiusImageBanner rib_simple_usage;

    @BindView(R.id.sib_the_most_comlex_usage)
    SimpleImageBanner sib_the_most_comlex_usage;

    @BindView(R.id.sib_res)
    SimpleImageBanner sib_res;

    @BindView(R.id.sib_rectangle)
    SimpleImageBanner sib_rectangle;

    @BindView(R.id.sib_corner_rectangle)
    SimpleImageBanner sib_corner_rectangle;

    @BindView(R.id.sib_indicator_right_with_text)
    SimpleImageBanner sib_indicator_right_with_text;

    @BindView(R.id.sib_indicator_left_with_text)
    SimpleImageBanner sib_indicator_left_with_text;

    @BindView(R.id.sib_anim)
    SimpleImageBanner sib_anim;

    @BindView(R.id.sib_anim2)
    SimpleImageBanner sib_anim2;

    @BindView(R.id.stb)
    SimpleTextBanner stb;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_pager_banner;
    }

    @Override
    protected void initViews() {
        mData = DemoDataProvider.getBannerList();
        sib_simple_usage();
        sib_the_most_comlex_usage();
        sib_res();
        sib_rectangle();
        sib_corner_rectangle();
        sib_indicator_right_with_text();
        sib_indicator_left_with_text();
        sib_anim();
        sib_anim2();
        stb();
    }

    @Override
    protected void initListeners() {
    }

    @OnClick({R.id.tv_user_guide, R.id.tv_select_transformer, R.id.tv_select_transformer2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_guide:
                Bundle param = new Bundle();
                param.putInt(POSITION, 0);
                openPage(UserGuideFragment.class, param);
                break;
            case R.id.tv_select_transformer:
                showSelectDialog(false);
                break;
            case R.id.tv_select_transformer2:
                showSelectDialog(true);
                break;
            default:
                break;
        }
    }


    /**
     * 图片轮播的简单使用
     */
    private void sib_simple_usage() {
        sib_simple_usage.setSource(mData)
                .setOnItemClickListener((view, t, position) -> {
                })
                .setIsOnePageLoop(false).startScroll();

        rib_simple_usage.setSource(mData).startScroll();
    }

    /**
     * 图片轮播的复杂使用【参数配置】
     */
    private void sib_the_most_comlex_usage() {
        sib_the_most_comlex_usage
                /** methods in BaseIndicatorBanner */
//              .setIndicatorStyle(BaseIndicatorBanner.STYLE_CORNER_RECTANGLE)//set indicator style
//              .setIndicatorWidth(6)                               //set indicator width
//              .setIndicatorHeight(6)                              //set indicator height
//              .setIndicatorGap(8)                                 //set gap btween two indicators
//              .setIndicatorCornerRadius(3)                        //set indicator corner raduis
                .setSelectAnimClass(ZoomInEnter.class)              //set indicator select anim
                /** methods in BaseBanner */
//              .setBarColor(Color.parseColor("#88000000"))         //set bootom bar color
//              .barPadding(5, 2, 5, 2)                             //set bottom bar padding
//              .setBarShowWhenLast(true)                           //set bottom bar show or not when the position is the last
//              .setTextColor(Color.parseColor("#ffffff"))          //set title text color
//              .setTextSize(12.5f)                                 //set title text size
//              .setTitleShow(true)                                 //set title show or not
//              .setIndicatorShow(true)                             //set indicator show or not
//              .setDelay(2)                                        //setDelay before start scroll
//              .setPeriod(10)                                      //scroll setPeriod
                .setSource(mData)                  //data source list
                .setTransformerClass(ZoomOutSlideTransformer.class) //set page transformer
                .setOnItemClickListener(this)
                .startScroll();                                     //start scroll,the last method to call
    }

    /**
     * 索引点使用资源图片
     */
    private void sib_res() {
        sib_res
//                .setIndicatorStyle(SimpleImageBanner.STYLE_DRAWABLE_RESOURCE)
//                .setIndicatorSelectorRes(R.mipmap.banner_dot_unselect, R.mipmap.banner_dot_select)
                .setSource(mData)
                .setOnItemClickListener(this)
                .startScroll();
    }

    /**
     * 矩形的索引点
     */
    private void sib_rectangle() {
        sib_rectangle
//                .setIndicatorCornerRadius(0)
                .setSource(mData)
                .setOnItemClickListener(this)
                .startScroll();

    }

    /**
     * 扁长方形的索引点
     */
    private void sib_corner_rectangle() {
        sib_corner_rectangle
//                .setIndicatorWidth(10)
//                .setIndicatorHeight(4)
//                .setIndicatorCornerRadius(2)
                .setSource(mData)
                .setOnItemClickListener(this)
                .startScroll();

    }


    /**
     * 文字在左，索引点在右[xml中设置]
     */
    private void sib_indicator_right_with_text() {
        sib_indicator_right_with_text
                .setSource(mData)
                .setOnItemClickListener(this)
                .startScroll();
    }

    /**
     * 文字在右，索引点在左[xml中设置]
     */
    private void sib_indicator_left_with_text() {
        sib_indicator_left_with_text
                .setSource(mData)
                .setOnItemClickListener(this)
                .startScroll();
    }

    /**
     * 设置选中后切换的动画
     */
    private void sib_anim() {
        sib_anim
                .setSelectAnimClass(ZoomInEnter.class)
                .setSource(mData)
                .setOnItemClickListener(this)
                .startScroll();

    }

    private void sib_anim2() {
        sib_anim2
//                .setIndicatorWidth(10)
//                .setIndicatorHeight(4)
//                .setIndicatorCornerRadius(2)
                .setSelectAnimClass(RotateEnter.class)
                .setUnselectAnimClass(NoAnimExist.class)
                .setSource(mData)
                .setOnItemClickListener(this)
                .startScroll();
    }

    /**
     * 简单的文字轮播
     */
    private void stb() {

        List<String> titles = new ArrayList<>();
        Collections.addAll(titles, DemoDataProvider.titles);
        stb
                .setSource(titles)
                .setOnItemClickListener((view, item, position) -> XToastUtils.toast("position--->" + position))
                .startScroll();

    }

    private void showSelectDialog(final boolean isSimpleImageBanner) {

        List<String> itemList = new ArrayList<>();
        for (Class<?> c : DemoDataProvider.transformers) {
            itemList.add(c.getSimpleName());
        }
        final String[] contents = new String[itemList.size()];
        new MaterialDialog.Builder(getContext())
                .title(R.string.tip_please_select_transfer_type)
                .items(itemList.toArray(contents))
                .itemsCallback((dialog, itemView, position, text) -> {
                    Bundle param = new Bundle();
                    param.putInt(POSITION, position);
                    openPage(UserGuideFragment.class, param);
                })
                .show();

    }

    @Override
    public void onDestroyView() {
        sib_simple_usage.recycle();
        sib_the_most_comlex_usage.recycle();
        sib_res.recycle();
        sib_rectangle.recycle();
        sib_corner_rectangle.recycle();
        sib_indicator_right_with_text.recycle();
        sib_indicator_left_with_text.recycle();
        sib_anim.recycle();
        sib_anim2.recycle();
        stb.recycle();
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, BannerItem item, int position) {
        XToastUtils.toast("position--->" + position + ", item:" + item.title);
    }
}
