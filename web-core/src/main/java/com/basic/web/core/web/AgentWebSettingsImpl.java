/*
 * Copyright (C)  Justson(https://github.com/Justson/AgentWeb)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.basic.web.core.web;

import android.app.Activity;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.basic.web.action.PermissionInterceptor;
import com.basic.web.core.client.WebListenerManager;
import com.basic.web.core.AgentWeb;
import com.basic.web.utils.LogUtils;


/**
 * @since 1.0.0
 * @author cenxiaozhong
 */
public class AgentWebSettingsImpl extends AbsAgentWebSettings {
    private AgentWeb mAgentWeb;

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        Class<?> clazz = null;
        Object mDefaultDownloadImpl$Extra = null;
        try {
            clazz = Class.forName("com.basic.web.download.DefaultDownloadImpl");
            mDefaultDownloadImpl$Extra =
                    clazz.getDeclaredMethod("create", Activity.class, WebView.class,
                            Class.forName("com.basic.web.download.DownloadListener"),
                            Class.forName("com.basic.web.download.DownloadingListener"),
                            PermissionInterceptor.class)
                            .invoke(mDefaultDownloadImpl$Extra, (Activity) webView.getContext()
                                    , webView, null, null, mAgentWeb.getPermissionInterceptor());

        } catch (Throwable ignore) {
            if (LogUtils.isDebug()) {
                ignore.printStackTrace();
            }
        }
        return super.setDownloader(webView, mDefaultDownloadImpl$Extra == null ? downloadListener : (DownloadListener) mDefaultDownloadImpl$Extra);
    }
}
