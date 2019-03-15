package com.xmrxcaifu.httpmodel;

/**
 * Created by Administrator on 2018/3/8.
 */

public class LoginBean {

    /**
     * meta : {"success":true,"message":"登录成功","code":null}
     * data : {"id":22683,"username":"13245674567","phone":"13245674567","idCard":null,"token":"LWGzRg1DO0/ZOPTC16dGEMy6YbTlSScC","logined":true,"secName":"13245674567","gender":0,"appHeadUrl":null}
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
         * message : 登录成功
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

    public static class DataBean {
        /**
         * id : 22683
         * username : 13245674567
         * phone : 13245674567
         * idCard : null
         * token : LWGzRg1DO0/ZOPTC16dGEMy6YbTlSScC
         * logined : true
         * secName : 13245674567
         * gender : 0
         * appHeadUrl : null
         */

        private int id;
        private String username;
        private String phone;
        private String idCard;
        private String token;
        private boolean logined;
        private String secName;
        private String gender;
        private String appHeadUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isLogined() {
            return logined;
        }

        public void setLogined(boolean logined) {
            this.logined = logined;
        }

        public String getSecName() {
            return secName;
        }

        public void setSecName(String secName) {
            this.secName = secName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAppHeadUrl() {
            return appHeadUrl;
        }

        public void setAppHeadUrl(String appHeadUrl) {
            this.appHeadUrl = appHeadUrl;
        }
    }
}
