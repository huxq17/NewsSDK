package com.aiqing.newssdk.news;

import java.util.List;

public class NewsHtmlBean {

    /**
     * rc : 0
     * msg :
     * count : 1
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

        private String url;
        private String content;

        public String getUrl() {
            return url;
        }

        public String getContent() {
            return content;
        }
    }
}
