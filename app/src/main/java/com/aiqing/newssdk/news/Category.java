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

    public static Category get(int index) {
        int i=0;
        for (Category category : Category.values()) {
            if(index==i){
                return category;
            }
            i++;
        }
        return null;
    }

    public String toString() {
        return super.toString().toLowerCase();
    }
}
