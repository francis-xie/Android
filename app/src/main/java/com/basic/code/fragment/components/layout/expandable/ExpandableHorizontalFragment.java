
package com.basic.code.fragment.components.layout.expandable;

import androidx.appcompat.widget.AppCompatImageView;

import com.basic.page.annotation.Page;
import com.basic.face.widget.layout.ExpandableLayout;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "水平伸缩使用")
public class ExpandableHorizontalFragment extends BaseFragment {
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandableLayout;
    @BindView(R.id.expand_button)
    AppCompatImageView expandButton;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expandable_horizontal;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        expandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (expandButton != null) {
                expandButton.setRotation(expansion * 180);
            }
        });
    }

    @OnClick(R.id.expand_button)
    public void onViewClicked() {
        expandableLayout.toggle();
    }
}
