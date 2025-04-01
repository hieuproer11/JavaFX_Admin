package fsiAdministration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
          //  Parent parent = FXMLLoader.load(getClass().getResource("/fsiAdministration/views/page_connexion.fxml"));
            Parent parent = FXMLLoader.load(getClass().getResource("/fsiAdministration/views/page_connexion.fxml"));
            // create a scene
            Scene scene = new Scene(parent);
            // set scene to a stage
            stage.setScene(scene);
            // show the stage
            stage.show();
        } // end try
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
