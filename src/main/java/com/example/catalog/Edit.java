package com.example.catalog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Edit extends MainController implements Initializable{


    @FXML private ChoiceBox typesBox, tagsBox;
    private ArrayList typeBoxList, tagBoxList;
    @FXML private TextField typeNameField, propertyLabel, propertyContent, tagNameField;

    private TreeItem clickedItem;

    @FXML private TableView propertyTable;

    @FXML private ListView tagList;
    @FXML private Button  addTag, deleteTag, addProperty, deleteProperty, changeType, submit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void rename(String newName){

    }
    @FXML
    private void delete(){

    }
    @FXML
    private void addNew(){

    }
    @FXML
    private void rename(){

    }
    @FXML private void changeType(){

    }
    public void tableView(TableView t){
        labelColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("label"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Property, ArrayList<String>>("content"));
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
