package com.xmrxcaifu.http.convert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * ================================================
 * 作    者： （张宇）  ：
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：字符串的转换器
 * 修订历史：
 * ================================================
 */
public class BitmapConvert implements Converter<Bitmap> {

    public static BitmapConvert create() {
        return ConvertHolder.convert;
    }

    private static class ConvertHolder {
        private static BitmapConvert convert = new BitmapConvert();
    }

    @Override
    public Bitmap convertSuccess(Response value) throws Exception {
        return BitmapFactory.decodeStream(value.body().byteStream());
    }
}