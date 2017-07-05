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
    private String currentClasifier = "configuration/haarcascade_frontalface_alt.xml";
    private String filter = "gaussian";
    private int correctionParam = 0;

    @FXML
    private MenuItem menuLoadFile;

    @FXML
    private MenuItem menuEncryption;

    @FXML
    private MenuItem menuDecryption;

    @FXML
    private MenuItem menuSelectPK;

    @FXML
    private MenuItem menuAbout;

    @FXML
    private MenuItem buttondefault;

    @FXML
    private MenuItem buttonalternative1;

    @FXML
    private MenuItem buttonalternative2;

    @FXML
    private MenuItem buttontree;

    @FXML
    private Button buttonCalculate;

    @FXML
    private TextArea textField;

    @FXML
    private ComboBox comboBox;

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
        assert buttonCalculate != null;

        initializeMenu();
    }

    private void initializeMenu() {

        menuLoadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DialogOperations dialogOperations = new DialogOperations(FaceOperation.getController());
                File srcImage = dialogOperations.loadSourceImage();
                FaceOperation.setImageSrcPath(srcImage);
                FaceOperation.detectFaces(FaceOperation.setCascadeClassifier(currentClasifier));
            }
        });

        buttonCalculate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FaceOperation.displayFaces(correctionParam);
            }
        });

        buttonEncrypt.setOnAction(new EventHandler<ActionEvent>() {
            @Override

            public void handle(ActionEvent event) {
                if (decryptionMode == false) {
                    //FaceOperation.encryptFaces();
                } else {
                    //FaceOperation.decryptFaces();
                }
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
                modeField.setText("Current Mode: Encryption");
                buttonEncrypt.setText("Encrypt");
                decryptionMode = false;
            }
        });

        menuDecryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeField.setText("Current Mode: Decryption");
                buttonEncrypt.setText("Decrypt");
                decryptionMode = true;
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
