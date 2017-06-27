package nl.tudelft.mmsr.privacy.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import nl.tudelft.mmsr.privacy.detection.FaceDetection;

/**
 * Created by Jeroen Vrijenhoef on 6/25/17.
 */
public class FotoCryptGui {
    private ImageView imageSrc;
    private ImageView imageResult;

    public FotoCryptGui(FaceDetection faceDetection, Stage stage) {
        createStage(faceDetection, stage);
    }

    public void createStage(FaceDetection faceDetection, Stage stage) {
        try {
            // load fxml file from resource and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FotoCryptGui.class.getResource("/mmsr-privacy-gui.fxml"));
            AnchorPane page = loader.load();
            FotoCryptGuiController fotoCryptGuiController = loader.getController();

            // init the scene and set dialog properties
            Scene scene = new Scene(page);
            stage.setScene(scene);
            //Image icon = new Image(FotoCryptGui.class.getResourceAsStream("/icon.png"));
            //stage.getIcons().add(icon);
            stage.setTitle("FotoCrypt v1.0");
            stage.setResizable(false);

            imageSrc = fotoCryptGuiController.getImageSource();
            imageResult = fotoCryptGuiController.getImageResult();
            faceDetection.setImageResult(imageResult);
            fotoCryptGuiController.setFaceDetection(faceDetection);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
