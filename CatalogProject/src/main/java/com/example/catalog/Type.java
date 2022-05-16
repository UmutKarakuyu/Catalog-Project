package com.example.catalog;

import java.io.Serializable;
import java.util.ArrayList;

public class Type implements Serializable {
    private String type; // type's name
    private ArrayList<Item> items;
    private ArrayList<String> fieldLabels;

    public String toString() {
        return type;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void deleteItem(Item item) {
        items.remove(item);
    }

    public void addFieldLabels(ArrayList<Property> s) {
        for (Property property : s)
            if (!fieldLabels.contains(property.getLabel()))
                fieldLabels.add(property.getLabel());
    }

    public void addFieldLabel(String s) {
        if (!fieldLabels.contains(s))
            fieldLabels.add(s);
    }
    public void deleteAllFieldLabel() {
        fieldLabels.clear();
    }

    public void deleteFieldLabels(ArrayList<Property> s) {
        int counter = 0;

        for (Property property : s) {
            for(Item item: items){
                for (Property property1: item.getProperties()){
                    if (property1.getLabel().equals(property.getLabel()))
                        counter++;
                }
            }
            if (counter==1)
                fieldLabels.remove(property.getLabel());
        }
    }

    public void deleteFieldLabel(String s) {
        fieldLabels.remove(s);
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

    public ArrayList<String> getFieldLabels() {
        return fieldLabels;
    }

    public void setFieldLabels(ArrayList<String> fieldLabels) {
        this.fieldLabels = fieldLabels;
    }

    public Type(String type) {
        this.type = type;
        items = new ArrayList<>();
        fieldLabels = new ArrayList<>();
    }


}
