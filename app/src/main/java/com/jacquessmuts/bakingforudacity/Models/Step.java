
package com.jacquessmuts.bakingforudacity.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Step implements Parcelable {

    private Long id;
    private String description;
    private String shortDescription;
    private String thumbnailURL;
    private String videoURL;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getVideoURL() {

        if (TextUtils.isEmpty(videoURL) && !TextUtils.isEmpty(thumbnailURL) ){
            return thumbnailURL;
        }

        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Step)) return false;

        return (this.id == ((Step) obj).getId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.description);
        dest.writeString(this.shortDescription);
        dest.writeString(this.thumbnailURL);
        dest.writeString(this.videoURL);
    }

    public Step() {
    }

    protected Step(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.description = in.readString();
        this.shortDescription = in.readString();
        this.thumbnailURL = in.readString();
        this.videoURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
