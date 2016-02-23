package tutoringfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.DBProps;

/**
 *
 * @author Melissa Rhein
 */
public class TutoringFX extends Application{
  
  @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Tutoring.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setWidth(700);
        stage.setHeight(500);
        stage.setTitle("TutoringFX - " + DBProps.which);

        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
  
}
