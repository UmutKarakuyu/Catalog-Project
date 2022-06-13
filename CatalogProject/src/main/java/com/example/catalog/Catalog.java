package com.example.catalog;

import java.io.*;
import java.util.ArrayList;

public class Catalog {
    private static final BST types = new BST();
    private static final BST tags = new BST();
    private static final BST items = new BST();

    protected static ArrayList<Type> typeList;
    protected static ArrayList<Tag> tagList;
    protected static ArrayList<Item> itemList;

    private static Catalog catalog = null;

    public static Catalog getCatalog() {
        if (catalog == null)
            catalog = new Catalog();
        return catalog;
    }

    private Catalog() {
        readFromFile();
        itemList = new ArrayList(items.inOrder());
        typeList = new ArrayList(types.inOrder());
        tagList = new ArrayList(tags.inOrder());
    }

    public void createItem(Item item) {
        items.insert(item);
        itemList.add(item);

        if (item.getType() != null) {
            createType(item.getType());
            item.getType().addItem(item);
        }

        for (Tag tag : item.getTags())
            createTag(tag);
    }

    public void deleteItem(Item item) {
        item.getType().deleteItem(item);
        for (Tag tag : item.getTags())
            tag.deleteItem(item);
        items.remove(item);
        itemList.remove(item);
    }

    public void createType(Type type) {
        if (!typeList.contains(type)) {
            types.insert(type);
            typeList.add(type);
        }
    }

    public void deleteType(Type type) {
        type.deleteAllFieldLabels();
        ArrayList<Item> tempItems = new ArrayList<>(type.getItems());
        for (Item item : tempItems)
            deleteItem(item);
        types.remove(type);
        typeList.remove(type);
    }

    public void createTag(Tag tag) {
        if (!tagList.toString().contains(tag.getName())) {
            tags.insert(tag);
            tagList.add(tag);
        }
    }

    public void deleteTag(Tag tag) {
        for (Item item : tag.getItems())
            item.deleteTag(tag);
        tags.remove(tag);
        tagList.remove(tag);
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

    public void writeToFile() {
        writeToFile(types.inOrder());
    }

    private void readFromFile() {
        File file = new File("CatalogProject/src/main/resources/files/types.txt");
        if (file.exists()) {
            ObjectInputStream ois;
            FileInputStream fis;
            try {
                fis = new FileInputStream("CatalogProject/src/main/resources/files/types.txt");
                ois = new ObjectInputStream(fis);
                try {
                    while (true) {
                        Type type = (Type) ois.readObject();
                        Catalog.types.insert(type);
                        for (Item item : type.getItems()) {
                            Catalog.items.insert(item);
                            for (Tag tag : item.getTags())
                                Catalog.tags.insert(tag);
                        }
                    }
                } catch (EOFException | ClassNotFoundException ignored) {}
                ois.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }// file not found

        }
    }

    private void writeToFile(ArrayList<Object> arrayList) {
        File file = new File("CatalogProject/src/main/resources/files/types.txt");
        if (file.exists()) {
            ObjectOutputStream oos;
            try {
                FileOutputStream fos = new FileOutputStream("CatalogProject/src/main/resources/files/types.txt");
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
}
