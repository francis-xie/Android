package com.basic.code.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.umeng.analytics.MobclickAgent;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.dialog.DialogLoader;
import com.basic.face.widget.guidview.GuideCaseQueue;
import com.basic.face.widget.guidview.GuideCaseView;
import com.basic.code.R;
import com.basic.code.adapter.menu.DrawerAdapter;
import com.basic.code.adapter.menu.DrawerItem;
import com.basic.code.adapter.menu.SimpleItem;
import com.basic.code.adapter.menu.SpaceItem;
import com.basic.code.base.BaseActivity;
import com.basic.code.fragment.ComponentsFragment;
import com.basic.code.fragment.ExpandsFragment;
import com.basic.code.fragment.UtilitiesFragment;
import com.basic.code.fragment.other.AboutFragment;
import com.basic.code.fragment.other.QRCodeFragment;
import com.basic.code.fragment.other.SettingFragment;
import com.basic.code.utils.SettingSPUtils;
import com.basic.code.utils.TokenUtils;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.code.widget.GuideTipsDialog;
import com.basic.tools.common.ClickUtils;
import com.basic.tools.system.DeviceUtils;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.Arrays;

import butterknife.BindView;

/**
 * 项目主页面
 *

 * @since 2018/11/13 下午5:20
 */
public class MainActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener, ClickUtils.OnClick2ExitListener {
    private static final int POS_COMPONENTS = 0;
    private static final int POS_UTILITIES = 1;
    private static final int POS_EXPANDS = 2;
    private static final int POS_ABOUT = 3;
    private static final int POS_LOGOUT = 5;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    private SlidingRootNav mSlidingRootNav;
    private LinearLayout mLLMenu;
    private String[] mMenuTitles;
    private Drawable[] mMenuIcons;
    private DrawerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //登记一下
        MobclickAgent.onProfileSignIn(DeviceUtils.getAndroidID());

        initData();

        initViews();

        initSlidingMenu(savedInstanceState);

        initOthers();
    }

    private void initData() {
        mMenuTitles = ResUtils.getStringArray(R.array.menu_titles);
        mMenuIcons = ResUtils.getDrawableArray(this, R.array.menu_icons);
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    private void initViews() {
        WidgetUtils.clearActivityBackground(this);
        initTab();
    }

    /**
     * 初始化Tab
     */
    private void initTab() {
        WidgetUtils.addTabWithoutRipple(mTabLayout, "组件", SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_component : R.drawable.selector_icon_tabbar_component);
        WidgetUtils.addTabWithoutRipple(mTabLayout, "工具", SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_util : R.drawable.selector_icon_tabbar_util);
        WidgetUtils.addTabWithoutRipple(mTabLayout, "拓展", SettingSPUtils.getInstance().isUseCustomTheme() ? R.drawable.custom_selector_icon_tabbar_expand : R.drawable.selector_icon_tabbar_expand);
        WidgetUtils.setTabLayoutTextFont(mTabLayout);

        switchPage(ComponentsFragment.class);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mAdapter.setSelected(tab.getPosition());
                switch (tab.getPosition()) {
                    case POS_COMPONENTS:
                        switchPage(ComponentsFragment.class);
                        break;
                    case 1:
                        switchPage(UtilitiesFragment.class);
                        break;
                    case 2:
                        switchPage(ExpandsFragment.class);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initOthers() {
        GuideTipsDialog.showTips(this);
        //静默检查版本更新
        Utils.checkUpdate(this, false);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        initSlidingMenu(null);
    }

    public void openMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.openMenu();
        }
    }

    public void closeMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.closeMenu();
        }
    }

    public boolean isMenuOpen() {
        if (mSlidingRootNav != null) {
            return mSlidingRootNav.isMenuOpened();
        }
        return false;
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withGravity(ResUtils.isRtl() ? SlideGravity.RIGHT : SlideGravity.LEFT)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        mLLMenu = mSlidingRootNav.getLayout().findViewById(R.id.ll_menu);
        final AppCompatImageView ivQrcode = mSlidingRootNav.getLayout().findViewById(R.id.iv_qrcode);
        ivQrcode.setOnClickListener(v -> openNewPage(QRCodeFragment.class));

        final AppCompatImageView ivSetting = mSlidingRootNav.getLayout().findViewById(R.id.iv_setting);
        ivSetting.setOnClickListener(v -> openNewPage(SettingFragment.class));
        ViewUtils.setVisibility(mLLMenu, false);

        mAdapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_COMPONENTS).setChecked(true),
                createItemFor(POS_UTILITIES),
                createItemFor(POS_EXPANDS),
                createItemFor(POS_ABOUT),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        mAdapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);

        mAdapter.setSelected(POS_COMPONENTS);
        mSlidingRootNav.setMenuLocked(false);
        mSlidingRootNav.getLayout().addDragStateListener(new DragStateListener() {
            @Override
            public void onDragStart() {
                ViewUtils.setVisibility(mLLMenu, true);
            }

            @Override
            public void onDragEnd(boolean isMenuOpened) {
                ViewUtils.setVisibility(mLLMenu, isMenuOpened);
                if (isMenuOpened) {
                    if (!GuideCaseView.isShowOnce(MainActivity.this, getString(R.string.guide_key_sliding_root_navigation))) {
                        final GuideCaseView guideStep1 = new GuideCaseView.Builder(MainActivity.this)
                                .title("点击进入，可切换主题样式哦～～")
                                .titleSize(18, TypedValue.COMPLEX_UNIT_SP)
                                .focusOn(ivSetting)
                                .build();

                        final GuideCaseView guideStep2 = new GuideCaseView.Builder(MainActivity.this)
                                .title("点击进入，扫码关注哦～～")
                                .titleSize(18, TypedValue.COMPLEX_UNIT_SP)
                                .focusOn(ivQrcode)
                                .build();

                        new GuideCaseQueue()
                                .add(guideStep1)
                                .add(guideStep2)
                                .show();
                        GuideCaseView.setShowOnce(MainActivity.this, getString(R.string.guide_key_sliding_root_navigation));
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case POS_COMPONENTS:
            case POS_UTILITIES:
            case POS_EXPANDS:
                if (mTabLayout != null) {
                    TabLayout.Tab tab = mTabLayout.getTabAt(position);
                    if (tab != null) {
                        tab.select();
                    }
                }
                mSlidingRootNav.closeMenu();
                break;
            case POS_ABOUT:
                openNewPage(AboutFragment.class);
                break;
            case POS_LOGOUT:
                DialogLoader.getInstance().showConfirmDialog(
                        this,
                        getString(R.string.lab_logout_confirm),
                        getString(R.string.lab_yes),
                        (dialog, which) -> {
                            dialog.dismiss();
                            TokenUtils.handleLogoutSuccess();
                            finish();
                        },
                        getString(R.string.lab_no),
                        (dialog, which) -> dialog.dismiss()
                );
                break;
            default:
                break;
        }
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(mMenuIcons[position], mMenuTitles[position])
                .withIconTint(ThemeUtils.resolveColor(this, R.attr.face_config_color_content_text))
                .withTextTint(ThemeUtils.resolveColor(this, R.attr.face_config_color_content_text))
                .withSelectedIconTint(ThemeUtils.getMainThemeColor(this))
                .withSelectedTextTint(ThemeUtils.getMainThemeColor(this));
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMenuOpen()) {
                closeMenu();
            } else {
                ClickUtils.exitBy2Click(2000, this);
            }
        }
        return true;
    }

    /**
     * 再点击一次
     */
    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    /**
     * 退出
     */
    @Override
    public void onExit() {
        moveTaskToBack(true);
    }
}
