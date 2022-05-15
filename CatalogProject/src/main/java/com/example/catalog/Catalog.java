package com.example.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Catalog extends Application {

    private static BST types = new BST();
    private static BST tags = new BST();
    private static BST items = new BST();


    public BST getTypes() {
        return types;
    }

    public void setTypes(BST types) {
        Catalog.types = types;
    }

    public BST getTags() {
        return tags;
    }

    public void setTags(BST tags) {
        Catalog.tags = tags;
    }

    public BST getItems() {
        return items;
    }

    public void setItems(BST items) {
        Catalog.items = items;
    }

    public void createType(String typeName) {
        Type type = new Type(typeName);
        addType(type);
    }

    public void createTag(String tagName) {
        Tag tag = new Tag(tagName);
        addTag(tag);
    }

    public void createItem(Type type, String itemName) {
        Item item = new Item(type, itemName);
        addItem(item);
    }

    public void editType(Type type, String name) {
        type.setType(name);
    }

    public void editTag(Tag tag, String name) {
        tag.setTag(name);
    }

    public void deleteItem(Item item) {
        for (Tag tag : item.getTags())
            tag.getItems().remove(item);
        item.getType().getItems().remove(item);
        item.getType().deleteFieldLabels(item.getProperties());
        items.remove(item);
    }

    public void deleteTag(Tag tag) {
        for (Item item : tag.getItems())
            item.getTags().remove(tag);
        tags.remove(tag);
    }

    public void deleteType(Type type, ArrayList itemsForType) {
        for (Item item : type.getItems()) {
            for (Tag tag : item.getTags())
                tag.getItems().remove(item);
            items.remove(item);
            itemsForType.remove(item);
        }
        types.remove(type);
    }

    public ArrayList searchType(String type) {
        return types.find(type);
    }

    public ArrayList searchItem(String item) {
        return items.find(item);
    }

    public ArrayList searchTag(String tag) {
        return tags.find(tag);
    }

    public void addType(Type type) {
        types.insert(type);
    }

    public void addItem(Item item) {
        items.insert(item);
        item.getType().getItems().add(item);

        for (Tag tag : item.getTags())
            if (!tag.getItems().contains(item))
                tag.getItems().add(item);
    }

    public void addTag(Tag tag) {
        tags.insert(tag);
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Catalog.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Catalog");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}