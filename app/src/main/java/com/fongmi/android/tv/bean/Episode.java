package com.fongmi.android.tv.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fongmi.android.tv.utils.Utils;
import com.github.catvod.utils.Trans;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Episode implements Parcelable {

    @SerializedName("name")
    private String name;
    @SerializedName("desc")
    private String desc;
    @SerializedName("url")
    private String url;

    private final int number;
    private boolean activated;

    public static Episode create(String name, String url) {
        return new Episode(name, "", url);
    }

    public static Episode create(String name, String desc, String url) {
        return new Episode(name, desc, url);
    }

    public static Episode objectFrom(String str) {
        return new Gson().fromJson(str, Episode.class);
    }

    public Episode(String name, String desc, String url) {
        this.number = Utils.getDigit(name);
        this.name = Trans.s2t(name);
        this.desc = Trans.s2t(desc);
        this.url = url;
    }

    private Episode(Parcel in) {
        this.name = in.readString();
        this.desc = in.readString();
        this.url = in.readString();
        this.number = in.readInt();
        this.activated = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public int getNumber() {
        return number;
    }

    public boolean isActivated() {
        return activated;
    }

    public void deactivated() {
        this.activated = false;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean rule1(String name) {
        return getName().equalsIgnoreCase(name);
    }

    public boolean rule2(int number) {
        return getNumber() == number && number != -1;
    }

    public boolean rule3(String name) {
        return getName().toLowerCase().contains(name.toLowerCase());
    }

    public boolean rule4(String name) {
        return name.toLowerCase().contains(getName().toLowerCase());
    }

    public boolean equals(Episode episode) {
        return rule1(episode.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Episode)) return false;
        Episode it = (Episode) obj;
        return getUrl().equals(it.getUrl()) && getName().equals(it.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.url);
        dest.writeInt(this.number);
        dest.writeByte(this.activated ? (byte) 1 : (byte) 0);
    }

    public static final Creator<Episode> CREATOR = new Creator<>() {
        @Override
        public Episode createFromParcel(Parcel source) {
            return new Episode(source);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public static class Sorter implements Comparator<Episode> {

        public static List<Episode> sort(List<Episode> items) {
            if (items.size() > 1) Collections.sort(items, new Sorter());
            return items;
        }

        @Override
        public int compare(Episode o1, Episode o2) {
            return Integer.compare(o1.getNumber(), o2.getNumber());
        }
    }
}