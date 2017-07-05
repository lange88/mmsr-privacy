package nl.tudelft.mmsr.privacy.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import nl.tudelft.mmsr.privacy.detection.FaceOperation;
import nl.tudelft.mmsr.privacy.io.DialogOperations;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by jeroen on 6/25/17.
 */
public class FotoCryptGuiController implements Initializable {

    private FaceOperation FaceOperation;
    private boolean decryptionMode = false;

    /* Default Cascade Classifier */
    private String currentClasifier = "configuration/haarcascade_frontalface_default.xml";
    /* Default anonymization fitler */
    private String filter = "gaussianblur";

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
    private MenuItem buttonalt1;

    @FXML
    private MenuItem buttonalt2;

    @FXML
    private MenuItem buttontree;

    @FXML
    private MenuItem menugaus;

    @FXML
    private MenuItem menuerode;

    @FXML
    private MenuItem menudilate;

    @FXML
    private Button buttonCalculate;

    @FXML
    private ComboBox<Double> comboBox;

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
        comboBox.setItems(FXCollections.observableArrayList((double) 0.0));
        comboBox.getSelectionModel().selectFirst();
        for (int i = 0 ; i < 11; i += 2) {
            if (i != 0) {
                double x = (i / 10.0);
                comboBox.getItems().addAll(x);
            }
        }
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
                centerImage(imageSrc);
            }
        });

        buttonCalculate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Double temp = comboBox.getValue() * 100;
                correctionParam = temp.intValue() ;
                FaceOperation.displayFaces(correctionParam);
                centerImage(imageResult);
            }
        });

        buttonEncrypt.setOnAction(new EventHandler<ActionEvent>() {
            @Override

            public void handle(ActionEvent event) {
                if (decryptionMode == false) {
                    FaceOperation.encryptFaces(filter);
                } else {
                    FaceOperation.decryptFaces();
                }
                centerImage(imageResult);
            }
        });

        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DialogOperations dialogOperations = new DialogOperations(FaceOperation.getController());
                dialogOperations.displayAbout();
            }
        });

        menugaus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filter = "gaussianblur";
            }
        });

        menuerode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filter = "erode";
            }
        });

        menudilate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filter = "dilate";
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

        buttondefault.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentClasifier = "configuration/haarcascade_frontalface_default.xml";
            }
        });

        buttonalt1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentClasifier = "configuration/haarcascade_frontalface_alt.xml";
            }
        });

        buttonalt2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentClasifier = "configuration/haarcascade_frontalface_alt2.xml";
            }
        });

        buttontree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentClasifier = "configuration/haarcascade_frontalface_alt_tree.xml";
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

    private void centerImage(ImageView imageView) {
        javafx.scene.image.Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);
        }
    }
}
