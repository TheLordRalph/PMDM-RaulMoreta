package com.juanagui.room.db;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity(tableName = "student")
public class Student {
    @PrimaryKey(autoGenerate = true)
    public int sid;
    @ColumnInfo
    public String name;
    @ColumnInfo(name = "first_surname")
    public String firstSurname;
    @ColumnInfo(name = "second_surname")
    public String secondSurname;
}