package com.dal4.attendance.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "StudentsTable")
public class StudentModel {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "StdName")
    public String StdName;

    @ColumnInfo(name = "StdID")
    public String StdID;

    @ColumnInfo(name = "StdGroup")
    public String StdGroup;

    @ColumnInfo(name = "SubUid")
    public String SubUid;

    public StudentModel(String stdName, String stdID, String stdGroup, String subUid) {
        StdName = stdName;
        StdID = stdID;
        StdGroup = stdGroup;
        SubUid = subUid;
    }

    public StudentModel() {
    }

    public String getStdName() {
        return StdName;
    }

    public void setStdName(String stdName) {
        StdName = stdName;
    }

    public String getStdID() {
        return StdID;
    }

    public void setStdID(String stdID) {
        StdID = stdID;
    }

    public String getStdGroup() {
        return StdGroup;
    }

    public void setStdGroup(String stdGroup) {
        StdGroup = stdGroup;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSubUid() {
        return SubUid;
    }

    public void setSubUid(String subUid) {
        SubUid = subUid;
    }
}
