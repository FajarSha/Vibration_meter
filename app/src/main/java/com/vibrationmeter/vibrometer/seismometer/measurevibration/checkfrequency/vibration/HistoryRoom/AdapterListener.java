package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryRoom;

import com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.HistoryModel;

public interface AdapterListener {
    void OnUpdate(int id,int pos);
    void OnDelete(int id,int pos);
    void OnitemDelete(HistoryModel model);
    void OnitemSahre(HistoryModel model);
    void Savemodelitem(SavedModel model);
    void OndeleteSaveditem(SavedModel model,int position);
}
