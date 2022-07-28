package com.basic.http2.request;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * <p>描述：删除请求</p>
 */
public class DeleteRequest extends BaseBodyRequest<DeleteRequest> {

    public DeleteRequest(String url) {
        super(url);
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        if (mRequestBody != null) {
            // 自定义的请求体
            return mApiManager.deleteBody(getUrl(), mRequestBody);
        } else if (mJson != null) {
            // Json
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), mJson);
            return mApiManager.deleteJson(getUrl(), body);
        } else if (mObject != null) {
            // 自定义的请求object
            return mApiManager.deleteBody(getUrl(), mObject);
        } else if (mString != null) {
            // 文本内容
            RequestBody body = RequestBody.create(mMediaType, mString);
            return mApiManager.deleteBody(getUrl(), body);
        } else {
            return mApiManager.delete(getUrl(), mParams.urlParamsMap);
        }
    }
}
