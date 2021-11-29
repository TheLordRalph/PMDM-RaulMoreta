package com.juanagui.room;

import android.app.Application;

import com.juanagui.room.db.AppDatabase;

public class RoomApplication extends Application {

    public AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getInstance(this);
    }
}