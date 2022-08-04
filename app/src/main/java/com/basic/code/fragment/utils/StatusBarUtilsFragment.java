package com.basic.code.fragment.utils;

import android.widget.CheckBox;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ColorUtils;
import com.basic.face.utils.StatusBarUtils;
import com.basic.face.widget.dialog.materialdialog.MaterialDialog;
import com.basic.face.widget.grouplist.FACECommonListItemView;
import com.basic.face.widget.grouplist.FACEGroupListView;
import com.basic.code.R;
import com.basic.code.activity.TranslucentActivity;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;
import com.basic.tools.app.ActivityUtils;

import butterknife.BindView;

@Page(name = "StatusBarUtils", extra = R.drawable.ic_util_status_bar)
public class StatusBarUtilsFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    FACEGroupListView groupListView;

    CheckBox checkBox;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_util_statusbar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        FACEGroupListView.newSection(getContext())
                .setDescription("支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android")
                .addItemView(groupListView.createItemView("沉浸式状态栏"), v -> ActivityUtils.startActivity(TranslucentActivity.class))
                .addTo(groupListView);

        FACEGroupListView.newSection(getContext())
                .setDescription("支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android")
                .addItemView(groupListView.createItemView("设置状态栏黑色字体与图标"), v -> StatusBarUtils.setStatusBarLightMode(getActivity()))
                .addItemView(groupListView.createItemView("设置状态栏白色字体与图标"), v -> StatusBarUtils.setStatusBarDarkMode(getActivity()))
                .addTo(groupListView);

        FACEGroupListView.newSection(getContext())
                .setDescription("不同机型下状态栏高度可能略有差异，并不是固定值，可以通过这个方法获取实际高度")
                .addItemView(groupListView.createItemView("获取状态栏的实际高度"), v -> ToastUtils.toast("状态栏的实际高度：" + StatusBarUtils.getStatusBarHeight(getContext())))
                .addTo(groupListView);


        FACEGroupListView.newSection(getContext())
                .addItemView(groupListView.createItemView("获取底部导航条的实际高度"), v -> ToastUtils.toast("导航条的高度：" + StatusBarUtils.getNavigationBarHeight(getContext())))
                .addItemView(groupListView.createItemView("设置底部导航条的颜色"), v -> StatusBarUtils.setNavigationBarColor(getActivity(), ColorUtils.getRandomColor()))
                .addTo(groupListView);

        FACECommonListItemView fullScreenSwitch = groupListView.createItemView("全屏切换");
        fullScreenSwitch.setAccessoryType(FACECommonListItemView.ACCESSORY_TYPE_SWITCH);
        checkBox = fullScreenSwitch.getSwitch();
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                StatusBarUtils.fullScreen(getActivity());
            } else {
                StatusBarUtils.cancelFullScreen(getActivity());
            }
        });

        FACEGroupListView.newSection(getContext())
                .addItemView(fullScreenSwitch, null)
                .addItemView(groupListView.createItemView("全屏下弹起Dialog"), v -> {
                    showSimpleTipDialog();
                })
                .addTo(groupListView);
    }


    /**
     * 简单的提示性对话框
     */
    private void showSimpleTipDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.icon_tip)
                .title(R.string.tip_infos)
                .content(R.string.content_simple_confirm_dialog)
                .positiveText(R.string.lab_submit)
                .build();
        StatusBarUtils.showDialog(getActivity(), dialog);

//        MiniLoadingDialog dialog = new MiniLoadingDialog(getContext());
//        dialog.showIfSync(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 3000);
    }


}
