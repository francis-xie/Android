package com.basic.page;

import android.content.Context;


import com.basic.page.model.PageInfo;

import java.util.List;

/**
 * 页面配置接口
 */
public interface PageConfiguration {

    /**
     * 注册页面
     *
     * @param context 上下文
     * @return 注册的页面集合
     */
    List<PageInfo> registerPages(Context context);

}
