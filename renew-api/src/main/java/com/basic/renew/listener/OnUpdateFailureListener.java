
package com.basic.renew.listener;


import com.basic.renew.entity.UpdateError;

/**
 * 更新失败监听
 *

 * @since 2018/7/1 下午7:43
 */
public interface OnUpdateFailureListener {
    /**
     * 更新失败
     *
     * @param error 错误
     */
    void onFailure(UpdateError error);
}