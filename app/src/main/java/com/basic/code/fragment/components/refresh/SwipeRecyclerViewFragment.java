package com.basic.code.fragment.components.refresh;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.swipe.SwipeDragMoveFragment;
import com.basic.code.fragment.components.refresh.swipe.SwipeHeadFootViewFragment;
import com.basic.code.fragment.components.refresh.swipe.SwipeMenuItemFragment;
import com.basic.code.fragment.components.refresh.swipe.SwipeRefreshFragment;
import com.basic.code.utils.Utils;

/**
 * @author XUE
 * @since 2019/4/1 11:00
 */
@Page(name = "SwipeRecyclerView\n基于RecyclerView封装, 支持Item侧滑菜单、Item滑动删除、Item长按拖拽、添加HeaderView/FooterView、加载更多、Item点击监听等功能")
public class SwipeRecyclerViewFragment extends ComponentContainerFragment {
    /**
     * 获取页面的类集合[使用@Page注解进行注册的页面]
     *
     * @return
     */
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                SwipeMenuItemFragment.class,
                SwipeDragMoveFragment.class,
                SwipeHeadFootViewFragment.class,
                SwipeRefreshFragment.class
        };
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/yanzhenjie/SwipeRecyclerView");
            }
        });
        return titleBar;
    }
}
