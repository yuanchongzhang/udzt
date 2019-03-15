package com.xmrxcaifu.modle;

/**
 * Created by Administrator on 2018/4/17.
 */

public class UpDateBean {


    /**
     * meta : {"success":true,"message":"查询成功!","code":null}
     * data : {"pageNo":1,"pageSize":20,"id":31,"description":"1、解锁界面视觉优化，新增日历功能；\n 2、启动页适配全面屏手机；\n 3、消息推送问题修复；\n 4、已知BUG修复，性能提升。","forceUpdate":1,"appUrl":"https://itunes.apple.com/us/app/id1229479417","version":"1.0.1","state":1,"type":1,"createTime":"2018-04-17 17:21:04","updateTime":"2018-04-10 13:45:21","createBy":null,"updateBy":null,"versionType":2}
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
         * message : 查询成功!
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class DataBean {
        /**
         * pageNo : 1
         * pageSize : 20
         * id : 31
         * description : 1、解锁界面视觉优化，新增日历功能；
         2、启动页适配全面屏手机；
         3、消息推送问题修复；
         4、已知BUG修复，性能提升。
         * forceUpdate : 1
         * appUrl : https://itunes.apple.com/us/app/id1229479417
         * version : 1.0.1
         * state : 1
         * type : 1
         * createTime : 2018-04-17 17:21:04
         * updateTime : 2018-04-10 13:45:21
         * createBy : null
         * updateBy : null
         * versionType : 2
         */

        private int pageNo;
        private int pageSize;
        private int id;
        private String description;
        private int forceUpdate;
        private String appUrl;
        private String version;
        private int state;
        private int type;
        private String createTime;
        private String updateTime;
        private String createBy;
        private String updateBy;
        private int versionType;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(int forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public int getVersionType() {
            return versionType;
        }

        public void setVersionType(int versionType) {
            this.versionType = versionType;
        }
    }
}
