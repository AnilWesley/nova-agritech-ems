package com.agritech.empmanager.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Leave implements Parcelable {

    public String leaveType;

    public String appliedOn;

    public String year;

    public long fromDate;

    public long toDate;

    public String subject;

    public String description;

    public String status;

    public String appliedByUID;

    public String appliedByName;

    public String statusUpdateByUID;

    public String statusUpdateByName;

    public String uuid;


    public String fromDateAs;
    public String toDateAs;


    public Leave() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.leaveType);
        dest.writeString(this.appliedOn);
        dest.writeString(this.year);
        dest.writeLong(this.fromDate);
        dest.writeLong(this.toDate);
        dest.writeString(this.subject);
        dest.writeString(this.description);
        dest.writeString(this.status);
        dest.writeString(this.appliedByUID);
        dest.writeString(this.appliedByName);
        dest.writeString(this.statusUpdateByUID);
        dest.writeString(this.statusUpdateByName);
        dest.writeString(this.uuid);
        dest.writeString(this.fromDateAs);
        dest.writeString(this.toDateAs);
    }

    protected Leave(Parcel in) {
        this.leaveType = in.readString();
        this.appliedOn = in.readString();
        this.year = in.readString();
        this.fromDate = in.readLong();
        this.toDate = in.readLong();
        this.subject = in.readString();
        this.description = in.readString();
        this.status = in.readString();
        this.appliedByUID = in.readString();
        this.appliedByName = in.readString();
        this.statusUpdateByUID = in.readString();
        this.statusUpdateByName = in.readString();
        this.uuid = in.readString();
        this.fromDateAs = in.readString();
        this.toDateAs = in.readString();
    }

    public static final Parcelable.Creator<Leave> CREATOR = new Parcelable.Creator<Leave>() {
        @Override
        public Leave createFromParcel(Parcel source) {
            return new Leave(source);
        }

        @Override
        public Leave[] newArray(int size) {
            return new Leave[size];
        }
    };
}
