package com.example.catalog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateController extends MainController implements Initializable {
    @FXML
    private Item item;
    @FXML
    private ChoiceBox typesBox, tagsBox;
    @FXML
    private TextField itemName, typeNameField, propertyLabel, propertyContent, tagNameField;

    @FXML
    private Button create;
    @FXML
    private TableView propertyTable;
    @FXML
    private ListView tagListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        item = new Item(null, null);
    }

    @FXML
    public void createItem() {
        if (!itemName.getText().isBlank()) {
            item.setName(itemName.getText());
            if (item.getType() != null) {
                MainController.itemList.add(item);
                catalog.addItem(item);
                item.getType().getItems().add(item);
                if (item.getTags() != null)
                    for (int i = 0; i < item.getTags().size(); i++)
                        item.getTags().get(i).getItems().add(item);
                alertSuccessWindow("Item Created!!", "Item is successfully created");
            } else
                alertErrorWindow("Type is null", "Please press the 'Add Item Type' button to add a type");
        } else
            alertErrorWindow("Enter a name", "Please enter an item name");
    }

    @FXML
    private void createType() {
        boolean isRepeated = false;
        if (!typesBox.getValue().equals("Types")) {
            Type type = (Type) MainController.typeList.get(MainController.typeList.indexOf(typesBox.getValue()));
            item.setType(type);
        } else if (!typeNameField.getText().isBlank()) {
            for (int i = 0; i < MainController.typeList.size(); i++)
                if (MainController.typeList.get(i).toString().equals(typeNameField.getText())) {
                    isRepeated = true;
                    alertErrorWindow("Type Exists", "This type already exists. Please choose it from choice box!");
                }
            if (!isRepeated) {
                Type type = new Type(typeNameField.getText());
                item.setType(type);
                typesBox.getItems().add(type);
                MainController.typeList.add(type);
                catalog.addType(type);
            }
            typeNameField.clear();
        } else
            alertErrorWindow("Error", "You must enter a new Type name and press the 'Add Item Type' button or choose it from the choice box!");
    }

    @FXML
    private void addProperty() {
        boolean isRepeated = false;
        if (!propertyContent.getText().isBlank() && !propertyLabel.getText().isBlank()) {
            Property property = new Property(propertyLabel.getText(), propertyContent.getText());
            for (int i = 0; i < item.getProperties().size(); i++) {
                if (property.toString().equals(item.getProperties().get(i).toString())) {
                    isRepeated = true;
                    alertErrorWindow("This property exists", "You have already added this property!!");
                    propertyContent.clear();
                    propertyLabel.clear();
                }
            }
            if (!isRepeated) {
                item.createProperty(property);
                propertyTable.getItems().add(property);
                propertyContent.clear();
                propertyLabel.clear();
            }
        } else
            alertErrorWindow("Nothing entered!", "You must enter label and content(s).");
    }

    @FXML
    private void addTag() {
        boolean isRepeated = false;
        if (!tagNameField.getText().isBlank()) {
            for (int i = 0; i < MainController.tagList.size(); i++) {
                if (MainController.tagList.get(i).toString().equals(tagNameField.getText())) {
                    isRepeated = true;
                    alertErrorWindow("Tag Exists", "This tag already exists in choice box. Please choose different tag or create new tag!");
                }
            }
            if (!isRepeated) {
                Tag tag = new Tag(tagNameField.getText());
                item.addTag(tag);
                tagListView.getItems().add(tag);
                MainController.tagList.add(tag);
                catalog.addTag(tag);
                tagsBox.getItems().add(tag);
            }
            tagNameField.clear();
        } else if (!tagsBox.getValue().equals("Tags")) {
            for (int i = 0; i < item.getTags().size(); i++) {
                if (item.getTags().get(i).toString().equals(tagsBox.getValue().toString())) {
                    isRepeated = true;
                    alertErrorWindow("Tag Exists", "This tag already exists. Please choose another tag from choice box or create new tag!");
                }
            }
            if (!isRepeated) {
                tagListView.getItems().add(MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
                item.getTags().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
            }
        }
    }

    @FXML
    private void deleteTag() {
        Tag selectedTag = (Tag) tagListView.getSelectionModel().getSelectedItem();
        if (selectedTag != null) {
            tagListView.getItems().remove(selectedTag);
            item.getTags().remove(selectedTag);
        } else
            alertErrorWindow("Nothing selected!", "You must select a type to delete it.");
    }

    @FXML
    private void deleteProperty() {
        Property selectedProperty = (Property) propertyTable.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            propertyTable.getItems().remove(selectedProperty);
            item.getProperties().remove(item.getProperties().indexOf(selectedProperty));
        } else
            alertErrorWindow("Nothing selected!", "You must select a property to delete it.");
    }

    public void tableView() {
        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, ArrayList<String>>("content"));
        propertyTable.getColumns().add(labelColumn);
        propertyTable.getColumns().add(contentColumn);
        propertyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void choiceBoxes(ArrayList typeList, ArrayList tagList) {
        typesBox.setValue("Types");
        typesBox.getItems().addAll(typeList);

        tagsBox.setValue("Tags");
        tagsBox.getItems().addAll(tagList);
    }
}