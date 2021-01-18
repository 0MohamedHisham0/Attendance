package com.dal4.attendance.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "SubjectTable")
public class SubjectsModel implements Serializable {


    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "SubName")
    public String SubName;

    @ColumnInfo(name = "SubGroup")
    public String SubGroup;

    @ColumnInfo(name = "SubStudentNum")
    public String SubStudentNum;

    @ColumnInfo(name = "CurrentStudent")
    public String CurrentStudent;


    public SubjectsModel(String subName, String subGroup, String subStudentNum, String currentStudent) {
        SubName = subName;
        SubGroup = subGroup;
        SubStudentNum = subStudentNum;
        CurrentStudent = currentStudent;
    }

    public SubjectsModel() {
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getSubGroup() {
        return SubGroup;
    }

    public void setSubGroup(String subGroup) {
        SubGroup = subGroup;
    }

    public String getSubStudentNum() {
        return SubStudentNum;
    }

    public void setSubStudentNum(String subStudentNum) {
        SubStudentNum = subStudentNum;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCurrentStudent() {
        return CurrentStudent;
    }

    public void setCurrentStudent(String currentStudent) {
        CurrentStudent = currentStudent;
    }
}
