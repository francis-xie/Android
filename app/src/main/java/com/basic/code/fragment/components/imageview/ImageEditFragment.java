
package com.basic.code.fragment.components.imageview;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.imageview.edit.ImageCropFragment;
import com.basic.code.fragment.components.imageview.edit.ImageEnhanceFragment;
import com.basic.code.fragment.components.imageview.edit.PhotoEditFragment;

/**

 * @since 2019-10-21 15:53
 */
@Page(name = "图片编辑")
public class ImageEditFragment extends ComponentContainerFragment {

    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                ImageCropFragment.class,
                ImageEnhanceFragment.class,
                PhotoEditFragment.class
        };
    }
}
