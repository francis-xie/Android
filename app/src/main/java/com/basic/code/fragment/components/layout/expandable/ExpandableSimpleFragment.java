
package com.basic.code.fragment.components.layout.expandable;

import android.util.Log;

import com.basic.page.annotation.Page;
import com.basic.face.widget.layout.ExpandableLayout;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.basic.face.widget.layout.ExpandableLayout.State.COLLAPSED;
import static com.basic.face.widget.layout.ExpandableLayout.State.EXPANDED;

@Page(name = "可伸缩布局简单使用")
public class ExpandableSimpleFragment extends BaseFragment {

    @BindView(R.id.expandable_layout_1)
    ExpandableLayout expandableLayout1;
    @BindView(R.id.expandable_layout_2)
    ExpandableLayout expandableLayout2;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expandable_simple;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        expandableLayout1.setOnExpansionChangedListener((expansion, state) -> Log.d("expandableLayout1", "State: " + state));

        expandableLayout2.setOnExpansionChangedListener((expansion, state) -> {
            if (state == COLLAPSED) {
                ToastUtils.toast("已收起");
            } else if (state == EXPANDED) {
                ToastUtils.toast("已展开");
            }
        });
    }

    @OnClick(R.id.expand_button)
    public void onViewClicked() {
        if (expandableLayout1.isExpanded()) {
            expandableLayout1.collapse();
        } else if (expandableLayout2.isExpanded()) {
            expandableLayout2.collapse();
        } else {
            expandableLayout1.expand();
            expandableLayout2.expand();
        }
    }
}
