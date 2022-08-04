
package com.basic.web.core.event;

import android.view.KeyEvent;

/**
 * 事件处理
 *
 */
public interface IEventHandler {

    boolean onKeyDown(int keyCode, KeyEvent event);


    boolean back();
}
