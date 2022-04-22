package com.example.catalog;
import java.util.ArrayList;
import java.util.List;

public class Tag {
    private String tag;
    private ArrayList<Item> items = new ArrayList<Item>();

    public String toString(){
        return tag;
    }
    public void addItems(Item item){
        items.add(item);
    }

    public Tag(String tagName) {
        tag = tagName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag =tag;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
