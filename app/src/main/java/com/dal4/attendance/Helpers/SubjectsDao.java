package com.dal4.attendance.Helpers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dal4.attendance.Models.SubjectsModel;

import java.util.List;

@Dao
public interface SubjectsDao {

    @Query("SELECT * FROM SubjectTable ")
    List<SubjectsModel> GetAll_Subjects();

    @Insert
    void CreateNew_Subject(SubjectsModel model);

    @Delete
    void DeleteItem_Subject(SubjectsModel model);

    @Update
    void Update_Subject(SubjectsModel model);

}
