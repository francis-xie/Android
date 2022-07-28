package com.basic.http2.utils;

import com.basic.http2.model.SchedulerType;
import com.basic.http2.transform.HttpSchedulersTransformer;

import io.reactivex.ObservableTransformer;

/**
 * <p>描述：线程调度工具</p>
 */
public class RxSchedulers {

    private RxSchedulers() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 订阅发生在主线程 （  ->  -> main)
     * 使用compose操作符
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> _main() {
        return new HttpSchedulersTransformer<>(SchedulerType._main);
    }

    /**
     * 订阅发生在io线程 （  ->  -> io)
     * 使用compose操作符
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> _io() {
        return new HttpSchedulersTransformer<>(SchedulerType._io);
    }


    /**
     * 处理在io线程，订阅发生在主线程（ -> io -> main)
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> _io_main() {
        return new HttpSchedulersTransformer<>(SchedulerType._io_main);
    }


    /**
     * 处理在io线程，订阅也发生在io线程（ -> io -> io)
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> _io_io() {
        return new HttpSchedulersTransformer<>(SchedulerType._io_io);
    }

}
