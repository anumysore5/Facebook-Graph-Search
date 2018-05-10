package com.example.anumy.searchonfb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anumy on 18-04-2017.
 */

public class Posts implements Parcelable{
    private String name;
    private String dateTime;
    private String message;
    private String imageUrl;

    public Posts(String name, String dateTime, String message, String imageUrl) {
        this.name = name;
        this.dateTime = dateTime;
        this.message = message;
        this.imageUrl = imageUrl;
    }

    protected Posts(Parcel in) {
        name = in.readString();
        dateTime = in.readString();
        message = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateTime);
        dest.writeString(message);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Posts> CREATOR = new Creator<Posts>() {
        @Override
        public Posts createFromParcel(Parcel in) {
            return new Posts(in);
        }

        @Override
        public Posts[] newArray(int size) {
            return new Posts[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
