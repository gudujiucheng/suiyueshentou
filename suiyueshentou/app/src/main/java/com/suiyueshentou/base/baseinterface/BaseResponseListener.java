package com.suiyueshentou.base.baseinterface;


/**
 * 请求响应回调（用于解耦）
 * Created by zc on 2016/8/5.
 */
public interface BaseResponseListener<T> {
    /**
     * 开始请求
     */
    void OnRequestStart();

    /**
     * 请求成功
     */
    void OnRequestSuccess(T t);


    /**
     * 其他类型错误
     */
    void OnRequestError(Exception error);
}
