package com.dal4.attendance.Network.Local;

import androidx.room.RoomDatabase;

import com.dal4.attendance.Helpers.StudentsDao;
import com.dal4.attendance.Helpers.SubjectsDao;
import com.dal4.attendance.Models.StudentModel;
import com.dal4.attendance.Models.SubjectsModel;


@androidx.room.Database(entities = {StudentModel.class}, version = 2)
public abstract class Database_Students extends RoomDatabase {

    public abstract StudentsDao studentsDao();


}


