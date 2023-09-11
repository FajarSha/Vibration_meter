package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryModel;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insert(HistoryModel model);

    @Update
    void update(HistoryModel model);

    @Query("Delete from HistoryModel where id=:historyid")
    void delete(int historyid);

    @Query("Select * from HistoryModel")
    List<HistoryModel> getAllHistory();
    @Query("delete from HistoryModel")
    void deleteAll();
    @Insert
    void insert(SavedModel model);

    @Update
    void update(SavedModel model);

    @Query("Delete from SavedModel where id=:savedid")
    void deleteSaveditem(int savedid);

    @Query("Select * from SavedModel")
    List<SavedModel> getAllSaved();
    @Query("delete from SavedModel")
    void deleteAllSaved();

}
