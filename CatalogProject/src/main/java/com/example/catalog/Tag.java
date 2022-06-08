package com.example.catalog;

import java.io.Serializable;
import java.util.ArrayList;

public class Tag implements Serializable {
    private String name;
    private ArrayList<Item> items=new ArrayList<>();

    public String toString(){
        return name;
    }
    public void addItems(Item item){
        items.add(item);
    }

    public void deleteItem(Item item){ items.remove(item);}

    public Tag(String tagName) {
        name = tagName;
    }

    public String getName() {
        return name;
    }

    public void setName(String tag) {
        this.name =tag;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
