package com.example.catalog;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.net.MalformedURLException;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.scene.web.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController {
    protected static Catalog catalog;

    @FXML
    private TextField searchField;
    @FXML
    private TextField name;

    @FXML
    private ChoiceBox<String> searchChoice;
    private final String[] objectList = {"Type", "Tag", "Item"};

    protected static ArrayList<Tag> tempTagList;

    @FXML
    protected TreeView<Object> treeView;
    protected static TreeItem<Object> root, selectedObject;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label typeName;
    @FXML
    private Label itemTags;
    @FXML
    private Label listTitle;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button deleteFieldButton;

    @FXML
    private TableView<Property> tableView;

    @FXML
    private ListView<String> fieldList;

    @FXML
    private CheckComboBox<Tag> filterTagNames;

    @FXML
    private StackPane stackPane;

    @FXML
    private Button exportButton;

    @FXML
    private Button printButton;

    protected static int tempState;

    @FXML
    private void onSearch() {
        if (!searchField.getText().isBlank()) {
            TreeItem<Object> searchTreeRoot = new TreeItem<>();
            treeView.setRoot(searchTreeRoot);
            treeView.setShowRoot(false);

            ArrayList<Type> foundTypeList = new ArrayList<Type>(catalog.searchType(searchField.getText()));
            ArrayList<Tag> foundTagList = new ArrayList<Tag>(catalog.searchTag(searchField.getText()));
            ArrayList<Item> foundItemList = new ArrayList<Item>(catalog.searchItem(searchField.getText()));

            switch (searchChoice.getValue()) {
                case "Type" -> {
                    if (foundTypeList.size() != 0)
                        typeSearch(foundTypeList, searchTreeRoot, true);
                    else
                        typeSearch(Catalog.typeList, searchTreeRoot, false);
                }
                case "Tag" -> {
                    if (foundTagList.size() != 0)
                        tagSearch(foundTagList, searchTreeRoot, true);
                    else
                        tagSearch(Catalog.tagList, searchTreeRoot, false);
                }
                case "Item" -> {
                    if (foundItemList.size() != 0)
                        itemSearch(foundItemList, searchTreeRoot, true);
                    else
                        itemSearch(Catalog.itemList, searchTreeRoot, false);
                }
                default -> {
                    if (foundTypeList.size() != 0)
                        typeSearch(foundTypeList, searchTreeRoot, true);
                    else
                        typeSearch(Catalog.typeList, searchTreeRoot, false);

                    if (foundTagList.size() != 0)
                        tagSearch(foundTagList, searchTreeRoot, true);
                    else
                        tagSearch(Catalog.tagList, searchTreeRoot, false);

                    if (foundItemList.size() != 0)
                        itemSearch(foundItemList, searchTreeRoot, true);
                    else
                        itemSearch(Catalog.itemList, searchTreeRoot, false);
                }
            }
        } else
            treeView.setRoot(root);
    }

    private void typeSearch(ArrayList<Type> typeList, TreeItem<Object> root, boolean isFound) {
        for (Type type : typeList) {
            if (isFound || type.toString().contains(searchField.getText())) {
                TreeItem<Object> newType = new TreeItem<>(type);
                root.getChildren().add(newType);
                for (Item item : type.getItems()) {
                    TreeItem<Object> newItem = new TreeItem<>(item);
                    newType.getChildren().add(newItem);
                }
            }
        }
    }

    public void tagSearch(ArrayList<Tag> tagList, TreeItem<Object> root, boolean isFound) {
        for (Tag tag : tagList) {
            if (isFound || tag.toString().contains(searchField.getText())) {
                TreeItem<Object> newTag = new TreeItem<>(tag);
                root.getChildren().add(newTag);
                for (Item item : tag.getItems()) {
                    TreeItem<Object> newItem = new TreeItem<>(item);
                    newTag.getChildren().add(newItem);
                }
            }
        }
    }

    public void itemSearch(ArrayList<Item> itemList, TreeItem<Object> root, boolean isFound) {
        for (Item item : itemList) {
            if (isFound || item.toString().contains(searchField.getText())) {
                TreeItem<Object> newItem = new TreeItem<>(item);
                root.getChildren().add(newItem);
            }
        }
    }

    @FXML
    private void treeView() {
        root.getChildren().clear();

        TreeItem<Object> types = new TreeItem<>("Types");
        TreeItem<Object> tags = new TreeItem<>("Tags");
        TreeItem<Object> items = new TreeItem<>("Items");

        root.getChildren().addAll(types, tags, items);

        for (Type type : Catalog.typeList) {
            TreeItem<Object> newType = new TreeItem<>(type);
            types.getChildren().add(newType);
            for (Item item : type.getItems()) {
                TreeItem<Object> newItem = new TreeItem<>(item);
                newType.getChildren().add(newItem);
            }
        }
        for (Item item : Catalog.itemList) {
            TreeItem<Object> newItem = new TreeItem<>(item);
            items.getChildren().add(newItem);
        }
        for (Tag tag : Catalog.tagList) {
            TreeItem<Object> newTag = new TreeItem<>(tag);
            tags.getChildren().add(newTag);
            for (Item item : tag.getItems()) {
                TreeItem<Object> newItem = new TreeItem<>(item);
                newTag.getChildren().add(newItem);
            }
        }
    }

    @FXML
    private void filterByTags() {
        ObservableList<Tag> list = filterTagNames.getCheckModel().getCheckedItems();
        if (list.size() != 0) {
            TreeItem<Object> filterRoot = new TreeItem<>();
            treeView.setRoot(filterRoot);
            treeView.setShowRoot(false);

            for (Tag tag : list) {
                for (Item item : tag.getItems()) {
                    boolean isFound = false;
                    if (item.getTags().containsAll(list)) {
                        TreeItem<Object> newItem = new TreeItem<>(item);

                        for (TreeItem<Object> t : filterRoot.getChildren()) {
                            Item i = (Item) t.getValue();
                            if (item.getName().equals(i.getName())) {
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound)
                            filterRoot.getChildren().add(newItem);
                    }
                }
            }
        } else
            treeView.setRoot(root);
    }

    @FXML
    private void renameButton() {
        if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Item")) {
            Item item = (Item) selectedObject.getValue();
            item.setName(name.getText());
        } else if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Type")) {
            Type type = (Type) selectedObject.getValue();
            type.setName(name.getText());
            typeName.setText(type.getName());
        } else if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Tag")) {
            Tag tag = (Tag) selectedObject.getValue();
            tag.setName(name.getText());
            typeName.setText(tag.getName());
        }
        treeView.refresh();
    }

    @FXML
    private void deleteButton() {
        if( alertYesNoWindow("Delete","Are you sure you want to delete")) {
            if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Type"))
                catalog.deleteType((Type) selectedObject.getValue());
            else if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Tag")) {
                catalog.deleteTag((Tag) selectedObject.getValue());
                filterTagNames.getItems().remove((Tag) selectedObject.getValue());
            } else if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Item"))
                catalog.deleteItem((Item) selectedObject.getValue());
            anchorPane.setVisible(false);
            treeView();
        }
    }

    @FXML
    private void selected() {
        selectedObject = treeView.getSelectionModel().getSelectedItem();
        clearPage();

        if (selectedObject != null) {
            boolean isFolder = false;
            for (TreeItem<Object> treeItem : root.getChildren())
                if (treeItem.equals(selectedObject)) {
                    isFolder = true;
                    break;
                }

            if (!isFolder) {
                name.setText(selectedObject.getValue().toString());
                if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Item")) {
                    Item item = (Item) selectedObject.getValue();

                    typeName.setText(item.getType().toString());
                    itemTags.setText(item.getTags().toString());

                    tableView(item);
                    displayItem();
                } else if (selectedObject.getValue().getClass().getName().equals("com.example.catalog.Type")) {
                    Type type = (Type) selectedObject.getValue();
                    stackPane.getChildren().get(0).setVisible(false);
                    if (type.getFieldLabels().size() != 0) {
                        stackPane.setVisible(true);
                        stackPane.getChildren().get(1).setVisible(true);
                        listTitle.setVisible(true);
                        exportButton.setVisible(false);
                        printButton.setVisible(false);
                    }
                    for (String string : type.getFieldLabels())
                        if (!fieldList.getItems().contains(string))
                            fieldList.getItems().add(string);
                    if (fieldList.getItems().size() != 0) {
                        fieldList.setVisible(true);
                        deleteFieldButton.setVisible(true);
                        exportButton.setVisible(false);
                        printButton.setVisible(false);

                    }
                }
                anchorPane.setVisible(true);
            }
        }
    }

    @FXML
    private void deleteFieldLabel() {
        String s = fieldList.getSelectionModel().getSelectedItem();
        Type type = (Type) selectedObject.getValue();
        type.removeFieldLabel(s);
        fieldList.getItems().remove(s);
    }

    private void tableView(Item item) {
        for (Property property : item.getProperties())
            tableView.getItems().add(property);
    }

    @FXML
    private void exportItem() {
        FileChooser fileChooser = new FileChooser();
        File pathFile = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        Item item = (Item) selectedObject.getValue();

        try {
            File f = !pathFile.getAbsolutePath().contains(".") ? new File(pathFile.toPath() + ".html") : pathFile;
            Files.writeString(f.toPath(), item.exportItem(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void printPDF() {
        Item printItem = (Item) selectedObject.getValue();
        AnchorPane pane  = new AnchorPane();
        WebView webView = new WebView();
        pane.getChildren().add(webView);

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        printItem.printFile();
        WebEngine webEngine = webView.getEngine();
        File f = new File("CatalogProject/src/main/resources/files/template.html");
        if (f.exists()) {
            try {
                webEngine.load(f.toURI().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(stage)) {
            boolean success = job.printPage(pane);
            if (success)
                job.endJob();
        }
    }
    @FXML
    private void initialize() {
        catalog = Catalog.getCatalog();

        tempTagList = new ArrayList<>();

        searchChoice.setValue("Search");
        searchChoice.getItems().addAll(objectList);
        searchChoice.setOnAction(event -> onSearch());
        filterTagNames.getItems().addAll(Catalog.tagList);
        filterTagNames.getCheckModel().getCheckedItems().addListener((ListChangeListener<Tag>) change -> filterByTags());

        tableViewCreator(tableView);

        root = new TreeItem<>();
        treeView.setRoot(root);
        treeView.setShowRoot(false);

        anchorPane.setVisible(false);

        treeView();
    }

    public static void tableViewCreator(TableView<Property> tableView) {
        TableColumn<Property, String> labelColumn = new TableColumn<>("Label");
        TableColumn<Property, ArrayList<String>> contentColumn = new TableColumn<>("Content");

        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        labelColumn.setStyle("-fx-alignment: CENTER");
        contentColumn.setStyle("-fx-alignment: CENTER");

        tableView.getColumns().add(labelColumn);
        tableView.getColumns().add(contentColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    private void editType() throws IOException {
        tempState = 0;
        edit();
    }

    @FXML
    private void editTag() throws IOException {
        tempState = 1;
        edit();
    }

    @FXML
    private void editProperty() throws IOException {
        tempState = 2;
        edit();
    }

    private void edit() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Edit");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            treeView();
            anchorPane.setVisible(false);
            filterTagNames.getItems().addAll(tempTagList);
            tempTagList.clear();
        });
    }

    @FXML
    private void createNewType() throws IOException {
        tempState = 0;
        create();
    }

    @FXML
    private void createNewItem() throws IOException {
        tempState = 1;
        create();
    }
    private void create() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Create");
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> {
            treeView();
            anchorPane.setVisible(false);
            filterTagNames.getItems().addAll(tempTagList);
            tempTagList.clear();
        });
    }

    @FXML
    private void helpMenu() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Help");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
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

    public boolean alertYesNoWindow(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        AtomicBoolean b = new AtomicBoolean(false);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait().ifPresent(choice -> b.set(choice != noButton));
        return b.get();
    }

    private void clearPage() {
        tableView.getItems().clear();
        fieldList.getItems().clear();
        listTitle.setVisible(false);
        name.setText(null);
        typeName.setText(null);
        itemTags.setText(null);
        gridPane.setVisible(false);
        anchorPane.setVisible(false);
        deleteFieldButton.setVisible(false);
        stackPane.setVisible(false);
        exportButton.setVisible(false);
        printButton.setVisible(false);
    }

    public void displayItem() {
        gridPane.setVisible(true);
        stackPane.setVisible(true);
        stackPane.getChildren().get(0).setVisible(true);
        stackPane.getChildren().get(1).setVisible(false);
        exportButton.setVisible(true);
        printButton.setVisible(true);

    }
}