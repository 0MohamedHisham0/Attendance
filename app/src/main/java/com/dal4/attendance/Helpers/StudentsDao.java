package com.dal4.attendance.Helpers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dal4.attendance.Models.StudentModel;
import com.dal4.attendance.Models.SubjectsModel;

import java.util.List;

@Dao
public interface StudentsDao {

    @Query("SELECT * FROM StudentsTable ")
    List<StudentModel> GetAllStudents();

    @Insert
    void CreateNew_Student(StudentModel model);

    @Delete
    void DeleteItem_Student(StudentModel model);

    @Update
    void Update_Student(StudentModel model);
}
