
package com.basic.code.base.webview;

import android.view.KeyEvent;

import com.basic.web.core.AgentWeb;
import com.basic.code.base.BaseFragment;

/**
 * 基础web
 *

 * @since 2019/5/28 10:22
 */
public abstract class BaseWebViewFragment extends BaseFragment {

    protected AgentWeb mAgentWeb;

    @Override
    protected void initViews() {
        mAgentWeb = createAgentWeb();
    }

    /**
     * 创建AgentWeb
     *
     * @return AgentWeb
     */
    protected abstract AgentWeb createAgentWeb();

    //===================生命周期管理===========================//
    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();//恢复
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        if (mAgentWeb != null) {
            mAgentWeb.destroy();
        }
        super.onDestroyView();
    }
}
