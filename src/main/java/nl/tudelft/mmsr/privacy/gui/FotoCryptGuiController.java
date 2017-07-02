package nl.tudelft.mmsr.privacy.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import nl.tudelft.mmsr.privacy.detection.FaceOperation;
import nl.tudelft.mmsr.privacy.io.DialogOperations;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by jeroen on 6/25/17.
 */
public class FotoCryptGuiController implements Initializable {

    private FaceOperation FaceOperation;
    private boolean decryptionMode = false;
    private boolean aesmodus = true;

    @FXML
    private MenuItem menuLoadFile;

    @FXML
    private MenuItem menuEncryption;

    @FXML
    private MenuItem menuDecryption;

    @FXML
    private MenuItem menuSelectPK;

    @FXML
    private MenuItem menuEncryptRSA;

    @FXML
    private MenuItem menuDecryptRSA;

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
                    FaceOperation.detectFaces(!aesmodus);
                } else {
                    FaceOperation.decryptFaces(!aesmodus);
                }
            }
        });
        initializeMenu();
    }

    private void initializeMenu() {
        menuLoadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DialogOperations dialogOperations = new DialogOperations(FaceOperation.getController());
                File srcImage = dialogOperations.loadSourceImage();
                FaceOperation.setImageSrcPath(srcImage);
            }
        });
        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DialogOperations dialogOperations = new DialogOperations(FaceOperation.getController());
                dialogOperations.displayAbout();
            }
        });

        menuEncryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeField.setText("Current Mode: Encryption - AES CBC");
                buttonEncrypt.setText("Encrypt");
                decryptionMode = false;
                aesmodus = true;
            }
        });

        menuDecryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeField.setText("Current Mode: Decryption - AES CBC");
                buttonEncrypt.setText("Decrypt");
                decryptionMode = true;
                aesmodus = true;
            }
        });

        menuSelectPK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DialogOperations dialogOperations = new DialogOperations(FaceOperation.getController());
                File srcRSA = dialogOperations.loadRSAfile();
                FaceOperation.setRSAfile(srcRSA);
            }
        });

        menuEncryptRSA.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeField.setText("Current Mode: Encryption - RSA");
                buttonEncrypt.setText("Encrypt");
                decryptionMode = false;
                aesmodus = false;
            }
        });

        menuDecryptRSA.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeField.setText("Current Mode: Decryption - RSA");
                buttonEncrypt.setText("Decrypt");
                decryptionMode = true;
                aesmodus = false;
            }
        });

    }

    public void setFaceOperation(FaceOperation faceOperation) {
        this.FaceOperation = faceOperation;
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
}
