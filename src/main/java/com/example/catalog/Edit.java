package com.example.catalog;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Edit extends MainController implements Initializable{
    @FXML Item selectedItem;
    @FXML private ChoiceBox typesBox, tagsBox;
    private ArrayList typeBoxList, tagBoxList;
    @FXML private TextField typeNameField, propertyLabel, propertyContent, tagNameField;

    private TreeItem clickedItem;

    @FXML private TableView propertyTable;

    @FXML private ListView tagList;
    @FXML private Button  addTag, deleteTag, addProperty, deleteProperty, changeType, submit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedItem = (Item)MainController.selectedItem.getValue();
    }
    @FXML
    private void addProperty(){
        boolean isRepeated=false;
        if(!propertyLabel.getText().isBlank()){
            Property property=new Property(propertyLabel.getText(),propertyContent.getText());
            for(int i=0;i<selectedItem.getProperties().size();i++){
                if(property.getLabel().toString().equals(selectedItem.getProperties().get(i).getLabel())){
                    isRepeated=true;

                }
            }
            if(isRepeated==false) {
                selectedItem.createProperty(property);
                propertyTable.getItems().add(property);
                tableView.getItems().add(property);

            }
            propertyContent.clear();
            propertyLabel.clear();
        }

    }
    @FXML
    private void deleteProperty(){
        Property selectedProperty=(Property)propertyTable.getSelectionModel().getSelectedItem();
        propertyTable.getItems().remove(selectedProperty);
        selectedItem.getProperties().remove(selectedItem.getProperties().indexOf(selectedProperty));
        tableView.getItems().remove(selectedProperty);
    }

    @FXML
    private void addTag(){
        boolean isRepeated=false;
        if (!tagNameField.getText().isBlank()) {
            for (int i = 0; i < MainController.tagList.size(); i++) {
                if (MainController.tagList.get(i).toString().equals(tagNameField.getText()))
                    isRepeated = true;
            }
            {
                if (isRepeated == false) {
                    Tag tag = new Tag(tagNameField.getText());
                    selectedItem.getTags().add(tag);
                    tag.addItems(selectedItem);
                    MainController.tagList.add(tag);
                    tagList.getItems().add(tag);

                }
                tagNameField.clear();
            }
        }
        else if(tagsBox.getValue()!=null){
            for(int i=0;i<selectedItem.getTags().size();i++){
                if(selectedItem.getTags().get(i).toString().equals(tagsBox.getValue().toString()))
                    isRepeated=true;
            }
            if(isRepeated==false) {
                tagList.getItems().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
                ((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue()))).addItems(selectedItem);
                selectedItem.getTags().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));

            }
        }

    }
    @FXML
    private void deleteTag(){
        Tag selectedTag=(Tag)tagList.getSelectionModel().getSelectedItem();
        tagList.getItems().remove(selectedTag);
        selectedTag.getItems().remove(selectedItem);
        selectedItem.getTags().remove(selectedItem.getTags().get(selectedItem.getTags().indexOf(selectedTag)));
        if(selectedTag.getItems().size()==0)
            MainController.tagList.remove(selectedTag);

    }

    @FXML private void changeType(){
       if(!typeNameField.getText().isBlank()) {
           Type type=new Type(typeNameField.getText());
           selectedItem.setType(type);
           type.getItems().add(selectedItem);
           MainController.typeList.add(type);
           typeNameField.clear();
       }
       else if(typesBox!=null){
           selectedItem.getType().getItems().remove(selectedItem);
           selectedItem.setType((Type)typesBox.getValue());
           selectedItem.getType().getItems().add(selectedItem);
       }


    }
    public void tableView(TableView t){
        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("content"));
        propertyTable.getColumns().add(labelColumn);
        propertyTable.getColumns().add(contentColumn);
        for (int i=0;i<t.getItems().size();i++)
            propertyTable.getItems().add(t.getItems().get(i));
    }
    public void listView(Item item){
        for (Tag tag : item.getTags())
            tagList.getItems().add(tag);
    }
    public void choiceBoxes(ArrayList typeList, ArrayList tagslist){
        typesBox.setValue("Types");
        typesBox.getItems().addAll(typeList);

        tagsBox.setValue("Tags");
        tagsBox.getItems().addAll(tagslist);
    }


    public void setClickedItem(TreeItem clickedItem) {
        this.clickedItem = clickedItem;
    }

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }
}
