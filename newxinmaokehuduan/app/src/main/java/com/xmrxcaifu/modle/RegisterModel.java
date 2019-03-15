package com.xmrxcaifu.modle;

/**
 * Created by Administrator on 2018/3/23.
 */

public class RegisterModel {


    /**
     * meta : {"success":false,"message":"短信验证码格式不正确","code":null}
     * data : null
     */

    private MetaBean meta;
    private Object data;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class MetaBean {
        /**
         * success : false
         * message : 短信验证码格式不正确
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
