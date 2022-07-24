package com.basic.page;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.basic.page.annotation.Page;
import com.basic.page.base.PageActivity;
import com.basic.page.core.CoreConfig;
import com.basic.page.logger.PageLog;
import com.basic.page.model.PageInfo;
import com.basic.page.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面配置
 *

 * @since 2018/5/24 下午3:40
 */
public class PageConfig {

    /**
     * 页面配置接口
     */
    private PageConfiguration mPageConfiguration;

    /**
     * 页面信息
     */
    private List<PageInfo> mPages = new ArrayList<>();

    /**
     * PageFragment的容器Activity类名
     */
    private String mContainActivityClassName = PageActivity.class.getCanonicalName();

    private static PageConfig gInstance;

    public static PageConfig getInstance() {
        if (gInstance == null) {
            synchronized (PageConfig.class) {
                if (gInstance == null) {
                    gInstance = new PageConfig();
                }
            }
        }
        return gInstance;
    }

    public PageConfig setPageConfiguration(PageConfiguration pageConfiguration) {
        mPageConfiguration = pageConfiguration;
        return this;
    }

    /**
     * 初始化页面配置
     *
     * @param application 应用上下文
     */
    public void init(Application application) {
        initPages(application);
    }

    /**
     * 设置是否打开调试
     *
     * @param isDebug 是否打开调试
     */
    public PageConfig debug(boolean isDebug) {
        PageLog.debug(isDebug);
        return this;
    }

    /**
     * 设置调试模式
     *
     * @param tag 标记
     */
    public PageConfig debug(String tag) {
        PageLog.debug(tag);
        return this;
    }

    /**
     * 初始化页面信息
     *
     * @param context 上下文
     */
    private void initPages(Context context) {
        if (mPageConfiguration == null) {
            // 没有设置的话，就使用自动注册配置
            mPageConfiguration = new AutoPageConfiguration();
        }
        registerPageInfos(mPageConfiguration.registerPages(context));
        CoreConfig.init(context, getPages());
    }

    /**
     * 设置PageFragment的容器Activity类名
     *
     * @param containActivityClazz 容器Activity类
     */
    public PageConfig setContainActivityClazz(@NonNull Class<? extends PageActivity> containActivityClazz) {
        mContainActivityClassName = containActivityClazz.getCanonicalName();
        return this;
    }

    public static String getContainActivityClassName() {
        return getInstance().mContainActivityClassName;
    }

    /**
     * 注册页面信息
     *
     * @param clazz
     * @return
     */
    public PageConfig registerPageInfo(Class<?> clazz) {
        mPages.add(getPageInfo(clazz));
        return this;
    }

    /**
     * 注册多个页面信息
     *
     * @param clazz
     * @return
     */
    public PageConfig registerPageInfos(Class... clazz) {
        for (int i = 0; i < clazz.length; i++) {
            registerPageInfo(clazz[i]);
        }
        return this;
    }

    /**
     * 注册多个页面信息
     *
     * @param pageInfos
     * @return
     */
    private PageConfig registerPageInfos(List<PageInfo> pageInfos) {
        if (pageInfos != null && pageInfos.size() > 0) {
            mPages.clear();
            mPages.addAll(pageInfos);
        }
        return this;
    }

    public List<PageInfo> getPages() {
        return mPages;
    }

    public static PageInfo getPageInfo(Class<?> clazz) {
        Page page = getPage(clazz);
        PageInfo pageInfo = new PageInfo(TextUtils.isEmpty(page.name()) ? clazz.getSimpleName() : page.name(), clazz);
        if (!TextUtils.isEmpty(page.params()[0])) {
            pageInfo.setParams(page.params());
        }
        pageInfo.setAnim(page.anim());
        return pageInfo;
    }

    /**
     * 获取页面信息
     *
     * @param clazz
     * @return
     */
    public static Page getPage(Class<?> clazz) {
        return Utils.checkNotNull(clazz.getAnnotation(Page.class), "Page == null，请检测页面是否漏加 @Page 进行修饰！");
    }
}
