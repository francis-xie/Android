package com.basic.code.fragment.components.popupwindow;

import android.view.View;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.popupwindow.popup.FACESimpleExpandablePopup;
import com.basic.face.widget.popupwindow.popup.FACESimplePopup;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;
import com.basic.tools.display.DensityUtils;

import butterknife.OnClick;

/**

 * @date 2017/11/12 下午4:34
 */
@Page(name = "弹出框统一样式")
public class PopupWindowStyleFragment extends BaseFragment {
    private FACESimplePopup mListPopup;
    private FACESimpleExpandablePopup mExpandableListPopup;
    private FACESimplePopup mMenuPopup;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setCenterClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View view) {
                mMenuPopup.showDown(view);
            }
        });
        titleBar.addAction(new TitleBar.TextAction("菜单") {
            @SingleClick
            @Override
            public void performAction(View view) {
                mMenuPopup.showDown(view);
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_popupwindow_style;
    }

    @Override
    protected void initViews() {
        initListPopup();
        initExpandableListPopup();
        initMenuPopup();
    }

    @Override
    protected void initListeners() {

    }

    private void initListPopup() {
        mListPopup = new FACESimplePopup(getContext(), DemoDataProvider.dpiItems)
                .create(DensityUtils.dip2px(getContext(), 170), (adapter, item, position) -> ToastUtils.toast(item.getTitle().toString()))
                .setHasDivider(true);
    }

    private void initExpandableListPopup() {
        mExpandableListPopup = new FACESimpleExpandablePopup(getContext(), DemoDataProvider.expandableItems)
                .create(DensityUtils.dip2px(getContext(), 200), DensityUtils.dip2px(getContext(), 200))
                .setOnExpandableItemClickListener(false, (adapter, group, groupPosition, childPosition) -> ToastUtils.toast(group.getChildItem(childPosition).getTitle()));
    }

    private void initMenuPopup() {
        mMenuPopup = new FACESimplePopup(getContext(), DemoDataProvider.menuItems)
                .create((adapter, item, position) -> ToastUtils.toast(item.getTitle().toString()));
    }

    @SingleClick
    @OnClick({R.id.btn_commonlist_popup, R.id.btn_expandable_popup, R.id.btn_menu_popup})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commonlist_popup:
                mListPopup.showDown(v);
                break;
            case R.id.btn_expandable_popup:
                mExpandableListPopup.clearExpandStatus();
                mExpandableListPopup.showDown(v);
                break;
            case R.id.btn_menu_popup:
                mMenuPopup.showDown(v);
                break;
            default:
                break;
        }
    }


}
