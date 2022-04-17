package com.example.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Catalog extends Application {

    private BST types = new BST();
    private BST tags=new BST();
    private BST items=new BST();

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
        types.insert(type);
    }
    public void createItem(Type type,String itemName){
        Item item=new Item(type,itemName);
        items.insert(item);
    }
    public void editType(Type type, String name, ArrayList<Item> arrayList){
        type.setType(name);
        type.setItems(arrayList);
    }
    public void editTag(){

    }
    public void searchType(String type){
        types.find(type);
    }
    public void searchItem(String item){
        items.find(item);
    }
    public void searchTag(String tag){
        tags.find(tag);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Catalog.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}