package com.basic.http2.request;

import com.basic.http2.cache.model.CacheResult;
import com.basic.http2.callback.CallBack;
import com.basic.http2.callback.CallBackProxy;
import com.basic.http2.model.ApiResult;
import com.basic.http2.subsciber.CallBackSubscriber;
import com.basic.http2.transform.HandleErrTransformer;
import com.basic.http2.transform.HttpResultTransformer;
import com.basic.http2.transform.HttpSchedulersTransformer;
import com.basic.http2.transform.func.CacheResultFunc;
import com.basic.http2.transform.func.RetryExceptionFunc;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * <p>描述：自定义请求，例如你有自己的ApiService</p>
 */
@SuppressWarnings(value = {"unchecked"})
public class CustomRequest extends BaseRequest<CustomRequest> {

    public CustomRequest() {
        super("");
    }

    @Override
    public CustomRequest build() {
        return super.build();
    }

    /**
     * 创建api服务  可以支持自定义的api，默认使用BaseApiService,上层不用关心
     *
     * @param service 自定义的ApiService class
     */
    public <T> T create(final Class<T> service) {
        checkValidate();
        return mRetrofit.create(service);
    }

    private void checkValidate() {
        if (mRetrofit == null) {
            build();
        }
    }

    //=================apiCall====================//

    /**
     * 针对retrofit定义的接口，返回的是Observable<ApiResult<T>>的情况<br>
     * <p>
     * 对ApiResult进行拆包，直接获取数据
     *
     * @param observable retrofit定义接口返回的类型
     */
    public <T> Observable<T> apiCall(Observable<? extends ApiResult<T>> observable) {
        checkValidate();
        return observable
                .compose(new HttpResultTransformer())
                .compose(new HttpSchedulersTransformer(mIsSyncRequest, mIsOnMainThread))
                .retryWhen(new RetryExceptionFunc(mRetryCount, mRetryDelay, mRetryIncreaseDelay));
    }

    /**
     * 针对retrofit定义的接口，返回的是Observable<ApiResult<T>>的情况<br>
     * <p>
     * 对ApiResult进行拆包，直接获取数据
     *
     * @param observable retrofit定义接口返回的类型
     */
    public <T> Disposable apiCall(Observable observable, CallBack<T> callBack) {
        return call(observable, new CallBackProxy<ApiResult<T>, T>(callBack) {
        });
    }

    //=================call====================//

    /**
     * 针对retrofit定义的接口，返回的是Observable<T>的情况<br>
     * <p>
     * 不对ApiResult进行拆包，返回服务端响应的ApiResult
     *
     * @param observable retrofit定义接口返回的类型
     */
    public <T> Observable<T> call(Observable<T> observable) {
        checkValidate();
        return observable
                .compose(new HandleErrTransformer())
                .compose(new HttpSchedulersTransformer(mIsSyncRequest, mIsOnMainThread))
                .retryWhen(new RetryExceptionFunc(mRetryCount, mRetryDelay, mRetryIncreaseDelay));
    }

    /**
     * 针对retrofit定义的接口，返回的是Observable<T>的情况<br>
     * <p>
     * 不对ApiResult进行拆包，返回服务端响应的ApiResult
     *
     * @param observable retrofit定义接口返回的类型
     * @param callBack   网络请求回调
     */
    public <T> void call(Observable<T> observable, CallBack<T> callBack) {
        call(observable, new CallBackSubscriber(callBack));
    }

    /**
     * 针对retrofit定义的接口，返回的是Observable<T>的情况<br>
     * <p>
     * 不对ApiResult进行拆包，返回服务端响应的ApiResult
     *
     * @param observable retrofit定义接口返回的类型
     * @param subscriber 请求订阅
     * @param <R>
     */
    public <R> void call(Observable<R> observable, Observer<R> subscriber) {
        call(observable).subscribe(subscriber);
    }

    private <T> Disposable call(Observable<T> observable, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        Observable<CacheResult<T>> cacheObservable = build().toObservable(observable, proxy);
        if (CacheResult.class != proxy.getRawType()) {
            return cacheObservable.compose(new ObservableTransformer<CacheResult<T>, T>() {
                @Override
                public ObservableSource<T> apply(@NonNull Observable<CacheResult<T>> upstream) {
                    return upstream.map(new CacheResultFunc<T>());
                }
            }).subscribeWith(new CallBackSubscriber<T>(proxy.getCallBack()));
        } else {
            return cacheObservable.subscribeWith(new CallBackSubscriber<CacheResult<T>>(proxy.getCallBack()));
        }
    }

    @Override
    protected <T> Observable<CacheResult<T>> toObservable(Observable observable, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        checkValidate();
        return observable
                .compose(new HttpResultTransformer())
                .compose(new HttpSchedulersTransformer(mIsSyncRequest, mIsOnMainThread))
                .compose(mRxCache.transformer(mCacheMode, proxy.getCallBack().getType()))
                .retryWhen(new RetryExceptionFunc(mRetryCount, mRetryDelay, mRetryIncreaseDelay));
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        return null;
    }
}
