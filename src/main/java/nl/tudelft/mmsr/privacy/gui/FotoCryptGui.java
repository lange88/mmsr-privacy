package nl.tudelft.mmsr.privacy.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Created by Jeroen Vrijenhoef on 6/25/17.
 */
public class FotoCryptGui {
    private Image srcImage;
    private Image resultImage;

    public FotoCryptGui(Stage stage) {
        createStage(stage);
    }

    public void createStage(Stage stage) {
        try {
            // load fxml file from resource and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FotoCryptGui.class.getResource("/mmsr-privacy-gui.fxml"));
            AnchorPane page = loader.load();
            FotoCryptGuiController bejeweledController = loader.getController();

            // init the scene and set dialog properties
            Scene scene = new Scene(page);
            stage.setScene(scene);
            //Image icon = new Image(FotoCryptGui.class.getResourceAsStream("/icon.png"));
            //stage.getIcons().add(icon);
            stage.setTitle("FotoCrypt v1.0");
            stage.setResizable(false);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
