package com.xmrxcaifu.util;

public class PushInfo {


    /**
     * policy : {"expire_time":"2018-07-09 17:53:02"}
     * description : 世界杯测试2
     * production_mode : true
     * appkey : 5913c832677baa7b9f000631
     * payload : {"body":{"title":"今日竞猜，火热展开","ticker":"今日竞猜，火热展开","text":"今日竞猜场次：巴西VS墨西哥，比利时VS日本。竞猜火热进行中，赶紧投注你支持的球队，猜中获胜方就有机会将50g金条、小米电视等精美大礼带回家。","after_open":"go_app","play_vibrate":"true","play_lights":"true","play_sound":"true"},"display_type":"notification","extra":{"linkUrl":"https://wealth.xmrxcaifu.com/#/worldCup","openRule":"openWithWeb"}}
     * mipush : true
     * mi_activity : com.xmrxcaifu.MainActivity
     * device_tokens : AlEBBfselEzEyioNmphmQlwmlcPbqoZvgFNN4_x-uuBd
     * type : unicast
     * timestamp : 1531102470255
     */

    private PolicyBean policy;
    private String description;
    private boolean production_mode;
    private String appkey;
    private PayloadBean payload;
    private boolean mipush;
    private String mi_activity;
    private String device_tokens;
    private String type;
    private String timestamp;

    public PolicyBean getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyBean policy) {
        this.policy = policy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isProduction_mode() {
        return production_mode;
    }

    public void setProduction_mode(boolean production_mode) {
        this.production_mode = production_mode;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public boolean isMipush() {
        return mipush;
    }

    public void setMipush(boolean mipush) {
        this.mipush = mipush;
    }

    public String getMi_activity() {
        return mi_activity;
    }

    public void setMi_activity(String mi_activity) {
        this.mi_activity = mi_activity;
    }

    public String getDevice_tokens() {
        return device_tokens;
    }

    public void setDevice_tokens(String device_tokens) {
        this.device_tokens = device_tokens;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class PolicyBean {
        /**
         * expire_time : 2018-07-09 17:53:02
         */

        private String expire_time;

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }
    }

    public static class PayloadBean {
        /**
         * body : {"title":"今日竞猜，火热展开","ticker":"今日竞猜，火热展开","text":"今日竞猜场次：巴西VS墨西哥，比利时VS日本。竞猜火热进行中，赶紧投注你支持的球队，猜中获胜方就有机会将50g金条、小米电视等精美大礼带回家。","after_open":"go_app","play_vibrate":"true","play_lights":"true","play_sound":"true"}
         * display_type : notification
         * extra : {"linkUrl":"https://wealth.xmrxcaifu.com/#/worldCup","openRule":"openWithWeb"}
         */

        private BodyBean body;
        private String display_type;
        private ExtraBean extra;

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public String getDisplay_type() {
            return display_type;
        }

        public void setDisplay_type(String display_type) {
            this.display_type = display_type;
        }

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public static class BodyBean {
            /**
             * title : 今日竞猜，火热展开
             * ticker : 今日竞猜，火热展开
             * text : 今日竞猜场次：巴西VS墨西哥，比利时VS日本。竞猜火热进行中，赶紧投注你支持的球队，猜中获胜方就有机会将50g金条、小米电视等精美大礼带回家。
             * after_open : go_app
             * play_vibrate : true
             * play_lights : true
             * play_sound : true
             */

            private String title;
            private String ticker;
            private String text;
            private String after_open;
            private String play_vibrate;
            private String play_lights;
            private String play_sound;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTicker() {
                return ticker;
            }

            public void setTicker(String ticker) {
                this.ticker = ticker;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getAfter_open() {
                return after_open;
            }

            public void setAfter_open(String after_open) {
                this.after_open = after_open;
            }

            public String getPlay_vibrate() {
                return play_vibrate;
            }

            public void setPlay_vibrate(String play_vibrate) {
                this.play_vibrate = play_vibrate;
            }

            public String getPlay_lights() {
                return play_lights;
            }

            public void setPlay_lights(String play_lights) {
                this.play_lights = play_lights;
            }

            public String getPlay_sound() {
                return play_sound;
            }

            public void setPlay_sound(String play_sound) {
                this.play_sound = play_sound;
            }
        }

        public static class ExtraBean {
            /**
             * linkUrl : https://wealth.xmrxcaifu.com/#/worldCup
             * openRule : openWithWeb
             */

            private String linkUrl;
            private String openRule;

            public String getLinkUrl() {
                return linkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
            }

            public String getOpenRule() {
                return openRule;
            }

            public void setOpenRule(String openRule) {
                this.openRule = openRule;
            }
        }
    }
}
