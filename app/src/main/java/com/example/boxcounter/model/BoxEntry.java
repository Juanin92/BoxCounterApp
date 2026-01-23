package com.example.boxcounter.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "boxes")
public class BoxEntry {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private int quantity;
    private long timestamp;

    public BoxEntry(Long id, long timestamp, int quantity) {
        this.id = id;
        this.timestamp = timestamp;
        this.quantity = quantity;
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
