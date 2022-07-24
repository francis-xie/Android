
package com.basic.tools.app.notify.builder;

import androidx.core.app.NotificationCompat;

/**
 * <pre>
 *     desc   : 多文本通知

 *     time   : 2018/4/28 上午12:25
 * </pre>
 */
public class BigTextBuilder extends BaseBuilder {

    @Override
    protected void beforeBuild() {
        NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
        textStyle.setBigContentTitle(mContentTitle).bigText(mContentText).setSummaryText(mSummaryText);
        setStyle(textStyle);
    }
}
