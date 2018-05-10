package com.example.anumy.searchonfb;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by anumy on 18-04-2017.
 */

public class Albums implements Parcelable {
    private String albumName;
    private ArrayList<String> hdImageUrl;

    public Albums(String albumName, ArrayList<String> pictureId) {
        this.albumName = albumName;
        this.hdImageUrl = pictureId;
    }

    protected Albums(Parcel in) {
        albumName = in.readString();
        hdImageUrl = in.createStringArrayList();
    }

    public static final Creator<Albums> CREATOR = new Creator<Albums>() {
        @Override
        public Albums createFromParcel(Parcel in) {
            return new Albums(in);
        }

        @Override
        public Albums[] newArray(int size) {
            return new Albums[size];
        }
    };

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<String> getPictureId() {
        return hdImageUrl;
    }

    public void setPictureId(ArrayList<String> pictureId) {
        this.hdImageUrl = pictureId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumName);
        dest.writeStringList(hdImageUrl);
    }
}
