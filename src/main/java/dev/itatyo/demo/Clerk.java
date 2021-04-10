package dev.itatyo.demo;

import java.sql.Timestamp;

public class Clerk {
    private int id;
    private String name;
    private Timestamp created_timestamp;
    private Timestamp updated_timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created_datetime=" + created_timestamp +
                ", updated_datetime=" + updated_timestamp +
                '}';
    }
}
