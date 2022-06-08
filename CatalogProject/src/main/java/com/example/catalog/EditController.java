package com.example.catalog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditController extends MainController implements Initializable {
    private int state;
    private Item item;
    private Type selectedType;
    private Tag selectedTag;
    @FXML
    private StackPane stackPane;
    @FXML
    private TextField editTypeName;
    @FXML
    private ListView<Type> typeListView;
    @FXML
    private ListView<String> typeFieldNames;
    @FXML
    private ListView<String> typeFieldLabel;
    @FXML
    private Label currType;
    @FXML
    private ComboBox<Tag> tagComboBox;
    @FXML
    private TextField newTagName;
    @FXML
    private TextField editTagName;
    @FXML
    private ListView<Tag> itemsTagsListView;
    @FXML
    private TableView<Property> propertyTableView;
    @FXML
    private TextField propertyLabel;
    @FXML
    private TextField propertyContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearPage();
        state = tempState;
        stackPane.getChildren().get(state).setVisible(true);

        item = (Item) selectedObject.getValue();

        if (state == 0) {
            currType.setText(item.getType().toString());
            selectedType = item.getType();
            editTypeName.setText(selectedType.getName());
            typeListView.getItems().addAll(Catalog.typeList);
            typeFieldNames.getItems().addAll(item.getType().getFieldLabels());
        } else if (state == 1) {
            tagComboBox.getItems().addAll(Catalog.tagList);
            itemsTagsListView.getItems().addAll(item.getTags());
        } else if (state == 2) {

            TableColumn<Property, String> labelColumn = new TableColumn<>("Label");
            TableColumn<Property, ArrayList<String>> contentColumn = new TableColumn<>("Content");

            labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
            contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

            labelColumn.setStyle("-fx-alignment: CENTER");
            contentColumn.setStyle("-fx-alignment: CENTER");
            propertyTableView.getColumns().add(labelColumn);
            propertyTableView.getColumns().add(contentColumn);
            propertyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            typeFieldNames.getItems().addAll(item.getType().getFieldLabels());
        }
    }

    @FXML
    private void createProperty() {
        boolean isRepeated = false;
        if (!propertyLabel.getText().isBlank() && !propertyContent.getText().isBlank()) {
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
                propertyTableView.getItems().add(property);
                typeFieldLabel.getItems().add(propertyLabel.getText());
                propertyLabel.clear();
                propertyContent.clear();
            }
        } else if (propertyLabel.getText().isBlank())
            alertErrorWindow("Error", "Error");
        else if (propertyContent.getText().isBlank())
            alertErrorWindow("Error", "Error");
    }

    @FXML
    private void deleteProperty() {
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            item.getType().deleteFieldLabel(selectedProperty.getLabel()); // deletes if there is no other
            item.deleteProperty(selectedProperty);
            propertyTableView.getItems().remove(selectedProperty);
        }
        else
            alertErrorWindow("Error", "Error");
    }

    @FXML
    private void selectedFieldName() {
        String s = typeFieldLabel.getSelectionModel().getSelectedItem();
        if (s != null)
            propertyLabel.setText(s);
    }

    @FXML
    private void selectedTag() {
        selectedTag = itemsTagsListView.getSelectionModel().getSelectedItem();
        editTagName.setText(selectedTag.toString());
    }

    @FXML
    private void addTag() {
        if (tagComboBox.getValue() != null && !item.getTags().contains(tagComboBox.getValue())) {
            item.addTag(tagComboBox.getValue());
            tagComboBox.getValue().addItems(item);
            itemsTagsListView.getItems().add(tagComboBox.getValue());
        }
    }

    @FXML
    private void createNewTag() {
        if (!newTagName.getText().isBlank()) {
            Tag newTag = new Tag(newTagName.getText());
            catalog.createTag(newTag);
            item.addTag(newTag);
            newTag.addItems(item);
            tagComboBox.getItems().add(newTag);
            itemsTagsListView.getItems().add(newTag);
            tempTagList.add(newTag);
            newTagName.clear();
        }
    }

    @FXML
    private void deleteTagFromItem() {
        if (selectedTag != null) {
            item.deleteTag(selectedTag);
            selectedTag.getItems().remove(item);
            itemsTagsListView.getItems().remove(selectedTag);
            tempTagList.remove(selectedTag);
        }
    }

    @FXML
    private void deleteTagFromCatalog() {
        if (selectedTag != null) {
            catalog.deleteTag(selectedTag);
            tagComboBox.getItems().remove(selectedTag);
            itemsTagsListView.getItems().remove(selectedTag);
            tempTagList.remove(selectedTag);
        }
    }

    @FXML
    private void renameTag() {
        if (selectedTag != null) {
            selectedTag.setName(editTagName.getText());
            itemsTagsListView.refresh();
        }
    }

    @FXML
    private void selectedType() {
        selectedType = typeListView.getSelectionModel().getSelectedItem();
        if (selectedType != null) {
            typeFieldNames.getItems().clear();
            typeFieldNames.getItems().addAll(selectedType.getFieldLabels());
            currType.setText(selectedType.getName());
        }
    }

    @FXML
    private void deleteType() {
        if (selectedType != null) {
            catalog.deleteType(selectedType);
            typeFieldNames.getItems().clear();
            if (selectedType == item.getType()) {
                close();
            }
        }
    }

    @FXML
    private void deleteFieldName() {
        String s = typeFieldNames.getSelectionModel().getSelectedItem();
        if (s != null) {
            selectedType.deleteFieldLabel(s);
            typeFieldNames.getItems().remove(s);
        }
    }

    @FXML
    private void renameType() {
        if (!editTypeName.getText().isBlank() && !editTypeName.getText().equals(item.getType().getName())) {
            selectedType.setName(editTypeName.getText());
            typeListView.refresh();
        }
    }

    @FXML
    private void close() {
        if (state == 0) {
            if (!selectedType.equals(item.getType())) {
                boolean isTrue = alertYesNoWindow("Type changed", "Are you sure you want to change the type");
                if (isTrue) {
                    item.getType().getItems().remove(item);
                    item.setType(selectedType);
                    item.getType().getItems().add(item);

                    Stage stage = (Stage) stackPane.getScene().getWindow();
                    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                }
            } else {
                Stage stage = (Stage) stackPane.getScene().getWindow();
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        }
        else{
            Stage stage = (Stage) stackPane.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    private void clearPage() {
        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(false);
        stackPane.getChildren().get(2).setVisible(false);
    }
}
