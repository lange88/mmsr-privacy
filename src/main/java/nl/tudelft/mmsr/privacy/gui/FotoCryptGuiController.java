package nl.tudelft.mmsr.privacy.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import nl.tudelft.mmsr.privacy.detection.FaceDetection;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by jeroen on 6/25/17.
 */
public class FotoCryptGuiController implements Initializable {

    private FaceDetection faceDetection;

    @FXML
    private MenuItem menuLoad;

    @FXML
    private MenuItem menuSave;

    @FXML
    private MenuItem menuAddToList;

    @FXML
    private MenuItem menuAbout;

    @FXML
    private Button buttonEncrypt;

    @FXML
    private TextField textImagePath;

    @FXML
    private ImageView imageSrc;

    @FXML
    private ImageView imageResult;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert buttonEncrypt != null;
        buttonEncrypt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                faceDetection.detectFaces();
            }
        });
        initializeMenu();
    }

    private void initializeMenu() {
        menuLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                faceDetection.loadSourceImage();
            }
        });
        menuSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        menuAddToList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    public void setFaceDetection(FaceDetection faceDetection) {
        this.faceDetection = faceDetection;
    }

    public ImageView getImageSource() {
        return imageSrc;
    }

    public ImageView getImageResult() {
        return imageResult;
    }

    public TextField getTextImagePath() {
        return textImagePath;
    }

    public void setTextImagePath(TextField textImagePath) {
        this.textImagePath = textImagePath;
    }

    public void setImageSrc(ImageView imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void setImageResult(ImageView imageResult) {
        this.imageResult = imageResult;
    }
}
