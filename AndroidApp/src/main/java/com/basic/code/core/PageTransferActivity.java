package com.basic.code.core;

import android.os.Bundle;

import com.basic.code.utils.ToastUtils;
import com.basic.router.annotation.AutoWired;
import com.basic.router.annotation.Router;
import com.basic.tools.common.StringUtils;

/**
 * https://zhiqiang.club/page/transfer?pageName=xxxxx&....
 * applink的中转
 */
@Router(path = "/page/transfer")
public class PageTransferActivity extends BaseActivity {

    @AutoWired(name = "pageName")
    String pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.basic.router.launcher.Router.getInstance().inject(this);

        if (!StringUtils.isEmpty(pageName)) {
            if (openPage(pageName, getIntent().getExtras()) == null) {
                ToastUtils.error("页面未找到！");
                finish();
            }
        } else {
            ToastUtils.error("页面未找到！");
            finish();
        }
    }
}
