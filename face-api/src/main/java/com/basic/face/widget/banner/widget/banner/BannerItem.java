package com.basic.face.widget.banner.widget.banner;

/**
 * 图片轮播条目
 */
public class BannerItem {
    public String imgUrl;
    public String title;

    public String getImgUrl() {
        return imgUrl;
    }

    public BannerItem setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BannerItem setTitle(String title) {
        this.title = title;
        return this;
    }
}
