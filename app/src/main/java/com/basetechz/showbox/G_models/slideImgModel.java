package com.basetechz.showbox.G_models;

public class slideImgModel {
    private String slideId,slideImg,slideTxt,videoUrl;

    public slideImgModel(){

    }

    public slideImgModel(String slideId, String slideImg, String slideTxt, String videoUrl) {
        this.slideId = slideId;
        this.slideImg = slideImg;
        this.slideTxt = slideTxt;
        this.videoUrl = videoUrl;
    }

    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }

    public String getSlideImg() {
        return slideImg;
    }

    public void setSlideImg(String slideImg) {
        this.slideImg = slideImg;
    }

    public String getSlideTxt() {
        return slideTxt;
    }

    public void setSlideTxt(String slideTxt) {
        this.slideTxt = slideTxt;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
