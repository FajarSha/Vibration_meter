package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryModel;

@Database(entities = {HistoryModel.class,SavedModel.class},version=9)
public abstract class HistoryDatabase extends RoomDatabase {
    public abstract HistoryDao getDao();
    public static  HistoryDatabase INSTANCE;
    public  static HistoryDatabase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context,HistoryDatabase.class,"HistoryDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
