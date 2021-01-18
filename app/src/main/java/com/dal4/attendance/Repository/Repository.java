package com.dal4.attendance.Repository;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.dal4.attendance.Models.SubjectsModel;
import com.dal4.attendance.Network.Local.Database_Students;
import com.dal4.attendance.Network.Local.Database_Subjects;
import com.dal4.attendance.UI.Subjects_Screen;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    public static Database_Subjects database_subjects;
    public static Database_Students database_students;
    public List<SubjectsModel> List = new ArrayList<>();


    public static Database_Subjects InitSubjectsTable(Context context) {

        if (database_subjects == null) {
            database_subjects = Room.databaseBuilder(context, Database_Subjects.class, "Subjects").build();
        }

        return database_subjects;
    }

    public static Database_Students InitStudentsTable(Context context) {

        if (database_students == null) {

            database_students = Room.databaseBuilder(context, Database_Students.class, "Students").build();
        }

        return database_students;
    }



}
