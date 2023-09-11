package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
public class HistoryModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String current_date;
    private String saving_time;
    private String counter_value;
    private Double peak_value;
    private Double average_value;

    public HistoryModel(int id,String current_date, String saving_time, String counter_value, Double peak_value, Double average_value) {
        this.id=id;
        this.current_date = current_date;
        this.saving_time = saving_time;
        this.counter_value = counter_value;
        this.peak_value = peak_value;
        this.average_value = average_value;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getSaving_time() {
        return saving_time;
    }

    public void setSaving_time(String saving_time) {
        this.saving_time = saving_time;
    }

    public String getCounter_value() {
        return counter_value;
    }

    public void setCounter_value(String counter_value) {
        this.counter_value = counter_value;
    }

    public Double getPeak_value() {
        return peak_value;
    }

    public void setPeak_value(Double peak_value) {
        this.peak_value = peak_value;
    }

    public Double getAverage_value() {
        return average_value;
    }

    public void setAverage_value(Double average_value) {
        this.average_value = average_value;
    }

    //    Timestamp timestamp;


}
