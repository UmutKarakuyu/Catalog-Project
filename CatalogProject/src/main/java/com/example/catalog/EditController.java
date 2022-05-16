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
    private ChoiceBox<Type> typesBox;
    @FXML private ChoiceBox<Tag> tagsBox;
    @FXML
    private TextField typeNameField, propertyLabel, propertyContent, tagNameField;

    private Item clickedItem;
    private Type tempType;

    @FXML
    private TableView propertyTable;

    @FXML
    private ListView tagListView, fieldListView;
    @FXML
    private Button addTag, deleteTag, addProperty, deleteProperty, changeType, close;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickedItem = (Item) MainController.selectedItem.getValue();
        typesBox.setValue(clickedItem.getType());
    }

    @FXML
    private void listViewSelection() {
        if (fieldListView.getSelectionModel().getSelectedItem() != null)
            propertyLabel.setText(fieldListView.getSelectionModel().getSelectedItem().toString());
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
                    break;
                } else if (property.getLabel().equals(clickedItem.getProperties().get(i).getLabel())) {
                    isRepeated = true;
                    alertErrorWindow("This label exists", "You have already created a property with this label!!");
                    propertyContent.clear();
                    propertyLabel.clear();
                    break;
                }
            }
            if (!isRepeated) {
                clickedItem.createProperty(property);
                propertyTable.getItems().add(property);
                fieldListView(clickedItem);
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
            Type type = (Type) typesBox.getValue();
            propertyTable.getItems().remove(selectedProperty);
            clickedItem.getProperties().remove(selectedProperty);
            boolean isAlone = true;
            loop:
            for (Item item : type.getItems())
                for (Property property : item.getProperties())
                    if (property.getLabel().equals(selectedProperty.getLabel())) {
                        isAlone = false;
                        break loop;
                    }
            if (isAlone) {
                clickedItem.getType().deleteFieldLabel(selectedProperty.getLabel());
                fieldListView.getItems().remove(selectedProperty.getLabel());
            }
        } else
            alertErrorWindow("Nothing selected!", "You must select a property to delete it.");
    }

    @FXML
    private void addTag() {
        boolean isRepeated = false;
        if (!tagNameField.getText().isBlank()) {
            if (catalog.searchTag(tagNameField.getText()).contains(tagNameField.getText())) {
                alertErrorWindow("Tag Exists", "This tag already exists in choice box. Please choose different tag or create new tag!");
            } else {
                Tag tag = new Tag(tagNameField.getText());
                clickedItem.addTag(tag);
                tagListView.getItems().add(tag);
                MainController.tagList.add(tag);
                MainController.tempTagList.add(tag);
                catalog.addTag(tag);
                tagsBox.getItems().add(tag);
                tagsBox.setValue(tag);
            }
            tagNameField.clear();
        } else if (!tagsBox.getValue().equals(clickedItem.getType())) {
            for (Tag tag : clickedItem.getTags())
                if (tag.toString().equals(tagsBox.getValue().toString())) {
                    isRepeated = true;
                    alertErrorWindow("Tag Exists", "This tag already exists. Please choose another tag from choice box or create new tag!");
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
    private void createType() {

        if (!typeNameField.getText().isBlank()) {
            if (catalog.searchType(typeNameField.getText()).contains(typeNameField.getText())) {
                alertErrorWindow("Type Exists", "This type already exists. Please choose it from choice box!");
            } else {
                Type type = new Type(typeNameField.getText());
                fieldListView(clickedItem);
                tempType = type;
                typesBox.getItems().add(type);
                typesBox.setValue(type);
                MainController.typeList.add(type);
                clickedItem.setType(type);
                catalog.addType(type);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("You have created a new type");
                alert.setContentText("Would you like to continue creating an item?");

                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent(choice -> {
                    if (choice == noButton) {
                        Stage stage = (Stage) close.getScene().getWindow();
                        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                    } else {
                        alert.close();
                    }
                });
            }
        }
    }

    @FXML
    public void editItem() {
        if (!typeNameField.getText().isBlank() || clickedItem.getType() != typesBox.getValue())
            alertErrorWindow("Click the Button", "If you want to change type, you must press 'Change Type' button!!");
        else {
            if (!clickedItem.getType().getItems().contains(clickedItem))
                clickedItem.getType().addItem(clickedItem);
            clickedItem.setType((Type) typesBox.getValue());
            for (Property p : clickedItem.getProperties())
                if (!clickedItem.getType().getFieldLabels().contains(p.getLabel()))
                    clickedItem.getType().addFieldLabel(p.getLabel());

            for (Object type: MainController.typeList){
                Type t = (Type) type;
                if (t.getItems().size() == 0)
                    t.deleteAllFieldLabel();
            }
            alertSuccessWindow("Item Edited!!", "Item is successfully edited");

            Stage stage = (Stage) close.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    public void tableView(TableView t) {
        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, ArrayList<String>>("content"));
        propertyTable.getColumns().add(labelColumn);
        propertyTable.getColumns().add(contentColumn);
        propertyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (int i = 0; i < t.getItems().size(); i++)
            propertyTable.getItems().add(t.getItems().get(i));
    }

    public void listView() {
        for (Tag tag : clickedItem.getTags())
            tagListView.getItems().add(tag);
    }

    public void fieldListView(Item item) {
        Type type = typesBox.getValue();
        if (type != null) {
            fieldListView.getItems().clear();
            type.addFieldLabels(item.getProperties());

            for (String string : type.getFieldLabels())
                if (!fieldListView.getItems().contains(string))
                    fieldListView.getItems().add(string);
        }
    }

    public void choiceBoxes(ArrayList typeList, ArrayList tagList) {
        typesBox.setValue(clickedItem.getType());
        typesBox.getItems().addAll(typeList);

        tagsBox.setValue(null);
        tagsBox.getItems().addAll(tagList);
    }
}
