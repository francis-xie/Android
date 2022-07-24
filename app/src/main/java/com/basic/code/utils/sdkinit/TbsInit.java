
package com.basic.code.utils.sdkinit;

import android.app.Application;

/**
 * 腾讯X5 SDK初始化
 *

 * @since 2019-07-21 22:56
 */
public final class TbsInit {

    private TbsInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Application application) {
        PreLoadX5Service.start(application);
    }
}
