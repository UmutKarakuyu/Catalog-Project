package com.example.catalog;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.ArrayList;

public class Catalog extends Application {

    private static BST types = new BST();
    private static BST tags = new BST();
    private static BST items = new BST();

    private String itemFileName = "items.txt";
    private String typeFileName = "types.txt";
    private String tagFileName = "tags.txt";

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
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                writeToFile(items.inOrder(), itemFileName);
                writeToFile(types.inOrder(),typeFileName);
                writeToFile(tags.inOrder(),tagFileName);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        launch();

    }
    public void readFromFile(){
        readFromFile(items,itemFileName);
        readFromFile(types,typeFileName);
        readFromFile(tags,tagFileName);
    }
    private void readFromFile(BST tree,String fileName) {
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            try {
                while (true)
                    tree.insert(ois.readObject());
            }
            catch (EOFException e) { // eof
            }
            ois.close();
        }
        catch (EOFException e) { // eof
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // file not found
        }
    }
    private void writeToFile(ArrayList<Object> arrayList, String filename) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fos);
            try {
                for (Object item : arrayList)
                    oos.writeObject(item);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}