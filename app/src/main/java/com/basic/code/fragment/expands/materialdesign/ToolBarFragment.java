package com.basic.code.fragment.expands.materialdesign;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.popupwindow.popup.FACESimplePopup;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;
import com.basic.tools.display.DensityUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "ToolBar使用")
public class ToolBarFragment extends BaseFragment {
    @BindView(R.id.tool_bar1)
    Toolbar toolBar1;
    @BindView(R.id.tool_bar_2)
    Toolbar toolBar2;
    @BindView(R.id.tool_bar_3)
    Toolbar toolBar3;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tool_bar_4)
    Toolbar toolBar4;
    @BindView(R.id.ll_action)
    LinearLayout llAction;
    @BindView(R.id.tool_bar_5)
    Toolbar toolBar5;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;


    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_toolbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initToolbar1();
        initToolbar2();
        initToolbar3();
        initToolbar4();
        initToolbar5();
    }

    private void initToolbar1() {
        //设置NavigationIcon
        toolBar1.setNavigationIcon(R.drawable.ic_navigation_menu);
        // 设置 NavigationIcon 点击事件
        toolBar1.setNavigationOnClickListener(onClickListener);
        toolBar1.setContentInsetStartWithNavigation(0);
        // 设置 toolbar 背景色
        toolBar1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // 设置 Title
        toolBar1.setTitle(R.string.title_toolbar);
        //  设置Toolbar title文字颜色
        toolBar1.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置Toolbar subTitle
        toolBar1.setSubtitle(R.string.title_toolbar_sub);
        toolBar1.setSubtitleTextColor(getResources().getColor(R.color.white));
        // 设置logo
        toolBar1.setLogo(R.mipmap.ic_launcher);

        //设置 Toolbar menu
        toolBar1.inflateMenu(R.menu.menu_custom);
        // 设置溢出菜单的图标
        toolBar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_navigation_more));
        // 设置menu item 点击事件
        toolBar1.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar2() {
        toolBar2.setNavigationOnClickListener(onClickListener);
        toolBar2.inflateMenu(R.menu.menu_setting);
        toolBar2.setOnMenuItemClickListener(menuItemClickListener);

    }

    private void initToolbar3() {
        toolBar3.setNavigationOnClickListener(onClickListener);
        toolBar3.inflateMenu(R.menu.menu_setting);
        toolBar3.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar4() {
        toolBar4.setNavigationOnClickListener(onClickListener);
        toolBar4.inflateMenu(R.menu.menu_search);
        toolBar4.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void initToolbar5() {
        toolBar5.setNavigationOnClickListener(onClickListener);
        toolBar5.setOnMenuItemClickListener(menuItemClickListener);
        tvTitle.setText("主页");
    }


    private View.OnClickListener onClickListener = v -> ToastUtils.toast("点击了NavigationIcon");

    Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        ToastUtils.toast("点击了:" + item.getTitle());
        if (item.getItemId() == R.id.item_setting) {
            //点击设置
        }
        return false;
    };

    @OnClick(R.id.ll_action)
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ll_action) {
            showSelectPopWindow(view);
        }
    }

    private void showSelectPopWindow(View view) {
        new FACESimplePopup<>(getContext(), ResUtils.getStringArray(R.array.grid_titles_entry))
                .create(DensityUtils.dip2px(getContext(), 170), (adapter, item, position) -> {
                    ViewUtils.setText(tvSubTitle, String.format("<%s>", item));
                })
                .setHasDivider(true)
                .showDown(view);
    }
}
