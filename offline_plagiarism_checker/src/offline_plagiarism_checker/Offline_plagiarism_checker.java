/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline_plagiarism_checker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author El Salvacion
 */
public class Offline_plagiarism_checker extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("offline_plagiarism_checker.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon.png")));
        stage.setTitle("Offline Plagiarism Checker");
        stage.setScene(scene);
        stage.setMaxWidth(760);
        stage.setMaxHeight(615);
        stage.setMinWidth(760);
        stage.setMinHeight(615);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
