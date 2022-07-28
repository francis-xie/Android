
package com.basic.util.subsciber;

import com.basic.util.logs.RxLog;

import io.reactivex.functions.Consumer;


/**
 * 简单的出错处理（把错误打印出来）
 */
public final class SimpleThrowableAction implements Consumer<Throwable> {

    private String mTag;

    public SimpleThrowableAction(String tag) {
        mTag = tag;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        RxLog.eTag(mTag, "订阅发生错误！", throwable);
    }
}
