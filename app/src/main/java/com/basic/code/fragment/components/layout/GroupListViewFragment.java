
package com.basic.code.fragment.components.layout;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.widget.grouplist.FACECommonListItemView;
import com.basic.face.widget.grouplist.FACEGroupListView;
import com.basic.face.widget.progress.loading.MiniLoadingView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;

/**
 * {@link FACEGroupListView} 的使用示例
 */
@Page(name = "FACEGroupListView\n通用的GroupListView，注意它不是ListView")
public class GroupListViewFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    FACEGroupListView mGroupListView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grouplistview;
    }

    @Override
    protected void initViews() {
        initGroupListView();
    }

    private void initGroupListView() {
        FACECommonListItemView normalItem = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher),
                "Item 1",
                null,
                FACECommonListItemView.HORIZONTAL,
                FACECommonListItemView.ACCESSORY_TYPE_NONE);
        normalItem.setOrientation(FACECommonListItemView.VERTICAL);

        FACECommonListItemView itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_avatar1),
                "Item 2",
                null,
                FACECommonListItemView.HORIZONTAL,
                FACECommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetail.setDetailText("在右方的详细信息");

        FACECommonListItemView itemWithDetailBelow = mGroupListView.createItemView("Item 3");
        itemWithDetailBelow.setOrientation(FACECommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText("在标题下方的详细信息");

        FACECommonListItemView itemWithChevron = mGroupListView.createItemView("Item 4");
        itemWithChevron.setAccessoryType(FACECommonListItemView.ACCESSORY_TYPE_CHEVRON);

        FACECommonListItemView itemWithSwitch = mGroupListView.createItemView("Item 5");
        itemWithSwitch.setAccessoryType(FACECommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener((buttonView, isChecked) -> XToastUtils.toast("checked = " + isChecked));

        FACECommonListItemView itemWithCustom = mGroupListView.createItemView("Item 6");
        itemWithCustom.setAccessoryType(FACECommonListItemView.ACCESSORY_TYPE_CUSTOM);
        MiniLoadingView loadingView = new MiniLoadingView(getActivity());
        itemWithCustom.addAccessoryCustomView(loadingView);

        View.OnClickListener onClickListener = v -> {
            if (v instanceof FACECommonListItemView) {
                CharSequence text = ((FACECommonListItemView) v).getText();
                XToastUtils.toast(text + " is Clicked");
            }
        };

        int size = DensityUtils.dp2px(getContext(), 20);
        FACEGroupListView.newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .setDescription("Section 1 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(normalItem, onClickListener)
                .addItemView(itemWithDetail, onClickListener)
                .addItemView(itemWithDetailBelow, onClickListener)
                .addItemView(itemWithChevron, onClickListener)
                .addItemView(itemWithSwitch, onClickListener)
                .addTo(mGroupListView);

        FACEGroupListView.newSection(getContext())
                .setTitle("Section 2: 自定义右侧 View")
                .addItemView(itemWithCustom, onClickListener)
                .addTo(mGroupListView);
    }

}
