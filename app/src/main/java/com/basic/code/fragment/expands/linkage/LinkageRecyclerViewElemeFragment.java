
package com.basic.code.fragment.expands.linkage;

import android.view.View;
import android.view.ViewGroup;

import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.SnackbarUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.fragment.expands.linkage.custom.CustomLinkagePrimaryAdapterConfig;
import com.basic.code.fragment.expands.linkage.eleme.ElemeGroupedItem;
import com.basic.code.fragment.expands.linkage.eleme.ElemeSecondaryAdapterConfig;

import butterknife.BindView;

/**

 * @since 2019-11-25 23:20
 */
@Page(name = "仿饿了么双列表联动菜单")
public class LinkageRecyclerViewElemeFragment extends BaseFragment implements CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener, ElemeSecondaryAdapterConfig.OnSecondaryItemClickListener {

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
        linkage.init(DemoDataProvider.getElemeGroupItems(), new CustomLinkagePrimaryAdapterConfig(this), new ElemeSecondaryAdapterConfig(this));
    }

    @Override
    public void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        SnackbarUtils.Short(view, title).show();
    }

    @Override
    public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, item.info.getTitle()).show();
    }

    @Override
    public void onGoodAdd(View view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, "添加：" + item.info.getTitle()).show();
    }
}
