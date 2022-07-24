
package com.basic.code.fragment.expands.alibaba.tangram.support;

import android.view.View;

import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;
import com.basic.aop.annotation.SingleClick;
import com.basic.code.utils.XToastUtils;

/**
 * 自定义点击事件
 *

 * @since 2020/4/7 1:18 AM
 */
public class CustomClickSupport extends SimpleClickSupport {

    public CustomClickSupport() {
        setOptimizedMode(true);
    }

    @SingleClick(3000)
    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        XToastUtils.toast("您点击了组件，type=" + cell.stringType + ", pos=" + cell.pos);
    }
}
