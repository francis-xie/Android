
package com.basic.router.facade.template;

import android.content.Context;

/**
 * 对外提供接口的基类接口
 *

 * @since 2018/5/16 下午11:51
 */
public interface IProvider {

    /**
     * 进程初始化的方法
     *
     * @param context 上下文
     */
    void init(Context context);
}
