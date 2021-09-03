package com.vitap.aluminireconnect.model;


import java.util.Date;

public class FeedModel {
    String Title ;
    String Desc;
    String ImgUrl;
    String UserId;
    String PostId;
    Date Time;
    //String DyLink;
    boolean Event;
    Date RegEndTime;
    String RegLink;
    String Filter;

    public String getFilter() {
        return Filter;
    }

    public void setFilter(String filter) {
        Filter = filter;
    }

    public FeedModel(String title, String desc, String imgUrl, String userId, String postId, Date time, boolean event, Date regEndTime, String regLink) {
        Title = title;
        Desc = desc;
        ImgUrl = imgUrl;
        UserId = userId;
        PostId = postId;
        Time = time;
       // DyLink = dyLink;
        Event = event;
        RegEndTime = regEndTime;
        RegLink = regLink;
    }

    public boolean isEvent() {
        return Event;
    }

    public void setEvent(boolean event) {
        Event = event;
    }

    public Date getRegEndTime() {
        return RegEndTime;
    }

    public void setRegEndTime(Date regEndTime) {
        RegEndTime = regEndTime;
    }

    public String getRegLink() {
        return RegLink;
    }

    public void setRegLink(String regLink) {
        RegLink = regLink;
    }

    public FeedModel() {

    }

    public FeedModel(String filter, String desc, String imgUrl, String userId, String postId, Date time) {
        Filter = filter;
        Desc = desc;
        ImgUrl = imgUrl;
        UserId = userId;
        PostId = postId;
        Time = time;

    }

   /* public String getDyLink() {
        return DyLink;
    }

    public void setDyLink(String dyLink) {
        DyLink = dyLink;
    }

    */

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}

