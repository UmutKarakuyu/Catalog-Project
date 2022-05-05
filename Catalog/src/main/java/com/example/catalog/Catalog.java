package com.example.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class Catalog extends Application {

    private BST types = new BST();
    private BST tags = new BST();
    private BST items = new BST();

    public BST getTypes() {
        return types;
    }

    public void setTypes(BST types) {
        this.types = types;
    }

    public BST getTags() {
        return tags;
    }

    public void setTags(BST tags) {
        this.tags = tags;
    }

    public BST getItems() {
        return items;
    }

    public void setItems(BST items) {
        this.items = items;
    }

    public void createType(String typeName){
        Type type = new Type(typeName);
        addType(type);
    }
    public void createTag(String tagName){
        Tag tag = new Tag(tagName);
        addTag(tag);
    }
    public void createItem(Type type,String itemName){
        Item item = new Item(type,itemName);
        addItem(item);
    }
    public void editType(Type type, String name){
        type.setType(name);
    }
    public void editTag(Tag tag, String name){
        tag.setTag(name);
    }
    public void deleteItem(Item item){
        for (Tag tag : item.getTags())
            tag.getItems().remove(item);
        item.getType().getItems().remove(item);
        items.remove(item);
    }
    public void deleteTag(Tag tag){
        for (Item item : tag.getItems())
            item.getTags().remove(tag);
        tags.remove(tag);
    }
    public void deleteType(Type type, ArrayList itemsForType){
        types.remove(type);
        for (Item item : type.getItems()) {
            for (Tag tag : item.getTags())
                tag.getItems().remove(item);
            items.remove(item);
            itemsForType.remove(item);
        }
    }
    public ArrayList searchType(String type){
        return types.find(type);
    }
    public ArrayList searchItem(String item){
        return items.find(item);
    }
    public ArrayList searchTag(String tag){
        return tags.find(tag);
    }

    public void addType(Type type){
        types.insert(type);
    }
    public void addItem(Item item){
        items.insert(item);
    }
    public void addTag(Tag tag){
        tags.insert(tag);
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Catalog.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Catalog");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }


}