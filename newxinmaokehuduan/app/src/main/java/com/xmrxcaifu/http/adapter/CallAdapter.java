package com.xmrxcaifu.http.adapter;

/**
 * ================================================
 * 作    者：张宇
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：返回值的适配器
 * 修订历史：
 * ================================================
 */
public interface CallAdapter<T> {

    /** call执行的代理方法 */
    <R> T adapt(Call<R> call);
}