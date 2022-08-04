package com.basic.util.rxbus;


import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * RxBus事件通知工具
 */
public final class RxBus {

    /**
     * 事件订阅的注册池， Key：事件名， value：事件的订阅者（事件的消费者、目标）
     */
    private ConcurrentHashMap<Object, CopyOnWriteArrayList<Subject>> maps = new ConcurrentHashMap<>();

    private static RxBus sInstance;

    /**
     * 获取RxBus的实例
     *
     * @return
     */
    public static RxBus get() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 注册事件的订阅
     *
     * @param eventName 事件名
     * @param <T>
     * @return 订阅者
     */
    public <T> Flowable<T> register(@NonNull Object eventName, Class<T> clazz) {
        return register(eventName).toFlowable(BackpressureStrategy.LATEST).ofType(clazz);
    }

    /**
     * 注册事件的订阅
     *
     * @param eventName 事件名
     * @return 订阅者
     */
    private <T> Subject<T> register(@NonNull Object eventName) {
        CopyOnWriteArrayList<Subject> subjects = maps.get(eventName);
        if (subjects == null) {
            subjects = new CopyOnWriteArrayList<>();
            maps.put(eventName, subjects);
        }
        Subject<T> subject = PublishSubject.<T>create().toSerialized();
        subjects.add(subject);
        return subject;
    }

    /**
     * 注销事件指定的订阅者
     *
     * @param eventName 事件名
     * @param flowable  需要取消的订阅者
     */
    public void unregister(@NonNull Object eventName, @NonNull Flowable flowable) {
        CopyOnWriteArrayList<Subject> subjects = maps.get(eventName);
        if (subjects != null) {
            subjects.remove(flowable);
            if (subjects.isEmpty()) {
                maps.remove(eventName);
            }
        }
    }

    /**
     * 注销事件所有的订阅（注销事件）
     *
     * @param eventName 事件名
     */
    public void unregisterAll(@NonNull Object eventName) {
        CopyOnWriteArrayList<Subject> subjects = maps.get(eventName);
        if (subjects != null) {
            maps.remove(eventName);
        }
    }

    /**
     * 发送指定的事件(不携带数据)
     *
     * @param eventName 事件名
     */
    public void post(@NonNull Object eventName) {
        post(eventName, eventName);
    }

    /**
     * 发送指定的事件（携带数据）
     *
     * @param eventName 注册标识
     * @param content   发送的内容
     */
    public void post(@NonNull Object eventName, @NonNull Object content) {
        CopyOnWriteArrayList<Subject> subjects = maps.get(eventName);
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject s : subjects) {
                s.onNext(content);
            }
        }
    }
}
