
package com.basic.code.fragment.expands.alibaba;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.basic.refresh.layout.RefreshLayouts;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.adapter.simple.AdapterItem;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.banner.widget.banner.SimpleImageBanner;
import com.basic.face.widget.imageview.ImageLoader;
import com.basic.face.widget.imageview.RadiusImageView;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.base.delegate.SimpleDelegateAdapter;
import com.basic.code.adapter.base.delegate.SingleDelegateAdapter;
import com.basic.code.adapter.entity.NewInfo;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.display.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * VLayout使用步骤
 * 1.设置VirtualLayoutManager
 * 2.设置RecycledViews复用池大小（可选）
 * 3.设置DelegateAdapter
 *
 */
@Page(name = "VLayout\n多布局组件")
public class VLayoutFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayouts refreshLayout;

    private SimpleDelegateAdapter<NewInfo> mNewsAdapter;

    private DelegateAdapter mDelegateAdapter;
    private List<DelegateAdapter.Adapter> mAdapters = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_v_layout;
    }

    @Override
    protected void initViews() {
        // 1.设置VirtualLayoutManager
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);

        // 2.设置RecycledViews复用池大小（可选）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        viewPool.setMaxRecycledViews(0, 20);
        recyclerView.setRecycledViewPool(viewPool);

        //轮播条，单独布局
        SingleDelegateAdapter bannerAdapter = new SingleDelegateAdapter(R.layout.include_head_view_banner) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                SimpleImageBanner banner = holder.findViewById(R.id.sib_simple_usage);
                banner.setSource(DemoDataProvider.getBannerList())
                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
            }
        };

        //九宫格菜单, 九宫格布局
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 16, 0, 0);
        gridLayoutHelper.setVGap(10);
        gridLayoutHelper.setHGap(0);
        SimpleDelegateAdapter<AdapterItem> commonAdapter = new SimpleDelegateAdapter<AdapterItem>(R.layout.adapter_common_grid_item, gridLayoutHelper, DemoDataProvider.getGridItems(getContext())) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
                if (item != null) {
                    RadiusImageView imageView = holder.findViewById(R.id.riv_item);
                    imageView.setCircle(true);
                    ImageLoader.get().loadImage(imageView, item.getIcon());
                    holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));
                    holder.text(R.id.tv_sub_title, item.getTitle());

                    holder.click(R.id.ll_container, v -> XToastUtils.toast("点击了：" + item.getTitle()));
                }
            }
        };

        //资讯的标题
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        stickyLayoutHelper.setStickyStart(true);
        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_vlayout_title_item, stickyLayoutHelper) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.tv_title, "资讯");
                holder.text(R.id.tv_action, "更多");
                holder.click(R.id.tv_action, v -> XToastUtils.toast("更多"));
            }
        };

        //资讯，线性布局
        mNewsAdapter = new SimpleDelegateAdapter<NewInfo>(R.layout.adapter_news_facelayout_list_item, new LinearLayoutHelper()) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, NewInfo model) {
                if (model != null) {
                    holder.text(R.id.tv_user_name, model.getUserName());
                    holder.text(R.id.tv_tag, model.getTag());
                    holder.text(R.id.tv_title, model.getTitle());
                    holder.text(R.id.tv_summary, model.getSummary());
                    holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
                    holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
                    holder.text(R.id.tv_read, "阅读量 " + model.getRead());
                    holder.image(R.id.iv_image, model.getImageUrl());

                    holder.click(R.id.card_view, v -> Utils.goWeb(getContext(), model.getDetailUrl()));
                }
            }
        };

        FloatLayoutHelper floatLayoutHelper = new FloatLayoutHelper();
        floatLayoutHelper.setDefaultLocation(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        SingleDelegateAdapter floatAdapter = new SingleDelegateAdapter(R.layout.adapter_vlayout_float_item, floatLayoutHelper) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.itemView.setOnClickListener(v -> XToastUtils.toast("点击了悬浮窗"));
            }
        };

        FixLayoutHelper fixLayoutHelper = new ScrollFixLayoutHelper(50, 100);
        SingleDelegateAdapter scrollFixAdapter = new SingleDelegateAdapter(R.layout.adapter_vlayout_float_item, fixLayoutHelper) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.image(R.id.iv_content, R.drawable.icon_avatar1);
            }
        };

        mDelegateAdapter = new DelegateAdapter(virtualLayoutManager);

        mAdapters.add(floatAdapter);
        mAdapters.add(scrollFixAdapter);
        mAdapters.add(bannerAdapter);
        mAdapters.add(commonAdapter);
        mAdapters.add(titleAdapter);
        mAdapters.add(mNewsAdapter);

        // 3.设置DelegateAdapter
        recyclerView.setAdapter(mDelegateAdapter);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mNewsAdapter.refresh(DemoDataProvider.getDemoNewInfos());
            mDelegateAdapter.setAdapters(mAdapters);
            refreshLayout.finishRefresh();
        }, 1000));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mNewsAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishLoadMore();
        }, 1000));
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/alibaba/vlayout");
            }
        });
        return titleBar;
    }
}
