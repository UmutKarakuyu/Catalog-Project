package com.example.catalog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    @FXML private ListView tag_List;

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
                    tag_List.getItems().add(tag);
                    //MainController.tagList.add(tag);
                }
            }
            tagNameField.clear();
        }
        else{
            for(int i=0;i<item.getTags().size();i++){
                if(item.getTags().get(i).toString().equals(tagsBox.getValue().toString()))
                    isRepeated=true;
            }
            if(isRepeated==false) {
                tag_List.getItems().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
                item.getTags().add((Tag) MainController.tagList.get(MainController.tagList.indexOf(tagsBox.getValue())));
            }
        }
    }


    @FXML
    private void deleteTag(){
       Tag selectedTag=(Tag) tag_List.getSelectionModel().getSelectedItem();
       tag_List.getItems().remove(selectedTag);
       //MainController.tagList.remove(selectedTag);
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
            tag_List.getItems().add(tag);
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
           Type type = null;
           boolean isNewType = false;
           if(typeNameField.getText().isBlank() && typesBox.getValue()!=null){
               type=(Type)MainController.typeList.get(MainController.typeList.indexOf(typesBox.getValue()));
           } else if(!typeNameField.getText().isBlank()){
               boolean repeated = false;
               for(int i=0; i<MainController.typeList.size(); i++){
                   if(MainController.typeList.get(i).toString().equals(typeNameField.getText())){
                       repeated = true;
                       type = (Type) MainController.typeList.get(i);
                       break;
                   }
               }
               if(!repeated){
                   type= new Type(typeNameField.getText());
                   isNewType = true;
               }
           } else{
               return;
           }

           if(!itemName.getText().isBlank()){
               item = new Item(type, itemName.getText());
               if (propertyTable.getItems().size() != 0) {
                   for (int i = 0; i < propertyTable.getItems().size(); i++) {
                       String[] properties = propertyTable.getItems().get(i).toString().split(":");
                       item.createProperty(properties[0].replace(" ", ""), properties[1].replace(" ", ""));
                   }

                   if (tag_List.getItems().size() != 0) {
                       for (int i = 0; i < tag_List.getItems().size(); i++) {
                           Tag tag = null;
                           boolean repeated = false;
                           for(int j=0; j<MainController.tagList.size(); j++){
                               if(MainController.tagList.get(j).toString().equals(tag_List.getItems().get(i).toString())){
                                   repeated = true;
                                   tag = (Tag) MainController.tagList.get(j);
                                   tag.addItems(item);
                                   break;
                               }
                           }
                           if(!repeated){
                               tag = new Tag(tag_List.getItems().get(i).toString());
                               tag.addItems(item);
                               MainController.tagList.add(tag);
                           }

                           item.addTag(tag);
                       }
                   }

                   MainController.catalog.getItems().insert(item);
                   MainController.itemList.add(item);
                   type.addItems(item);
                   if (isNewType) {
                       MainController.typeList.add(type);
                   }


               } else {
                   return;
               }
           } else{
               return;
           }
       }


    }

    /*
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

     */

}