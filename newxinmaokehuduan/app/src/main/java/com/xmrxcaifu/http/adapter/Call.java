package com.xmrxcaifu.http.adapter;


import com.xmrxcaifu.http.callback.AbsCallback;
import com.xmrxcaifu.http.model.Response;
import com.xmrxcaifu.http.request.BaseRequest;

/**
 * ================================================
 * 作    者：张宇
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：请求的包装类
 * 修订历史：
 * ================================================
 */
public interface Call<T> {
    /** 同步执行 */
    Response<T> execute() throws Exception;

    /** 异步回调执行 */
    void execute(AbsCallback<T> callback);

    /** 是否已经执行 */
    boolean isExecuted();

    /** 取消 */
    void cancel();

    /** 是否取消 */
    boolean isCanceled();

    Call<T> clone();

    BaseRequest getBaseRequest();
}