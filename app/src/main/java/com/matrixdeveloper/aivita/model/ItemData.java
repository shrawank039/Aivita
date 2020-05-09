package com.matrixdeveloper.aivita.model;

public class ItemData {

    private int itemId;
    private String name;
    private String description;
    private String cover;

    public ItemData() {
    }

    public ItemData(int itemId, String name, String description, String cover) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "ItemData{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
