package com.example.catalog;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateController extends MainController implements Initializable {
    private Item item;
    private Type tempType;
    @FXML
    private ChoiceBox<Type> typesBox;
    @FXML ChoiceBox<Tag> tagsBox;
    @FXML
    private TextField itemName, typeNameField, propertyLabel, propertyContent, tagNameField;
    @FXML
    private Button create;
    @FXML
    private TableView propertyTable;
    @FXML
    private ListView tagListView;
    @FXML
    private ListView<String> fieldListView;
    @FXML
    private Label fieldName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        item = new Item(null, null);
        item.setType(tempType);
        typesBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                fieldListView(item);
            }
        });
        fieldListView.setPlaceholder(new Label("No Content In List"));
    }

    @FXML
    public void createItem() {
        if (typesBox.getValue() != null) {
            item.setName(itemName.getText());
            item.setType((Type) typesBox.getValue());
            MainController.itemList.add(item);
            catalog.addItem(item);
            for (Object type: MainController.typeList){
                Type t = (Type) type;
                if (t.getItems().size() == 0)
                    t.deleteAllFieldLabel();
            }
            alertSuccessWindow("Item Created!!", "Item is successfully created");

            Stage stage = (Stage) create.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        } else
            alertErrorWindow("Type is null", "Please press the 'Add Item Type' button to add a type");
    }

    @FXML
    private void createType() {

        if (!typeNameField.getText().isBlank()) {
            if (catalog.searchType(typeNameField.getText()).contains(typeNameField.getText())) {
                alertErrorWindow("Type Exists", "This type already exists. Please choose it from choice box!");
            } else {
                Type type = new Type(typeNameField.getText());
                fieldListView(item);
                tempType = type;
                typesBox.getItems().add(tempType);
                typesBox.setValue(tempType);
                MainController.typeList.add(tempType);

                catalog.addType(type);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("You have created a new type");
                alert.setContentText("Would you like to continue creating an item?");

                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent(choice -> {
                    if (choice == noButton) {
                        Stage stage = (Stage) create.getScene().getWindow();
                        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                    } else {
                        alert.close();
                    }
                });
            }
        }
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
                    break;
                } else if (property.getLabel().equals(item.getProperties().get(i).getLabel())) {
                    isRepeated = true;
                    alertErrorWindow("This label exists", "You have already created a property with this label!!");
                    propertyContent.clear();
                    propertyLabel.clear();
                    break;
                }
            }
            if (!isRepeated) {
                item.createProperty(property);
                propertyTable.getItems().add(property);
                fieldListView(item);
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
            item.getProperties().remove(selectedProperty);
            boolean isAlone = true;
            loop:
            for (Item item : type.getItems())
                for (Property property : item.getProperties())
                    if (property.getLabel().equals(selectedProperty.getLabel())) {
                        isAlone = false;
                        break loop;
                    }
            if (isAlone) {
                type.deleteFieldLabel(selectedProperty.getLabel());
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
                item.addTag(tag);
                tagListView.getItems().add(tag);
                MainController.tagList.add(tag);
                MainController.tempTagList.add(tag);
                catalog.addTag(tag);
                tagsBox.getItems().add(tag);
                tagsBox.setValue(tag);
            }
            tagNameField.clear();
        } else if (!tagsBox.getValue().equals("Tags")) {
            for (Tag tag : item.getTags())
                if (tag.getTag().equals(tagsBox.getValue().toString())) {
                    isRepeated = true;
                    alertErrorWindow("Tag Exists", "This tag already exists. Please choose another tag from choice box or create new tag!");
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
            selectedTag.getItems().remove(item);
            item.getTags().remove(selectedTag);
        } else
            alertErrorWindow("Nothing selected!", "You must select a type to delete it.");
    }

    @FXML
    private void listViewSelection() {
        if (fieldListView.getSelectionModel().getSelectedItem() != null)
            propertyLabel.setText(fieldListView.getSelectionModel().getSelectedItem().toString());
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

    public void tableView() {
        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, ArrayList<String>>("content"));
        propertyTable.getColumns().add(labelColumn);
        propertyTable.getColumns().add(contentColumn);
        propertyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void choiceBoxes(ArrayList typeList, ArrayList tagList) {
        typesBox.setValue(null);
        typesBox.getItems().addAll(typeList);

        tagsBox.setValue(null);
        tagsBox.getItems().addAll(tagList);
    }
}