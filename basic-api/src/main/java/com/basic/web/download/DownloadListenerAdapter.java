
package com.basic.web.download;

/**
 
 * @date 2018/2/11
 */
public class DownloadListenerAdapter implements DownloadListener, DownloadingListener {


    @Override
    public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, WebDownloader.Extra extra) {
        return false;
    }

    @Override
    public void onBindService(String url, DownloadingService downloadingService) {

    }

    @Override
    public void onProgress(String url, long downloaded, long length, long usedTime) {

    }

    @Override
    public void onUnbindService(String url, DownloadingService downloadingService) {

    }

    @Override
    public boolean onResult(String path, String url, Throwable e) {
        return false;
    }
}
