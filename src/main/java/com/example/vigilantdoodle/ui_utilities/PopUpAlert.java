package com.example.vigilantdoodle.ui_utilities;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class PopUpAlert {

    public  static void displayPopUpAlert(String title, String message){
        Stage window = new Stage();

        //prevents out put from other parts of the progra
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label1 = new Label();
        label1.setText(message);
        Button closeButton = new Button("Close window");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

}
