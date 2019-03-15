package com.xmrxcaifu.modle;

public class YIjuhua {


    /**
     * meta : {"success":true,"message":"查询成功!","code":null}
     * data : {"pageNo":1,"pageSize":20,"adsId":1,"adsImageCategoryId":null,"iconType":"","publicTime":null,"offlineTime":null,"recordTime":null,"adsTitle":"永不言退，我们是最好的团队！","linkUrl":"","url":"","seq":1,"appSite":true,"mzSite":false,"pubStatus":"1","createdBy":null,"createTime":null,"updatedBy":null,"updateTime":null}
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
         * pageNo : 1
         * pageSize : 20
         * adsId : 1
         * adsImageCategoryId : null
         * iconType :
         * publicTime : null
         * offlineTime : null
         * recordTime : null
         * adsTitle : 永不言退，我们是最好的团队！
         * linkUrl :
         * url :
         * seq : 1
         * appSite : true
         * mzSite : false
         * pubStatus : 1
         * createdBy : null
         * createTime : null
         * updatedBy : null
         * updateTime : null
         */

        private int pageNo;
        private int pageSize;
        private int adsId;
        private Object adsImageCategoryId;
        private String iconType;
        private Object publicTime;
        private Object offlineTime;
        private Object recordTime;
        private String adsTitle;
        private String linkUrl;
        private String url;
        private int seq;
        private boolean appSite;
        private boolean mzSite;
        private String pubStatus;
        private Object createdBy;
        private Object createTime;
        private Object updatedBy;
        private Object updateTime;

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

        public int getAdsId() {
            return adsId;
        }

        public void setAdsId(int adsId) {
            this.adsId = adsId;
        }

        public Object getAdsImageCategoryId() {
            return adsImageCategoryId;
        }

        public void setAdsImageCategoryId(Object adsImageCategoryId) {
            this.adsImageCategoryId = adsImageCategoryId;
        }

        public String getIconType() {
            return iconType;
        }

        public void setIconType(String iconType) {
            this.iconType = iconType;
        }

        public Object getPublicTime() {
            return publicTime;
        }

        public void setPublicTime(Object publicTime) {
            this.publicTime = publicTime;
        }

        public Object getOfflineTime() {
            return offlineTime;
        }

        public void setOfflineTime(Object offlineTime) {
            this.offlineTime = offlineTime;
        }

        public Object getRecordTime() {
            return recordTime;
        }

        public void setRecordTime(Object recordTime) {
            this.recordTime = recordTime;
        }

        public String getAdsTitle() {
            return adsTitle;
        }

        public void setAdsTitle(String adsTitle) {
            this.adsTitle = adsTitle;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public boolean isAppSite() {
            return appSite;
        }

        public void setAppSite(boolean appSite) {
            this.appSite = appSite;
        }

        public boolean isMzSite() {
            return mzSite;
        }

        public void setMzSite(boolean mzSite) {
            this.mzSite = mzSite;
        }

        public String getPubStatus() {
            return pubStatus;
        }

        public void setPubStatus(String pubStatus) {
            this.pubStatus = pubStatus;
        }

        public Object getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Object createdBy) {
            this.createdBy = createdBy;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(Object updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }
    }
}
