package com.example.catalog;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateController extends MainController implements Initializable {
    private int state;

    @FXML
    private StackPane stackPane;
    @FXML
    private TextField newTypeName;
    @FXML
    private TextField newItemName;
    @FXML
    private ChoiceBox<Type> typesBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        state = tempState;
        stackPane.getChildren().get(state).setVisible(true);
        if (state == 1)
            typesBox.getItems().addAll(Catalog.typeList);

        newTypeName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                   save();
                }
            }
        });
        newItemName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    save();
                }
            }
        });

    }

    @FXML
    private void save() {
        boolean error = false;
        if (state == 0) {
            if (newTypeName.getText().isBlank())
                alertErrorWindow("Error", "Please enter a type name to create a type");
            else {
                boolean isExists = false;
                for (Type type : Catalog.typeList){
                    if (type.getName().equals(newTypeName.getText())) {
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    catalog.createType(new Type(newTypeName.getText()));
                    error = true;
                }
                else {
                    alertErrorWindow("Type exists", "Type already exists in");
                    newTypeName.clear();
                }
            }
        } else if (state == 1) {
            if (newItemName.getText().isBlank())
                alertErrorWindow("Error", "Please enter a item name");
            else if (typesBox.getValue() == null)
                alertErrorWindow("Error", "Please choose a type from the choice box.");
            else {
                catalog.createItem(new Item(typesBox.getValue(), newItemName.getText()));
                error = true;
            }
        }
        if (error) {
            stackPane.getChildren().get(state).setVisible(false);
            Stage stage = (Stage) newTypeName.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }
}
