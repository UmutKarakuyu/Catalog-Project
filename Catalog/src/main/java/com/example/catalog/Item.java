package com.example.catalog;

import java.util.ArrayList;

public class Item {

    private Type type;
    private String name;
    private ArrayList<Tag> tags;
    private ArrayList<Property> properties;

    public Item(Type type, String name){
        this.type = type;
        this.name = name;
        tags = new ArrayList<>(){
            @Override
            public String toString() {
                return super.toString().replace("[", "").replace("]", "");
            }
        };
        properties = new ArrayList<>();
    }

    public void createProperty(String label, String content){
        Property property = new Property(label, content);
        properties.add(property);
    }
    public void createProperty(Property property){
        properties.add(property);
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }

    public void editProperty(Property property, String label, String content){
        property.setLabel(label);
        property.setContent(content);
    }

    public void editTag(Tag tag, String name){
        tag.setTag(name);
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

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
        return name;
    }
}
