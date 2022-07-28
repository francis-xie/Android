
package com.basic.web.core.event;

import android.view.KeyEvent;

/**
 * 事件处理
 *

 * @since 2019/1/4 上午11:23
 */
public interface IEventHandler {

    boolean onKeyDown(int keyCode, KeyEvent event);


    boolean back();
}
