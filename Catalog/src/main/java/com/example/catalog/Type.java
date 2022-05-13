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
            if (!fieldLabels.contains(property.toString()))
                fieldLabels.add(property.getLabel());
    }

    public void addFieldLabel(String s) {
        fieldLabels.add(s);
    }

    public void deleteFieldLabels(ArrayList<Property> s) {
        for (Property property : s)
            fieldLabels.remove(property.getLabel());
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
