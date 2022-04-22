package com.example.catalog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    protected Catalog catalog;

    protected static ArrayList typeList, tagList, itemList;

    @FXML
    private TextField textField, name;

    @FXML
    private ChoiceBox<String> searchChoice;
    private final String[] objectList = {"type", "tag", "item"};

    @FXML
    protected TreeView<Object> treeView;
    protected TreeItem<Object> root, selectedItem;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label typeName, itemTags, typeLabel, tagsLabel;

    @FXML
    private Button editButton;

    @FXML
    protected TableView tableView;

    protected TableColumn<Property, String> labelColumn = new TableColumn<>("Label");
    protected TableColumn<Property, String> contentColumn = new TableColumn<>("Content");

    @FXML
    private void searched() {
        if (!textField.getText().isBlank()) {
            TreeItem root = new TreeItem<>();
            treeView.setRoot(root);
            treeView.setShowRoot(false);

            ArrayList<Type> typeList = new ArrayList(catalog.searchType(textField.getText()));
            ArrayList<Tag> tagList = new ArrayList(catalog.searchTag(textField.getText()));
            ArrayList<Item> itemList = new ArrayList(catalog.searchItem(textField.getText()));

            switch (searchChoice.getValue()) {
                case "type" -> {
                    if (typeList.size() != 0)
                        typeTree(typeList, root, true);
                    else
                        typeTree(this.typeList, root, false);
                }
                case "item" -> {
                    if (itemList.size() != 0)
                        itemTree(itemList, root, true);
                    else
                        itemTree(this.itemList, root, false);
                }
                case "tag" -> {
                    if (tagList.size() != 0)
                        tagTree(tagList, root, true);
                    else
                        tagTree(this.tagList, root, false);
                }

                default -> {
                    if (typeList.size() == 0 && tagList.size() == 0 && itemList.size() == 0) {
                        typeTree(this.typeList, root, false);
                        itemTree(this.itemList, root, false);
                        tagTree(this.tagList, root, false);

                    } else {
                        typeTree(typeList, root, true);
                        itemTree(itemList, root, true);
                        tagTree(tagList, root, true);
                    }
                }
            }
        } else
            treeView();
    }
    @FXML
    private void treeView() {
        root = new TreeItem<>();
        treeView.setRoot(root);
        treeView.setShowRoot(false);

        TreeItem types = new TreeItem("Types");
        TreeItem tags = new TreeItem("Tags");
        TreeItem items = new TreeItem("Items");

        root.getChildren().addAll(types, tags, items);
        typeTree(typeList, types, true);
        tagTree(tagList, tags, true);
        itemTree(itemList, items, true);

    }

    public void typeTree(ArrayList<Type> typeList, TreeItem root, boolean isFound) {
        for (Type type : typeList) {
            if (isFound || type.toString().contains(textField.getText())) {
                TreeItem newType = new TreeItem<>(type);
                root.getChildren().add(newType);
                for (Item item : type.getItems()) {
                    TreeItem<Item> newItem = new TreeItem<>(item);
                    newType.getChildren().add(newItem);
                }
            }
        }
    }

    public void itemTree(ArrayList<Item> itemList, TreeItem root, boolean isFound) {
        for (Item item : itemList) {
            if (isFound || item.toString().contains(textField.getText())) {
                TreeItem<Item> newItem = new TreeItem<>(item);
                root.getChildren().add(newItem);
            }
        }
    }

    public void tagTree(ArrayList<Tag> tagList, TreeItem root, boolean isFound) {
        for (Tag tag : tagList) {
            if (isFound || tag.toString().contains(textField.getText())) {
                TreeItem newTag = new TreeItem(tag);
                root.getChildren().add(newTag);
                for (Item item : tag.getItems()) {
                    TreeItem<Item> newItem = new TreeItem<>(item);
                    newTag.getChildren().add(newItem);
                }
            }
        }
    }

    @FXML
    private void renameButton() {
        TreeItem selected = treeView.getSelectionModel().getSelectedItem();
        if (selected == null)
            selected = selectedItem;

        if (selected.getValue().getClass().getName().equals("com.example.catalog.Item")) {
            Item item = (Item) selected.getValue();
            item.setName(name.getText());
        } else if (selected.getValue().getClass().getName().equals("com.example.catalog.Type")) {
            Type type = (Type) selected.getValue();
            type.setType(name.getText());
            typeName.setText(type.getType());
        } else if (selected.getValue().getClass().getName().equals("com.example.catalog.Tag")) {
            Tag tag = (Tag) selected.getValue();
            tag.setTag(name.getText());
            typeName.setText(tag.getTag());
        }
        treeView.refresh();
    }

    @FXML
    private void deleteButton() {
        TreeItem selected = treeView.getSelectionModel().getSelectedItem();
        if (selected == null)
            selected = selectedItem;

        if (selected.getValue().getClass().getName().equals("com.example.catalog.Item")) {
            Item item = (Item) selected.getValue();
            catalog.deleteItem(item);
            itemList.remove(item);

        } else if (selected.getValue().getClass().getName().equals("com.example.catalog.Type")) {
            Type type = (Type) selected.getValue();
            catalog.deleteType(type, itemList);
            typeList.remove(type);
        } else if (selected.getValue().getClass().getName().equals("com.example.catalog.Tag")) {
            Tag tag = (Tag) selected.getValue();
            catalog.deleteTag(tag);
            tagList.remove(tag);
        }
        selected.getParent().getChildren().remove(selected);
        anchorPane.setVisible(false);
        treeView();
    }

    @FXML
    private void selected() {
        selectedItem = treeView.getSelectionModel().getSelectedItem();
        clearPage();

        if (selectedItem != null) {
            boolean isFolder = false;
            for (int i = 0; i < root.getChildren().size(); i++)
                if (root.getChildren().get(i).equals(selectedItem))
                    isFolder = true;

            if (!isFolder) {
                name.setText(selectedItem.getValue().toString());
                if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Item")) {
                    Item item = (Item) selectedItem.getValue();

                    typeName.setText(item.getType().toString());
                    itemTags.setText(item.getTags().toString());

                    tableView(item);
                    displayItem();
                }
                anchorPane.setVisible(true);
            }
        }
    }

    private void tableView(Item item) {
        for (Property property : item.getProperties())
            tableView.getItems().add(property);
    }

    public void clearPage() {
        tableView.getItems().clear();
        name.setText(null);
        typeName.setText(null);
        itemTags.setText(null);
        anchorPane.setVisible(false);
        tagsLabel.setVisible(false);
        typeLabel.setVisible(false);
        typeName.setVisible(false);
        itemTags.setVisible(false);
        tableView.setVisible(false);
        editButton.setVisible(false);
    }

    public void displayItem() {
        tableView.setVisible(true);
        tagsLabel.setVisible(true);
        typeLabel.setVisible(true);
        typeName.setVisible(true);
        itemTags.setVisible(true);
        editButton.setVisible(true);
    }

    @FXML
    private void initialize() {
        this.catalog = new Catalog();
        anchorPane.setVisible(false);

        çerçöp();

        itemList = new ArrayList(catalog.getItems().inOrder());
        typeList = new ArrayList(catalog.getTypes().inOrder());
        tagList = new ArrayList(catalog.getTags().inOrder());

        searchChoice.setValue("Search");
        searchChoice.getItems().addAll(objectList);
        searchChoice.setOnAction(this::searchAction);

        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("content"));
        tableView.getColumns().add(labelColumn);
        tableView.getColumns().add(contentColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        treeView();
    }

    private void searchAction(ActionEvent event) {
        searched();
    }

    @FXML
    public void edit(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Edit.fxml"));
        Parent root = fxmlLoader.load();

        editScreen(fxmlLoader,(Item) selectedItem.getValue());

        Scene scene = new Scene(root, 700, 450);
        stage.setTitle("Edit");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                treeView();
            }
        });
    }
    public void editScreen(FXMLLoader f, Item item){
        Edit editScene = f.getController();
        editScene.tableView(tableView);
        editScene.listView(item);
        editScene.choiceBoxes(typeList, tagList);
    }
    @FXML
    private void create(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Create.fxml"));
            Parent root = fxmlLoader.load();
            createScreen(fxmlLoader);
            Scene scene = new Scene(root, 740, 450);
            stage.setTitle("Create");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    treeView();
                }
            });
    }
    public void createScreen(FXMLLoader f){
        Create createScene = f.getController();
        createScene.selectedItem=this.selectedItem;
        createScene.choiceBoxes(typeList, tagList);
        createScene.tableView(tableView);
    }

    private void çerçöp() {
        Type a = new Type("Book");
        catalog.getTypes().insert(a);

        Item i1 = new Item(a, "item 1");
        i1.getProperties().add(new Property("Book Name", "Lord Of The Rings"));
        i1.getProperties().add(new Property("Book Type", "fantastic"));

        catalog.getItems().insert(i1);
        a.addItems(i1);

        Item i2 = new Item(a, "item 2");
        catalog.getItems().insert(i2);
        a.addItems(i2);


        Type b = new Type("type 2");
        catalog.getTypes().insert(b);
        Item i3 = new Item(b, "item 3");
        b.addItems(i3);
        catalog.getItems().insert(i3);
        Item i4 = new Item(b, "item 4");
        b.addItems(i4);
        catalog.getItems().insert(i4);

        Tag t1 = new Tag("tag1");
        catalog.getTags().insert(t1);
        t1.getItems().add(i1);
        t1.getItems().add(i2);
        i2.getTags().add(t1);
        i1.getTags().add(t1);
        Tag t2 = new Tag("tag2");
        catalog.getTags().insert(t2);
        t2.getItems().add(i3);
        i3.getTags().add(t2);
        t2.getItems().add(i4);
        i4.getTags().add(t2);
        t2.getItems().add(i2);
        i1.getTags().add(t2);
        i2.getTags().add(t2);
        t2.getItems().add(i1);
    }
}