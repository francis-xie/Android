
package com.basic.code.fragment.components.image;

import androidx.appcompat.widget.AppCompatImageView;

import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.image.ImageLoader;
import com.basic.face.widget.image.strategy.DiskCacheStrategyEnum;
import com.basic.face.widget.image.strategy.ILoadListener;
import com.basic.face.widget.image.strategy.LoadOption;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

@Page(name = "图片加载策略")
public class ImageLoadStrategyFragment extends BaseFragment {

    private static final String PICTURE_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573298332486&di=57555a4ffbd9c2c09f12042f0f2b8ba6&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1212%2F10%2Fc1%2F16491245_1355126013759.jpg";
    private static final String PICTURE_URL1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573298947875&di=a2d6f04cd74ad2a7db74eb5d2395f2c6&imgtype=0&src=http%3A%2F%2Fupload.17u.net%2Fuploadpicbase%2Fimage%2F201306150338029631.jpg";
    private static final String PICTURE_URL2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573298976039&di=ba80603c68dc64a4efc55e19065eacf1&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fc%2F59bcd50cb8281.jpg";

    @BindView(R.id.iv_content)
    AppCompatImageView ivContent;
    @BindView(R.id.iv_content1)
    AppCompatImageView ivContent1;
    @BindView(R.id.iv_content2)
    AppCompatImageView ivContent2;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_imageload_strategy;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        getMessageLoader("加载中...");
        ImageLoader.get().loadImage(ivContent, PICTURE_URL, DiskCacheStrategyEnum.NONE, new ILoadListener() {
            @Override
            public void onLoadSuccess() {
                getMessageLoader().dismiss();
            }
            @Override
            public void onLoadFailed(Throwable error) {
                getMessageLoader().dismiss();
            }
        });
        LoadOption option = LoadOption.of(DiskCacheStrategyEnum.ALL)
                .setPlaceholder(ResUtils.getDrawable(R.drawable.face_ic_default_img))
                .setSize(DensityUtils.dp2px(200), DensityUtils.dp2px(100));
        ImageLoader.get().loadImage(ivContent1, PICTURE_URL1, option);

        ImageLoader.get().loadImage(ivContent2, PICTURE_URL2, ResUtils.getDrawable(R.drawable.face_ic_default_img), DiskCacheStrategyEnum.AUTOMATIC);

    }
}
