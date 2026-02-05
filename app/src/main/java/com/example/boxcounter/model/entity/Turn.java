package com.example.boxcounter.model.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "turns")
public class Turn {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private int quantity;
    private Long startTime;
    private Long endTime;
    private boolean active;

    public Turn() {
    }

    @Ignore
    public Turn(int quantity, Long startTime, Long endTime, boolean active) {
        this.quantity = quantity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
