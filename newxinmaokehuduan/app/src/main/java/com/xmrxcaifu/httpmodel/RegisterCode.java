package com.xmrxcaifu.httpmodel;

/**
 * Created by Administrator on 2018/3/12.
 */

public class RegisterCode {


    /**
     * meta : {"success":true,"message":"验证码发送成功","code":null}
     * data : null
     */

    private MetaBean meta;
    private String data;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class MetaBean {
        /**
         * success : true
         * message : 验证码发送成功
         * code : null
         */

        private boolean success;
        private String message;
        private String code;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
