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
    private boolean decryptionMode = false;

    @FXML
    private MenuItem menuLoadFile;

    @FXML
    private MenuItem menuEncryption;

    @FXML
    private MenuItem menuDecryption;

    @FXML
    private MenuItem menuAbout;

    @FXML
    private TextField modeField;

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
                if (decryptionMode == false) {
                    faceDetection.detectFaces();
                } else {
                    //faceDetection.decryptFaces();
                }
            }
        });
        initializeMenu();
    }

    private void initializeMenu() {
        menuLoadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                faceDetection.loadSourceImage();
            }
        });
        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        menuEncryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeField.setText("Current Mode: Encryption");
                decryptionMode = false;
            }
        });

        menuDecryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeField.setText("Current Mode: Decryption");
                decryptionMode = true;
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
