package com.xmrxcaifu.httpmodel;

/**
 * Created by Administrator on 2018/3/8.
 */

public class RegisterBean {

    /**
     * meta : {"success":true,"message":"注册成功","code":null}
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class MetaBean {
        /**
         * success : true
         * message : 注册成功
         * code : null
         */

        private boolean success;
        private String message;
        private Object code;

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

        public void setCode(Object code) {
            this.code = code;
        }
    }
}
