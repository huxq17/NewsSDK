package com.aiqing.newssdk.news;

public enum Category {
    CAR("汽车"), ENT("娱乐"), HOT("热点"), SOCIAL("社会"), TECH("科技"), VIDEO("视频");
    private String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static int size() {
        return Category.values().length;
    }

    public String toString() {
        return super.toString().toLowerCase();
    }
}
