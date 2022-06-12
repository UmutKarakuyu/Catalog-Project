package com.example.catalog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class HelpController {

    @FXML
    public Button back,export,create,main,edit;

    @FXML
    private Pane mainHelp,editHelp,createHelp,help,exportHelp;


    @FXML
    private void back(){
        mainHelp.setVisible(false);
        editHelp.setVisible(false);
        createHelp.setVisible(false);
        exportHelp.setVisible(false);
        help.setVisible(true);
        back.setVisible(false);

    }
    @FXML
    private void create(){
        mainHelp.setVisible(false);
        editHelp.setVisible(false);
        createHelp.setVisible(true);
        exportHelp.setVisible(false);
        help.setVisible(false);
        back.setVisible(true);
    }
    @FXML
    private void edit(){
        mainHelp.setVisible(false);
        editHelp.setVisible(true);
        createHelp.setVisible(false);
        exportHelp.setVisible(false);
        help.setVisible(false);
        back.setVisible(true);
    }
    @FXML
    private void main(){
        mainHelp.setVisible(true);
        editHelp.setVisible(false);
        createHelp.setVisible(false);
        exportHelp.setVisible(false);
        help.setVisible(false);
        back.setVisible(true);

    }
    @FXML
    private void export(){
        mainHelp.setVisible(false);
        editHelp.setVisible(false);
        createHelp.setVisible(false);
        exportHelp.setVisible(true);
        help.setVisible(false);
        back.setVisible(true);

    }

}
