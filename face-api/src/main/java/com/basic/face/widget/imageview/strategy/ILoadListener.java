
package com.basic.face.widget.imageview.strategy;

/**
 * 加载监听
 *

 * @since 2019-11-09 10:39
 */
public interface ILoadListener {

    /**
     * 加载成功
     */
    void onLoadSuccess();

    /**
     * 加载失败
     *
     * @param error
     */
    void onLoadFailed(Throwable error);

}
