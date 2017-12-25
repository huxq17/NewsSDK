package com.aiqing.newssdk.news;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SDKNewsList {
    /**
     * rc : 0
     * msg : search ok
     * count : 10
     */

    private int rc;
    private String msg;
    private int count;
    private List<DataBean> data;

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 月销量破5万，为什么还有很多人攻击哈弗H6
         * abstract : 每一款车型上市都要挑战哈弗H6，但是都会被哈弗H6虐的体无完肤。
         * url : http://toutiao.com/group/6502215770913636878/
         * has_image : true
         * publish_time : 1513915083
         * keywords : H6,哈弗,SUV,丰田汉兰达,汉兰达,哈弗H6,诺基亚,发动机
         * video_detail_info : null
         * media_name : 汽车扒一扒
         * media_avatar_url : http://p5a.pstatp.com/large/1a6d000c60d617f4a1e8
         */

        private String title;
        @SerializedName("abstract")
        private String abstractX;
        private String url;
        private boolean has_image;
        private int publish_time;
        private String keywords;
        private MiddleImageBean middle_image;
        private Object video_detail_info;
        private String media_name;
        private String media_avatar_url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isHas_image() {
            return has_image;
        }

        public void setHas_image(boolean has_image) {
            this.has_image = has_image;
        }

        public int getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(int publish_time) {
            this.publish_time = publish_time;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public MiddleImageBean getMiddle_image() {
            return middle_image;
        }

        public void setMiddle_image(MiddleImageBean middle_image) {
            this.middle_image = middle_image;
        }

        public Object getVideo_detail_info() {
            return video_detail_info;
        }

        public void setVideo_detail_info(Object video_detail_info) {
            this.video_detail_info = video_detail_info;
        }

        public String getMedia_name() {
            return media_name;
        }

        public void setMedia_name(String media_name) {
            this.media_name = media_name;
        }

        public String getMedia_avatar_url() {
            return media_avatar_url;
        }

        public void setMedia_avatar_url(String media_avatar_url) {
            this.media_avatar_url = media_avatar_url;
        }

        public static class MiddleImageBean {
            /**
             * height : 401
             * uri : list/50ed0000a9565dadeb15
             * url : http://p3.pstatp.com/list/300x196/50ed0000a9565dadeb15.webp
             * width : 731
             */

            private int height;
            private String uri;
            private String url;
            private int width;
            private List<NewsBean> url_list;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<NewsBean> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<NewsBean> url_list) {
                this.url_list = url_list;
            }
        }
    }
}
