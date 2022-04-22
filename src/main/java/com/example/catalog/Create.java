package com.example.catalog;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Create extends MainController implements Initializable {
    @FXML private Item item;
    @FXML private ChoiceBox typesBox, tagsBox;
    private final String[] objectList = {};
    private ArrayList typeBoxList, tagBoxList;
    @FXML private TextField itemName, typeNameField, propertyLabel, propertyContent, tagNameField;

    private TreeItem clickedItem;

    @FXML private TableView propertyTable;
    @FXML private ListView tagList;

    @FXML private Button  addTag, deleteTag, addProperty, deleteProperty, changeType, create,createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.catalog = new Catalog();
        item=new Item(null,"Default");

    }
    @FXML
    private void addProperty(){
        boolean isRepeated=false;
        if(propertyContent.getText()!=null&& propertyLabel.getText()!=null){
            Property property=new Property(propertyLabel.getText(),propertyContent.getText());
            for(int i=0;i<item.getProperties().size();i++){
             if(property.getLabel().toString().equals(item.getProperties().get(i).getLabel())){
                 isRepeated=true;
                 propertyContent.clear();
                 propertyLabel.clear();
             }
            }
            if(isRepeated==false) {
                item.createProperty(property);
                propertyTable.getItems().add(property);
                tableView.getItems().add(property);
                propertyContent.clear();
                propertyLabel.clear();
            }
        }
        }


    @FXML
    private void addTag(){
        boolean isRepeated=false;
        if (!tagNameField.getText().isBlank()){
            for(int i=0;i<MainController.tagList.size();i++){
                if(MainController.tagList.get(i).toString().equals(tagNameField.getText()))
                    isRepeated=true;
            }
            {
                if (isRepeated==false) {
                    Tag tag = new Tag(tagNameField.getText());
                    item.addTag(tag);
                    tagList.getItems().add(tag);
                    MainController.tagList.add(tag);
                }
            }
            tagNameField.clear();
        }
        else if(tagsBox.getValue()!=null){
            for(int i=0;i<item.getTags().size();i++){
                if(item.getTags().get(i).toString().equals(tagsBox.getValue().toString()))
                    isRepeated=true;
            }
            if(isRepeated==false) {
                tagList.getItems().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
                item.getTags().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
            }
        }
    }


    @FXML
    private void deleteTag(){
       Tag selectedTag=(Tag)tagList.getSelectionModel().getSelectedItem();
       tagList.getItems().remove(selectedTag);
       MainController.tagList.remove(selectedTag);
       item.getTags().remove(item.getTags().get(item.getTags().indexOf(selectedTag)));

    }

    @FXML
    private void deleteProperty(){
        Property selectedProperty=(Property)propertyTable.getSelectionModel().getSelectedItem();
        propertyTable.getItems().remove(selectedProperty);
        item.getProperties().remove(item.getProperties().indexOf(selectedProperty));
        tableView.getItems().remove(selectedProperty);

    }

    public void tableView(TableView t){
        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("content"));
        propertyTable.getColumns().add(labelColumn);
        propertyTable.getColumns().add(contentColumn);
        for (int i=0;i<t.getItems().size();i++)
            propertyTable.getItems().add(t.getItems().get(i));
    }
    public void listView(){
        for (Tag tag : item.getTags())
            tagList.getItems().add(tag);
    }
    public void choiceBoxes(ArrayList typeList, ArrayList tagslist){
        typesBox.setValue("Types");
        typesBox.getItems().addAll(typeList);

        tagsBox.setValue("Tags");
        tagsBox.getItems().addAll(tagslist);

    }

   @FXML
    private void createItem(){
       {
           if(itemName.getText()!=null){
               item.setName(itemName.getText());
               if(item.getType()!=null){
                   MainController.itemList.add(item);
                   item.getType().getItems().add(item);
                   if(item.getTags()!=null){
                       for(int i=0;i<item.getTags().size();i++){
                           item.getTags().get(i).getItems().add(item);
                       }
                       //((Stage)createButton.getScene().getWindow()).close();
                   }
               }
           }
       }


    }
    @FXML
    private void createType(){
        boolean isRepeated=false;
        if(!typeNameField.getText().isBlank()){
            for(int i=0;i<MainController.typeList.size();i++){
                if(MainController.typeList.get(i).toString().equals(typeNameField.getText()))
                    isRepeated=true;
            }
            if(isRepeated==false) {
                Type type = new Type(typeNameField.getText());
                item.setType(type);
                MainController.typeList.add(type);
            }
        }
        else if(typesBox.getValue()!=null ||(typesBox.getValue()!=null&&!typeNameField.getText().isBlank())){
            Type type=(Type)typeList.get(typeList.indexOf(typesBox.getValue()));
            item.setType(type);
        }
    }

}