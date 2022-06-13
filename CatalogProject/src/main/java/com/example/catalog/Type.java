package com.example.catalog;

import java.io.Serializable;
import java.util.ArrayList;

public class Type implements Serializable {
    private String name; // type's name
    private final ArrayList<Item> items;
    private final ArrayList<String> fieldLabels;

    public String toString() {
        return name;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void deleteItem(Item item) {
        items.remove(item);
    }

    public void addFieldLabel(String s) {
        if (!fieldLabels.contains(s))
            fieldLabels.add(s);
    }
    public void deleteAllFieldLabels() {
        int counter = 0;
        ArrayList<String> tempFieldLabels = new ArrayList<>(fieldLabels);
        for (String string: tempFieldLabels) {
            for(Item item: items){
                for (Property property: item.getProperties()){
                    if (property.getLabel().equals(string))
                        counter++;
                }
            }
            if (counter == 1)
                fieldLabels.remove(string);
        }
    }
    public void removeFieldLabel(String s){
        for (Item item: items){
            ArrayList<Property> tempProperties = new ArrayList<>(item.getProperties());
            for (Property property: tempProperties)
                if (property.getLabel().equals(s))
                    item.deleteProperty(property);
        }
        fieldLabels.remove(s);
    }
    public void deleteFieldLabel(String s) {
        int counter = 0;
        for(Item item: items){
            for (Property property: item.getProperties()){
                if (property.getLabel().equals(s))
                    counter++;
            }
        }
        if (counter == 1) {
            fieldLabels.remove(s);
            for (Item item : items) {
                ArrayList<Property> tempList = new ArrayList<>(item.getProperties());
                for (Property property : tempList) {
                    if (property.getLabel().equals(s))
                        item.deleteProperty(property);
                }
            }

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<String> getFieldLabels() {
        return fieldLabels;
    }

    public Type(String name) {
        this.name = name;
        items = new ArrayList<>();
        fieldLabels = new ArrayList<>();
    }
}
