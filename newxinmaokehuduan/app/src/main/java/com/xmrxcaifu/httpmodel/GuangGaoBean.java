package com.xmrxcaifu.httpmodel;

/**
 * Created by Administrator on 2018/3/27.
 */

public class GuangGaoBean {


    /**
     * meta : {"success":true,"message":"","code":null}
     * data : {"pageNo":1,"pageSize":20,"id":19,"entId":null,"pictureUrl":"http://xmrxcaifu.test.upcdn.net/image/advertiseMentImg/20180326155226.jpg","linkUrl":"http://www.baidu.com","platform":1,"position":6,"seq":"1","deleted":0,"createdBy":null,"createTime":"2018-03-26 16:03:07","updatedBy":null,"updateTime":"2018-03-26 17:52:39","count":null,"picList":null,"linkList":null,"seqList":null}
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
         * message :
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
         * id : 19
         * entId : null
         * pictureUrl : http://xmrxcaifu.test.upcdn.net/image/advertiseMentImg/20180326155226.jpg
         * linkUrl : http://www.baidu.com
         * platform : 1
         * position : 6
         * seq : 1
         * deleted : 0
         * createdBy : null
         * createTime : 2018-03-26 16:03:07
         * updatedBy : null
         * updateTime : 2018-03-26 17:52:39
         * count : null
         * picList : null
         * linkList : null
         * seqList : null
         */

        private int pageNo;
        private int pageSize;
        private int id;
        private String entId;
        private String pictureUrl;
        private String linkUrl;
        private int platform;
        private int position;
        private String seq;
        private int deleted;
        private String createdBy;
        private String createTime;
        private String updatedBy;
        private String updateTime;
        private String count;
        private String picList;
        private String linkList;
        private String seqList;

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

        public Object getEntId() {
            return entId;
        }

        public void setEntId(String entId) {
            this.entId = entId;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public Object getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public Object getPicList() {
            return picList;
        }

        public void setPicList(String picList) {
            this.picList = picList;
        }

        public String getLinkList() {
            return linkList;
        }

        public void setLinkList(String linkList) {
            this.linkList = linkList;
        }

        public String getSeqList() {
            return seqList;
        }

        public void setSeqList(String seqList) {
            this.seqList = seqList;
        }
    }
}
