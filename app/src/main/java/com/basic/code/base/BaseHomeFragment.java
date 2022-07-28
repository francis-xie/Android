
package com.basic.code.base;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.model.PageInfo;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.activity.MainActivity;
import com.basic.code.adapter.WidgetItemAdapter;
import com.basic.code.fragment.other.AboutFragment;
import com.basic.code.fragment.other.SearchComponentFragment;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 基础主页面
 */
public abstract class BaseHomeFragment extends BaseFragment implements RecyclerViewHolder.OnItemClickListener<PageInfo> {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setLeftImageResource(R.drawable.ic_action_menu);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            @SingleClick
            public void onClick(View v) {
                getContainer().openMenu();
            }
        });
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_query) {
            @Override
            @SingleClick
            public void performAction(View view) {
                openNewPage(SearchComponentFragment.class);
            }

            @Override
            public int rightPadding() {
                return DensityUtils.dp2px(10);
            }
        });
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_about) {
            @Override
            @SingleClick
            public void performAction(View view) {
                openNewPage(AboutFragment.class);
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_container;
    }

    @Override
    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        WidgetUtils.initGridRecyclerView(mRecyclerView, 3, DensityUtils.dp2px(2), ThemeUtils.resolveColor(getContext(), R.attr.face_config_color_separator_light, ResUtils.getColor(R.color.face_config_color_separator_light_phone)));

        WidgetItemAdapter mWidgetItemAdapter = new WidgetItemAdapter(sortPageInfo(getPageContents()));
        mWidgetItemAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mWidgetItemAdapter);
    }

    /**
     * @return 页面内容
     */
    protected abstract List<PageInfo> getPageContents();

    /**
     * 进行排序
     *
     * @param pageInfoList
     * @return
     */
    private List<PageInfo> sortPageInfo(List<PageInfo> pageInfoList) {
        Collections.sort(pageInfoList, (o1, o2) -> o1.getClassPath().compareTo(o2.getClassPath()));
        return pageInfoList;
    }

    @Override
    @SingleClick
    public void onItemClick(View itemView, PageInfo widgetInfo, int pos) {
        if (widgetInfo != null) {
            openNewPage(widgetInfo.getName());
        }
    }

    public MainActivity getContainer() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig);
        ViewGroup root = (ViewGroup) getRootView();
        if (root.getChildAt(0) instanceof TitleBar) {
            root.removeViewAt(0);
            initTitle();
        }
    }

}
