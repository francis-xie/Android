
package com.basic.code.fragment;

import com.basic.page.annotation.Page;
import com.basic.page.config.AppPageConfig;
import com.basic.page.enums.CoreAnim;
import com.basic.page.model.PageInfo;
import com.basic.code.base.BaseHomeFragment;

import java.util.List;

/**

 * @since 2018/12/29 上午11:16
 */
@Page(name = "拓展", anim = CoreAnim.none)
public class ExpandsFragment extends BaseHomeFragment {

    @Override
    protected List<PageInfo> getPageContents() {
        return AppPageConfig.getInstance().getExpands();
    }
}
