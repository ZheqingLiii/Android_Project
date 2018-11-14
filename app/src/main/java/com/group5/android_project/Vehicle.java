package com.group5.android_project;

import android.os.Parcel;
import android.os.Parcelable;


public class Vehicle implements Parcelable {

    public static final Parcelable.Creator<Vehicle> CREATOR
            = new Parcelable.Creator<Vehicle>() {

        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }

    };
    private String model;
    private String year;
    private String city;
    private String detail;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String imageUrl;

    public Vehicle(String model, String year, String city, String detail) {
        this.model = model;
        this.year = year;
        this.city = city;
        this.detail = detail;
    }

    public Vehicle(Parcel source) {
        model = source.readString();
        year = source.readString();
        city = source.readString();
        detail = source.readString();
        startDate = source.readString();
        endDate = source.readString();
        startTime = source.readString();
        endTime = source.readString();
        imageUrl = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(model);
        dest.writeString(year);
        dest.writeString(city);
        dest.writeString(detail);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}