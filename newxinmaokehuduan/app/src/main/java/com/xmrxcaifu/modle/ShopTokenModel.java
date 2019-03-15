package com.xmrxcaifu.modle;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ShopTokenModel {


    /**
     * meta : {"success":true,"message":"获取成功","code":null,"data":null}
     * data : {"token":"NmZoRU5LMUdKbnd3R2RSVjI0UGUvWUZuUDlUdDVwRi9HTFdPbE01WVVPUERkbXdVWHcrdklkQjJyMVVuYlVVMg=="}
     */

    private MetaBean meta;
    private DataBean data;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class MetaBean {
        /**
         * success : true
         * message : 获取成功
         * code : null
         * data : null
         */

        private boolean success;
        private String message;
        private Object code;
        private Object data;

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

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    public static class DataBean {
        /**
         * token : NmZoRU5LMUdKbnd3R2RSVjI0UGUvWUZuUDlUdDVwRi9HTFdPbE01WVVPUERkbXdVWHcrdklkQjJyMVVuYlVVMg==
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
