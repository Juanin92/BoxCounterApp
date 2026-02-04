package com.example.boxcounter.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "turns")
public class Turn {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private int quantity;
    private long timestamp;

    public Turn() {
    }

    public Turn(Long id, int quantity, long timestamp) {
        this.id = id;
        this.quantity = quantity;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
