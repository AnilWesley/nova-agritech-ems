package com.agritech.empmanager.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Emp implements Parcelable {

    /*Basic info*/

    public String uid;
    public String empId;

    public String fName;
    public String lName;

    public String email;

    public String esiNumber;
    public String pfNumber;



    /*Work info*/

    public String designation;
    public String department;
    public String sourceOfHire;
    public String reportingToName;
    public String reportingToUID;


    /*Work Location*/
    public String state;
    public String distic;
    public String mandal;
    public String village;
    public String headQuarters;


    /*Personal info*/
    public String phone;
    public String dob;
    public String maritalStatus;
    public String address;


    /*Dependent info*/
    public String depName;
    public String depRelationship;
    public String depDOB;
    public String depOccupation;


    /*Leaves info*/
    public String casualLeaves;
    public String sickLeaves;
    public String annualLeaves;
    public String maternalLeaves;
    public String paternalLeaves;


    ArrayList<String> teams;

    public Emp() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.empId);
        dest.writeString(this.fName);
        dest.writeString(this.lName);
        dest.writeString(this.email);
        dest.writeString(this.esiNumber);
        dest.writeString(this.pfNumber);
        dest.writeString(this.designation);
        dest.writeString(this.department);
        dest.writeString(this.sourceOfHire);
        dest.writeString(this.reportingToName);
        dest.writeString(this.reportingToUID);
        dest.writeString(this.state);
        dest.writeString(this.distic);
        dest.writeString(this.mandal);
        dest.writeString(this.village);
        dest.writeString(this.headQuarters);
        dest.writeString(this.phone);
        dest.writeString(this.dob);
        dest.writeString(this.maritalStatus);
        dest.writeString(this.address);
        dest.writeString(this.depName);
        dest.writeString(this.depRelationship);
        dest.writeString(this.depDOB);
        dest.writeString(this.depOccupation);
        dest.writeString(this.casualLeaves);
        dest.writeString(this.sickLeaves);
        dest.writeString(this.annualLeaves);
        dest.writeString(this.maternalLeaves);
        dest.writeString(this.paternalLeaves);
        dest.writeStringList(this.teams);
    }



    protected Emp(Parcel in) {
        this.uid = in.readString();
        this.empId = in.readString();
        this.fName = in.readString();
        this.lName = in.readString();
        this.email = in.readString();
        this.esiNumber = in.readString();
        this.pfNumber = in.readString();
        this.designation = in.readString();
        this.department = in.readString();
        this.sourceOfHire = in.readString();
        this.reportingToName = in.readString();
        this.reportingToUID = in.readString();
        this.state = in.readString();
        this.distic = in.readString();
        this.mandal = in.readString();
        this.village = in.readString();
        this.headQuarters = in.readString();
        this.phone = in.readString();
        this.dob = in.readString();
        this.maritalStatus = in.readString();
        this.address = in.readString();
        this.depName = in.readString();
        this.depRelationship = in.readString();
        this.depDOB = in.readString();
        this.depOccupation = in.readString();
        this.casualLeaves = in.readString();
        this.sickLeaves = in.readString();
        this.annualLeaves = in.readString();
        this.maternalLeaves = in.readString();
        this.paternalLeaves = in.readString();
        this.teams = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Emp> CREATOR = new Parcelable.Creator<Emp>() {
        @Override
        public Emp createFromParcel(Parcel source) {
            return new Emp(source);
        }

        @Override
        public Emp[] newArray(int size) {
            return new Emp[size];
        }
    };
}
