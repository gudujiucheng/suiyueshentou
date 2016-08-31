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
     * 接口反馈错误
     */
    void OnRequestInterfaceError(String errorMsg);

    /**
     * 其他类型错误
     */
    void OnRequestOtherError(Exception error);
}
