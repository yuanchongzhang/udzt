package com.xmrxcaifu.http.request;




import com.xmrxcaifu.http.model.HttpHeaders;
import com.xmrxcaifu.http.utils.HttpUtils;
import com.xmrxcaifu.http.utils.OkLogger;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ================================================
 * 作    者：张宇
 * 版    本：1.0
 * 创建日期：2016/1/12
 * 描    述：Post请求的实现类，注意需要传入本类的泛型
 * 修订历史：
 * ================================================
 */
public class PostRequest extends BaseBodyRequest<PostRequest> {

    public PostRequest(String url) {
        super(url);
        method = "POST";
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        try {
            headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.post(requestBody).url(url).tag(tag).build();
    }
}