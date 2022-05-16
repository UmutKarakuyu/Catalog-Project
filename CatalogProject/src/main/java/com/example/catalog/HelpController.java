package com.example.catalog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class HelpController {

    @FXML
    public Button mainBack,mainScreenHelp,editScreenHelp,createScreenHelp,editBack,createBack,create,main,edit;

    @FXML
    private Pane mainHelp,editHelp,createHelp,help;

    @FXML
    private void back(){
        mainHelp.setVisible(false);
        editHelp.setVisible(false);
        createHelp.setVisible(false);
        help.setVisible(true);

    }
    @FXML
    private void create(){
        help.setVisible(false);
        mainHelp.setVisible(false);
        editHelp.setVisible(false);
        createHelp.setVisible(true);


    }
    @FXML
    private void edit(){
        help.setVisible(false);
        mainHelp.setVisible(false);
        editHelp.setVisible(true);
        createHelp.setVisible(false);
    }
    @FXML
    private void main(){
        help.setVisible(false);
        mainHelp.setVisible(true);
        editHelp.setVisible(false);
        createHelp.setVisible(false);

    }

}
