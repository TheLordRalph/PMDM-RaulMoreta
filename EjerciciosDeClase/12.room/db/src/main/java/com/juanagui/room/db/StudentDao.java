package com.juanagui.room.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface StudentDao {
    @Query("SELECT * FROM student")
    Single<List<Student>> getAll();

    @Query("SELECT * FROM student WHERE sid = :id")
    Single<Student> find(int id);

    @Insert
    Completable insertStudent(Student student);

    @Update
    Completable updateStudent(Student student);

    @Delete
    Completable deleteStudent(Student student);

}