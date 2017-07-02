package nl.tudelft.mmsr.privacy.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.mmsr.privacy.detection.FaceOperation;
import org.opencv.core.Core;

/**
 * Created by Jeroen Vrijenhoef on 6/25/17.
 */
public class FotoCryptGui {
    private ImageView imageSrc;
    private ImageView imageResult;

    public FotoCryptGui(FaceOperation FaceOperation, Stage stage) {
        createStage(FaceOperation, stage);
    }

    public void createStage(FaceOperation FaceOperation, Stage stage) {
        try {
            /* Load OpenCV SO or DLL library */
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            /* Load FXML file from resource and create a new stage for the dialog */
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FotoCryptGui.class.getResource("/mmsr-privacy-gui.fxml"));
            AnchorPane page = loader.load();
            FotoCryptGuiController fotoCryptGuiController = loader.getController();

            /* Init the scene and set dialog properties */
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle("FotoCrypt v1.0");
            stage.setResizable(false);

            imageSrc = fotoCryptGuiController.getImageSource();
            imageResult = fotoCryptGuiController.getImageResult();
            FaceOperation.setImageResult(imageResult);
            FaceOperation.setFotoCryptGuiController(fotoCryptGuiController);
            fotoCryptGuiController.setFaceOperation(FaceOperation);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
