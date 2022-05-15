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

public class CreateController extends MainController implements Initializable {
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
    @FXML
    private ListView<String> fieldListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        item = new Item(null, null);
        fieldListView.setVisible(false);
    }

    @FXML
    public void createItem() {
        if (!itemName.getText().isBlank()) {
            if (catalog.searchItem(itemName.getText()).size() == 0) {
                item.setName(itemName.getText());
                if (item.getType() != null) {
                    MainController.itemList.add(item);
                    catalog.addItem(item);
                    alertSuccessWindow("Item Created!!", "Item is successfully created");

                    Stage stage = (Stage) create.getScene().getWindow();
                    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                } else
                    alertErrorWindow("Type is null", "Please press the 'Add Item Type' button to add a type");
            } else
                alertErrorWindow("Name exists", "Please change the name of the item");
        } else
            alertErrorWindow("Enter a name", "Please enter an item name");
    }

    @FXML
    private void createType() {
        if ((typeNameField.getText().isBlank() && item.getType() == null)|| (item.getType() != null && !typesBox.getValue().equals(item.getType()))) {
            Type type = (Type) MainController.typeList.get(MainController.typeList.indexOf(typesBox.getValue()));
            if (item.getType() != null)
                item.getType().deleteFieldLabels(item.getProperties());
            item.setType(type);
            fieldListView(item);
            typesBox.setValue(type);
        } else if (!typeNameField.getText().isBlank()) {
            if (catalog.searchType(typeNameField.getText()).size() != 0) {
                alertErrorWindow("Type Exists", "This type already exists. Please choose it from choice box!");
            } else {
                Type type = new Type(typeNameField.getText());
                item.setType(type);
                typesBox.getItems().add(type);
                typesBox.setValue(type);
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
            propertyTable.getItems().remove(selectedProperty);
            item.getProperties().remove(selectedProperty);
            boolean isAlone = true;
            loop:
            for (Item item : item.getType().getItems())
                for (Property property : item.getProperties())
                    if (property.getLabel().equals(selectedProperty.getLabel())) {
                        isAlone = false;
                        break loop;
                    }
            if (isAlone) {
                item.getType().deleteFieldLabel(selectedProperty.getLabel());
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
        fieldListView.getItems().clear();
        if (item.getType() != null) {
            item.getType().addFieldLabels(item.getProperties());

            for (String string : item.getType().getFieldLabels())
                if (!fieldListView.getItems().contains(string))
                    fieldListView.getItems().add(string);

            fieldListView.setVisible(true);
        }
        if (fieldListView.getItems().size() == 0)
            fieldListView.setVisible(false);
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