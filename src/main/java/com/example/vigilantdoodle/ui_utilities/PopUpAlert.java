package com.example.vigilantdoodle.ui_utilities;

import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class PopUpAlert {

    public  static void displayPopUpAlert(String title, String message){
        Stage window = new Stage();

        //prevents out put from other parts of the program
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label1 = new Label();
        label1.setText(message);
        label1.setWrapText(true);
        label1.setTextAlignment(TextAlignment.CENTER);

        Button closeButton = new Button("Close window");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setMaxWidth(350);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

}
