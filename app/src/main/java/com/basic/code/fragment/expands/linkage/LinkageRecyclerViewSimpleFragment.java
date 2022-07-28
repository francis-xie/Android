
package com.basic.code.fragment.expands.linkage;

import android.view.View;

import com.kunminx.linkage.LinkageRecyclerView;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.SnackbarUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

@Page(name = "双列表简单使用")
public class LinkageRecyclerViewSimpleFragment extends BaseFragment {
    @BindView(R.id.linkage)
    LinkageRecyclerView linkage;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_linkage_recyclerview;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("切换") {
            @SingleClick
            @Override
            public void performAction(View view) {
                if (linkage != null) {
                    linkage.setGridMode(!linkage.isGridMode());
                }
            }
        });
        return titleBar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        linkage.init(DemoDataProvider.getGroupItems());
        linkage.setScrollSmoothly(true);
        linkage.setDefaultOnItemBindListener(
                (primaryHolder, primaryClickView, title) -> {
                    //一级列表点击
                    SnackbarUtils.Short(primaryClickView, title).show();
                },
                (primaryHolder, title) -> {
                    //一级列表样式设置
                    //TODO

                    },
                (secondaryHolder, item) -> {
                    //二级列表点击
                    secondaryHolder.getView(R.id.level_2_item).setOnClickListener(v -> SnackbarUtils.Short(v, item.info.getTitle()).show());
                },
                (headerHolder, item) -> {
                    //TODO
                },
                (footerHolder, item) -> {
                    //TODO
                }
        );
    }



}
