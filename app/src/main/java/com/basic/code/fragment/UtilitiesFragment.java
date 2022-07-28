
package com.basic.code.fragment;

import com.basic.page.annotation.Page;
import com.basic.page.config.AppPageConfig;
import com.basic.page.enums.CoreAnim;
import com.basic.page.model.PageInfo;
import com.basic.code.base.BaseHomeFragment;

import java.util.List;

@Page(name = "工具", anim = CoreAnim.none)
public class UtilitiesFragment extends BaseHomeFragment {

    @Override
    protected List<PageInfo> getPageContents() {
        return AppPageConfig.getInstance().getUtils();
    }
}
