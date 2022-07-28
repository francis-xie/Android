
package com.basic.renew.proxy;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basic.renew.entity.UpdateEntity;
import com.basic.renew.listener.IUpdateParseCallback;
import com.basic.renew.service.OnFileDownloadListener;

/**
 * 版本更新代理
 */
public interface IUpdateProxy {

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    @Nullable
    Context getContext();

    /**
     * 获取版本更新的地址
     *
     * @return 版本更新的地址
     */
    String getUrl();

    /**
     * 获取版本更新网络请求服务API
     *
     * @return 网络请求服务API
     */
    IUpdateHttpService getIUpdateHttpService();

    /**
     * 开始版本更新
     */
    void update();

    //============ICheckerProxy=================//

    /**
     * 版本检查之前
     */
    void onBeforeCheck();

    /**
     * 执行网络请求，检查应用的版本信息
     */
    void checkVersion();

    /**
     * 版本检查之后
     */
    void onAfterCheck();

    /**
     * 发现新版本
     *
     * @param updateEntity 版本更新信息
     * @param updateProxy  版本更新代理
     */
    void findNewVersion(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy);

    /**
     * 未发现新版本
     *
     * @param throwable 未发现的原因
     */
    void noNewVersion(Throwable throwable);

    //=============IParserProxy================//

    /**
     * 是否是异步解析者
     *
     * @return 是否是异步解析者
     */
    boolean isAsyncParser();

    /**
     * 将请求的json结果解析为版本更新信息实体【同步方法】
     *
     * @param json 请求的json数据
     * @return 版本更新信息实体
     * @throws Exception 解析出错
     */
    UpdateEntity parseJson(@NonNull String json) throws Exception;

    /**
     * 将请求的json结果解析为版本更新信息实体【异步方法】
     *
     * @param json     请求的json数据
     * @param callback 解析回调
     * @throws Exception 解析出错
     */
    void parseJson(@NonNull String json, final IUpdateParseCallback callback) throws Exception;

    //=============IPrompterProxy================//

    /**
     * 开始下载更新
     *
     * @param updateEntity     更新信息
     * @param downloadListener 文件下载监听
     */
    void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener);

    /**
     * 后台下载
     */
    void backgroundDownload();

    /**
     * 取消下载
     */
    void cancelDownload();

    /**
     * 资源回收
     */
    void recycle();

}
