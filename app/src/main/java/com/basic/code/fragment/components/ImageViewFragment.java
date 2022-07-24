package com.basic.code.fragment.components;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.imageview.ImageEditFragment;
import com.basic.code.fragment.components.imageview.ImageLoadStrategyFragment;
import com.basic.code.fragment.components.imageview.PreviewFragment;
import com.basic.code.fragment.components.imageview.RadiusImageViewFragment;
import com.basic.code.fragment.components.imageview.SignatureViewFragment;
import com.basic.code.fragment.components.imageview.pictureselector.PictureSelectorFragment;

/**

 * @since 2018/12/2 上午12:21
 */
@Page(name = "图片", extra = R.drawable.ic_widget_imageview)
public class ImageViewFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                RadiusImageViewFragment.class,
                PictureSelectorFragment.class,
                PreviewFragment.class,
                ImageEditFragment.class,
                ImageLoadStrategyFragment.class,
                SignatureViewFragment.class
        };
    }
}
