package com.example.catalog;

import java.util.ArrayList;

public class Item {

    /* It was created as a prototype.
     The code blocks will be filled in soon.

     */

    private Type type;
    private String name;
    private ArrayList<Tag> tags;
    private ArrayList<Property> properties;

    public Item(Type type, ArrayList<Tag> tags, ArrayList<Property> properties) {
        this.type = type;
        this.tags = tags;
        this.properties = properties;
    }
    public Item(Type type, String name){
        this.type = type;
        this.name = name;
        tags = new ArrayList<>();
        properties = new ArrayList<>();
        
    }

    public void createProperty(){

    }

    public void createTag(){

    }

    public void editProperty(){

    }

    public void editTag(){

    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }



    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                ", tags=" + tags +
                ", properties=" + properties +
                '}';
    }
}
