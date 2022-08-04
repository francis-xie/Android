package com.basic.http2.request;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Put请求
 */
public class PutRequest extends BaseBodyRequest<PutRequest> {

    public PutRequest(String url) {
        super(url);
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        // 自定义的请求体
        if (mRequestBody != null) {
            return mApiManager.putBody(getUrl(), mRequestBody);
        } else if (mJson != null) {
            // Json
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), mJson);
            return mApiManager.putJson(getUrl(), body);
        } else if (mObject != null) {
            // 自定义的请求object
            return mApiManager.putBody(getUrl(), mObject);
        } else if (mString != null) {
            // 文本内容
            RequestBody body = RequestBody.create(mMediaType, mString);
            return mApiManager.putBody(getUrl(), body);
        } else {
            return mApiManager.put(getUrl(), mParams.urlParamsMap);
        }
    }
}
