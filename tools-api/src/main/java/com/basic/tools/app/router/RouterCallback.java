
package com.basic.tools.app.router;

import android.content.Context;

/**
 * <pre>
 *     desc   : 路由回调接口

 *     time   : 2018/4/30 下午12:10
 * </pre>
 */
public interface RouterCallback {

    void onBefore(Context from, Class<?> to);

    void onNext(Context from, Class<?> to);

    void onError(Context from, Class<?> to, Throwable throwable);

}
