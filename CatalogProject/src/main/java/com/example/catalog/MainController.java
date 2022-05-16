package com.example.catalog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    protected static Catalog catalog;

    protected static ArrayList typeList, tagList, itemList;

    @FXML
    private TextField textField, name;

    @FXML
    private ChoiceBox<String> searchChoice;
    protected static ArrayList<Tag> tempTagList;
    private final String[] objectList = {"type", "tag", "item"};

    @FXML
    protected TreeView<Object> treeView;
    protected static TreeItem<Object> root, selectedItem;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label typeName, itemTags, typeLabel, tagsLabel, listTitle;

    @FXML
    private Button editButton, deleteFieldButton;

    @FXML
    protected TableView tableView;

    @FXML private CheckComboBox<Tag> tagNames;

    @FXML private StackPane stackPane;

    @FXML
    protected ListView<String> fieldList;

    protected TableColumn<Property, String> labelColumn = new TableColumn<>("Label");
    protected TableColumn<Property, ArrayList<String>> contentColumn = new TableColumn<>("Content");
    private Item item;

    @FXML
    private void searched() {
        if (!textField.getText().isBlank()) {
            TreeItem<Object> root = new TreeItem<>();
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
    protected void treeView() {
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
    @FXML private void filterByTags(){
        ObservableList<Tag> l = tagNames.getCheckModel().getCheckedItems();
        if (l.size()!= 0) {
            TreeItem<Object> root = new TreeItem<>();
            treeView.setRoot(root);
            treeView.setShowRoot(false);


            for (Tag tag : l) {
                for (Item item : tag.getItems()) {
                    boolean isFound = false;
                    if (item.getTags().containsAll(l)) {
                        TreeItem<Object> newItem = new TreeItem<>(item);

                        for (TreeItem t : root.getChildren()) {
                            Item i = (Item) t.getValue();
                            if (item.getName().equals(i.getName())) {
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound)
                            root.getChildren().add(newItem);
                    }
                }
            }
        }
        else
            treeView();
    }

    @FXML
    private void renameButton() {
        if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Item")) {
            Item item = (Item) selectedItem.getValue();
            item.setName(name.getText());
        } else if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Type")) {
            Type type = (Type) selectedItem.getValue();
            type.setType(name.getText());
            typeName.setText(type.getType());
        } else if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Tag")) {
            Tag tag = (Tag) selectedItem.getValue();
            tag.setTag(name.getText());
            typeName.setText(tag.getTag());
        }
        treeView.refresh();
    }

    @FXML
    private void deleteButton() {
        if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Item")) {
            item = (Item) selectedItem.getValue();
            catalog.deleteItem(item);
            itemList.remove(item);
            item.getType().deleteFieldLabels(item.getProperties());

        } else if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Type")) {
            Type type = (Type) selectedItem.getValue();
            catalog.deleteType(type, itemList);
            typeList.remove(type);
        } else if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Tag")) {
            Tag tag = (Tag) selectedItem.getValue();
            catalog.deleteTag(tag);
            tagList.remove(tag);
        }
        anchorPane.setVisible(false);
        treeView();
    }

    @FXML
    private void selected() {
        selectedItem = treeView.getSelectionModel().getSelectedItem();
        clearPage();

        if (selectedItem != null) {
            boolean isFolder = false;
            for (TreeItem treeItem : root.getChildren())
                if (treeItem.equals(selectedItem)) {
                    isFolder = true;
                    break;
                }

            if (!isFolder) {
                name.setText(selectedItem.getValue().toString());
                if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Item")) {
                    Item item = (Item) selectedItem.getValue();

                    typeName.setText(item.getType().toString());
                    itemTags.setText(item.getTags().toString());

                    tableView(item);
                    displayItem();
                } else if (selectedItem.getValue().getClass().getName().equals("com.example.catalog.Type")) {
                    Type type = (Type) selectedItem.getValue();
                    stackPane.getChildren().get(0).setVisible(false);
                    stackPane.getChildren().get(1).setVisible(true);
                    listTitle.setVisible(true);
                    for (String string : type.getFieldLabels())
                        if (!fieldList.getItems().contains(string))
                            fieldList.getItems().add(string);
                    if (fieldList.getItems().size() != 0) {
                        fieldList.setVisible(true);
                        deleteFieldButton.setVisible(true);
                    }
                }
                anchorPane.setVisible(true);
            }
        }
    }


    @FXML
    private void deleteFieldLabel() {
        String s = fieldList.getSelectionModel().getSelectedItem();
        Type type = (Type) selectedItem.getValue();
        fieldList.getItems().remove(s);
        type.deleteFieldLabel(s);
        for (Item item : type.getItems()) {
            if (item.getProperties().size() != 0)
                for (Property property : item.getProperties())
                    if (property.getLabel().equals(s))
                        item.deleteProperty(property);
        }
    }

    private void tableView(Item item) {
        for (Property property : item.getProperties())
            tableView.getItems().add(property);
    }

    public void clearPage() {
        tableView.getItems().clear();
        fieldList.getItems().clear();
        listTitle.setVisible(false);
        name.setText(null);
        typeName.setText(null);
        itemTags.setText(null);
        anchorPane.setVisible(false);
        tagsLabel.setVisible(false);
        typeLabel.setVisible(false);
        typeName.setVisible(false);
        itemTags.setVisible(false);
        editButton.setVisible(false);
        deleteFieldButton.setVisible(false);

    }

    public void displayItem() {
        tagsLabel.setVisible(true);
        typeLabel.setVisible(true);
        typeName.setVisible(true);
        itemTags.setVisible(true);
        editButton.setVisible(true);
        stackPane.getChildren().get(0).setVisible(true);
        stackPane.getChildren().get(1).setVisible(false);
    }
    public void addTags(Tag t){
        tagList.add(t);

        catalog.addTag(t);
    }

    @FXML
    private void initialize() {
        catalog = new Catalog();
        catalog.readFromFile();
        anchorPane.setVisible(false);

        itemList = new ArrayList<>(catalog.getItems().inOrder());
        typeList = new ArrayList<>(catalog.getTypes().inOrder());
        tagList = new ArrayList<>(catalog.getTags().inOrder());

        tempTagList = new ArrayList<>();

        searchChoice.setValue("Search");
        searchChoice.getItems().addAll(objectList);
        searchChoice.setOnAction(this::searchAction);


        tagNames.getItems().addAll(tagList);
        tagNames.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Tag>() {
            @Override
            public void onChanged(Change<? extends Tag> change) {
                filterByTags();
            }
        });

        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, ArrayList<String>>("content"));
        labelColumn.setStyle("-fx-alignment: CENTER");
        contentColumn.setStyle("-fx-alignment: CENTER");
        tableView.getColumns().add(labelColumn);
        tableView.getColumns().add(contentColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        treeView();
    }

    private void searchAction(ActionEvent event) {
        searched();
    }

    @FXML
    private void edit(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditScreen.fxml"));
        Parent root = fxmlLoader.load();
        EditController editControllerScene = fxmlLoader.getController();
        editScreen(editControllerScene);

        Scene scene = new Scene(root, 740, 450);
        stage.setTitle("Edit");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                treeView();
                anchorPane.setVisible(false);
                tagNames.getItems().addAll(tempTagList);
                tempTagList.clear();
            }
        });

    }

    public void editScreen(EditController editControllerScene) {
        editControllerScene.tableView(tableView);
        editControllerScene.listView();
        editControllerScene.choiceBoxes(typeList, tagList);
        editControllerScene.fieldListView((Item) selectedItem.getValue());
    }

    @FXML
    private void create(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateScreen.fxml"));
        Parent root = fxmlLoader.load();
        CreateController createControllerScene = fxmlLoader.getController();
        createScreen(createControllerScene);

        Scene scene = new Scene(root, 740, 450);
        stage.setTitle("Create");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                treeView();
                anchorPane.setVisible(false);
                tagNames.getItems().addAll(tempTagList);
                tempTagList.clear();
            }
        });
    }

    public void createScreen(CreateController createControllerScene) {
        createControllerScene.choiceBoxes(typeList, tagList);
        createControllerScene.tableView();
    }

    @FXML
    private void helpMenu() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setResizable(false);
        a.setTitle("About Catalog Program");
        a.setHeaderText("Details will be here soon...");
        a.setContentText("Details will be here soon...");
        a.showAndWait();
    }

    public void alertErrorWindow(String title, String contentText) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setResizable(false);
        a.setTitle(title);
        a.setContentText(contentText);
        a.showAndWait();
    }

    public void alertSuccessWindow(String title, String contentText) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setResizable(false);
        a.setTitle(title);
        a.setContentText(contentText);
        a.showAndWait();
    }
}