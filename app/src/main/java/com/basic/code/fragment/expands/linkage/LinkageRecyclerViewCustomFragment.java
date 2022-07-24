
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
import com.basic.code.fragment.expands.linkage.custom.CustomGroupedItem;
import com.basic.code.fragment.expands.linkage.custom.CustomLinkagePrimaryAdapterConfig;
import com.basic.code.fragment.expands.linkage.custom.CustomLinkageSecondaryAdapterConfig;

import butterknife.BindView;

/**

 * @since 2019-11-25 16:52
 */
@Page(name = "双列表自定义样式")
public class LinkageRecyclerViewCustomFragment extends BaseFragment implements CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener, CustomLinkageSecondaryAdapterConfig.OnSecondaryItemClickListener {

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
        linkage.init(DemoDataProvider.getCustomGroupItems(), new CustomLinkagePrimaryAdapterConfig(this), new CustomLinkageSecondaryAdapterConfig(this));

    }

    @Override
    public void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        SnackbarUtils.Short(view, title).show();
    }

    @Override
    public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<CustomGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, item.info.getTitle()).show();
    }
}
