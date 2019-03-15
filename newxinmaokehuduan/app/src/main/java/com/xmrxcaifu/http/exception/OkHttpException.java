package com.xmrxcaifu.http.exception;

/**
 * ================================================
 * 作    者： （张宇）  ：
 * 版    本：1.0
 * 创建日期：16/8/28
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class OkHttpException extends Exception {

    public static OkHttpException INSTANCE(String msg) {
        return new OkHttpException(msg);
    }

    public OkHttpException() {
        super();
    }

    public OkHttpException(String detailMessage) {
        super(detailMessage);
    }

    public OkHttpException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OkHttpException(Throwable throwable) {
        super(throwable);
    }
}