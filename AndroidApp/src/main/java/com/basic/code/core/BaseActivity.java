package com.basic.code.core;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.basic.page.base.PageActivity;
import com.basic.page.base.PageFragment;
import com.basic.page.core.CoreSwitchBean;
import com.basic.router.facade.service.SerializationService;
import com.basic.router.launcher.Router;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.slideback.SlideBack;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * 基础容器Activity
 */
public class BaseActivity<Binding extends ViewBinding> extends PageActivity {
    /**
     * 是否支持侧滑返回
     */
    public static final String KEY_SUPPORT_SLIDE_BACK = "key_support_slide_back";
    /**
     * ViewBinding
     */
    protected Binding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        //注入字体
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected View getCustomRootView() {
        binding = viewBindingInflate(getLayoutInflater());
        return binding != null ? binding.getRoot() : null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initStatusBarStyle();
        super.onCreate(savedInstanceState);
        registerSlideBack();
    }

    /**
     * 构建ViewBinding
     *
     * @param inflater  inflater
     * @return ViewBinding
     */
    @Nullable
    protected Binding viewBindingInflate(LayoutInflater inflater) {
        return null;
    }

    /**
     * 获取Binding
     *
     * @return Binding
     */
    public Binding getBinding() {
        return binding;
    }

    /**
     * 初始化状态栏的样式
     */
    protected void initStatusBarStyle() {

    }

    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    public <T extends PageFragment> T openPage(Class<T> clazz, boolean addToBackStack) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setAddToBackStack(addToBackStack);
        return (T) openPage(page);
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    public <T extends PageFragment> T openNewPage(Class<T> clazz) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        return (T) openPage(page);
    }

    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    public <T extends PageFragment> T switchPage(Class<T> clazz) {
        return openPage(clazz, false);
    }

    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
    public String serializeObject(Object object) {
        return Router.getInstance().navigation(SerializationService.class).object2Json(object);
    }

    @Override
    protected void onRelease() {
        unregisterSlideBack();
        super.onRelease();
    }

    /**
     * 注册侧滑回调
     */
    protected void registerSlideBack() {
        if (isSupportSlideBack()) {
            SlideBack.with(this)
                    .haveScroll(true)
                    .edgeMode(ResUtils.isRtl() ? SlideBack.EDGE_RIGHT : SlideBack.EDGE_LEFT)
                    .callBack(this::popPage)
                    .register();
        }
    }

    /**
     * 注销侧滑回调
     */
    protected void unregisterSlideBack() {
        if (isSupportSlideBack()) {
            SlideBack.unregister(this);
        }
    }

    /**
     * @return 是否支持侧滑返回
     */
    protected boolean isSupportSlideBack() {
        CoreSwitchBean page = getIntent().getParcelableExtra(CoreSwitchBean.KEY_SWITCH_BEAN);
        return page == null || page.getBundle() == null || page.getBundle().getBoolean(KEY_SUPPORT_SLIDE_BACK, true);
    }

}
