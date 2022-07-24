
package com.basic.code.fragment.expands;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.expands.linkage.LinkageRecyclerViewCustomFragment;
import com.basic.code.fragment.expands.linkage.LinkageRecyclerViewElemeFragment;
import com.basic.code.fragment.expands.linkage.LinkageRecyclerViewSimpleFragment;
import com.basic.code.utils.Utils;

/**

 * @since 2019-11-25 11:24
 */
@Page(name = "双列表联动", extra = R.drawable.ic_expand_linkage_list)
public class LinkageRecyclerViewFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                LinkageRecyclerViewSimpleFragment.class,
                LinkageRecyclerViewCustomFragment.class,
                LinkageRecyclerViewElemeFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/KunMinX/Linkage-RecyclerView");
            }
        });
        return titleBar;
    }
}
