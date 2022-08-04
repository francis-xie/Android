
package com.basic.web.core.video;

import android.view.View;
import android.webkit.WebChromeClient;


/**
 * 视频接口
 */
public interface IVideo {


    void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);


    void onHideCustomView();


    boolean isVideoState();

}
