package com.example.catalog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
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
    }

    @FXML
    private void save() {
        boolean error = false;
        if (state == 0) {
            if (newTypeName.getText().isBlank())
                alertErrorWindow("Error", "Error");
            else {
                catalog.createType(new Type(newTypeName.getText()));
                error = true;
            }
        } else if (state == 1) {
            if (newItemName.getText().isBlank())
                alertErrorWindow("Error", "Error");
            else if (typesBox.getValue() == null)
                alertErrorWindow("Error", "Error");
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
