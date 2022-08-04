package com.basic.http2.transform.func;

import com.basic.http2.exception.ApiException;
import com.basic.http2.logs.HttpLog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

import static com.basic.http2.Http.DEFAULT_RETRY_COUNT;
import static com.basic.http2.Http.DEFAULT_RETRY_DELAY;
import static com.basic.http2.Http.DEFAULT_RETRY_INCREASE_DELAY;

/**
 * 网络请求错误重试条件
 */
public class RetryExceptionFunc implements Function<Observable<? extends Throwable>, Observable<?>> {
    /**
     * retry次数
     */
    private int count = DEFAULT_RETRY_COUNT;
    /**
     * 延迟
     */
    private long delay = DEFAULT_RETRY_DELAY;
    /**
     * 叠加延迟
     */
    private long increaseDelay = DEFAULT_RETRY_INCREASE_DELAY;

    public RetryExceptionFunc() {

    }

    public RetryExceptionFunc(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    public RetryExceptionFunc(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable.zipWith(Observable.range(1, count + 1), new BiFunction<Throwable, Integer, Wrapper>() {
            @Override
            public Wrapper apply(@NonNull Throwable throwable, @NonNull Integer integer) throws Exception {
                return new Wrapper(throwable, integer);
            }
        }).flatMap(new Function<Wrapper, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Wrapper wrapper) throws Exception {
                if (wrapper.index > 1) {
                    HttpLog.i("重试次数：" + (wrapper.index));
                }
                int errCode = 0;
                if (wrapper.throwable instanceof ApiException) {
                    ApiException exception = (ApiException) wrapper.throwable;
                    errCode = exception.getCode();
                }
                if ((wrapper.throwable instanceof ConnectException
                        || wrapper.throwable instanceof SocketTimeoutException
                        || errCode == ApiException.ERROR.NET_WORD_ERROR
                        || errCode == ApiException.ERROR.TIMEOUT_ERROR
                        || wrapper.throwable instanceof SocketTimeoutException
                        || wrapper.throwable instanceof TimeoutException)
                        && wrapper.index < count + 1) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                    return Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS);

                }
                return Observable.error(wrapper.throwable);
            }
        });
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }

}
