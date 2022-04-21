package com.example.catalog;
import java.util.ArrayList;
import java.util.List;

public class Type {
    private String type; // type's name
    private ArrayList<Item> items = new ArrayList<Item>();


    public String toString(){
        return type;
    }
    public void addItems(Item item){
        items.add(item);
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Type(String type) {
        this.type = type;
    }
}
