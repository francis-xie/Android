
package com.basic.renew.listener.impl;

import com.basic.renew.entity.UpdateError;
import com.basic.renew.listener.OnUpdateFailureListener;
import com.basic.renew.logs.UpdateLog;

/**
 * 默认的更新出错的处理(简单地打印日志）
 */
public class DefaultUpdateFailureListener implements OnUpdateFailureListener {

    @Override
    public void onFailure(UpdateError error) {
        UpdateLog.e(error);
    }
}
