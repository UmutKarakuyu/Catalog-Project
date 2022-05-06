package com.example.catalog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditController extends MainController implements Initializable {
    @FXML
    private ChoiceBox typesBox, tagsBox;
    @FXML
    private TextField typeNameField, propertyLabel, propertyContent, tagNameField;

    private Item clickedItem;

    @FXML
    private TableView propertyTable;

    @FXML
    private ListView tagListView;
    @FXML
    private Button addTag, deleteTag, addProperty, deleteProperty, changeType, close;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickedItem = (Item) MainController.selectedItem.getValue();
    }

    @FXML
    private void addProperty() {
        boolean isRepeated = false;
        if (!propertyContent.getText().isBlank() && !propertyLabel.getText().isBlank()) {
            Property property = new Property(propertyLabel.getText(), propertyContent.getText());
            for (int i = 0; i < clickedItem.getProperties().size(); i++) {
                if (property.toString().equals(clickedItem.getProperties().get(i).toString())) {
                    isRepeated = true;
                    alertErrorWindow("This property exists", "You have already added this property!!");
                    propertyContent.clear();
                    propertyLabel.clear();
                }
            }
            if (!isRepeated) {
                clickedItem.createProperty(property);
                propertyTable.getItems().add(property);
                propertyContent.clear();
                propertyLabel.clear();
            }
        } else
            alertErrorWindow("Nothing entered!", "You must enter label and content(s).");
    }

    @FXML
    private void deleteProperty() {
        Property selectedProperty = (Property) propertyTable.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            propertyTable.getItems().remove(selectedProperty);
            clickedItem.getProperties().remove(clickedItem.getProperties().indexOf(selectedProperty));
        } else
            alertErrorWindow("Nothing selected!", "You must select a property to delete it.");
    }

    @FXML
    private void addTag() {
        boolean isRepeated = false;
        if (!tagNameField.getText().isBlank()) {
            for (int i = 0; i < MainController.tagList.size(); i++) {
                if (MainController.tagList.get(i).toString().equals(tagNameField.getText())) {
                    isRepeated = true;
                    alertErrorWindow("Tag Exists", "This tag already exists. Please choose it from choice box!");
                }
            }
            if (!isRepeated) {
                Tag tag = new Tag(tagNameField.getText());
                clickedItem.addTag(tag);
                tagListView.getItems().add(tag);
                MainController.tagList.add(tag);
                catalog.addTag(tag);
                tagsBox.getItems().add(tag);
            }
            tagNameField.clear();
        } else if (tagsBox.getValue() != null || tagsBox.getValue().equals("Tags")) {
            for (int i = 0; i < clickedItem.getTags().size(); i++) {
                if (clickedItem.getTags().get(i).toString().equals(tagsBox.getValue().toString()))
                    isRepeated = true;
            }
            if (!isRepeated) {
                tagListView.getItems().add(MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
                ((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue()))).addItems(clickedItem);
                clickedItem.getTags().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
            }
        }
    }

    @FXML
    private void deleteTag() {
        Tag selectedTag = (Tag) tagListView.getSelectionModel().getSelectedItem();
        if (selectedTag != null) {
            tagListView.getItems().remove(selectedTag);
            selectedTag.getItems().remove(clickedItem);
            clickedItem.getTags().remove(selectedTag);
        } else
            alertErrorWindow("Nothing selected!", "You must select a type to delete it.");
    }

    @FXML
    private void changeType() {
        if (typesBox.getValue() != clickedItem.getType() && !typeNameField.getText().equals(clickedItem.getType().toString())) {
            clickedItem.getType().getItems().remove(clickedItem);
            if (!typesBox.getValue().equals(clickedItem.getType()))
                clickedItem.setType((Type) typesBox.getValue());
            else if (!typeNameField.getText().isBlank()) {
                Type type = new Type(typeNameField.getText());
                clickedItem.setType(type);
                MainController.typeList.add(type);
                typeNameField.clear();
                typesBox.getItems().add(type);
            } else
                alertErrorWindow("Type is null", "Please press the 'Change Type' button to add a type");
        }
    }

    @FXML
    public void editItem() {
        if (!clickedItem.getType().toString().equals(typeNameField.getText()) && !clickedItem.getType().toString().equals(typesBox.getValue().toString()))
            alertErrorWindow("Click the Button", "If you want to change type, you must press 'Change Type' button!!");
        else {
            clickedItem.getType().getItems().add(clickedItem);
            alertSuccessWindow("Item Editted!!", "Item is successfully editted");
            Stage stage = (Stage) close.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    public void tableView(TableView t) {
        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, ArrayList<String>>("content"));
        propertyTable.getColumns().add(labelColumn);
        propertyTable.getColumns().add(contentColumn);
        for (int i = 0; i < t.getItems().size(); i++)
            propertyTable.getItems().add(t.getItems().get(i));
    }

    public void listView() {
        for (Tag tag : clickedItem.getTags())
            tagListView.getItems().add(tag);
    }

    public void choiceBoxes(ArrayList typeList, ArrayList taglist) {
        typesBox.setValue(clickedItem.getType());
        typesBox.getItems().addAll(typeList);

        tagsBox.setValue("Tags");
        tagsBox.getItems().addAll(taglist);
    }
}
