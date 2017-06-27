package nl.tudelft.mmsr.privacy.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.tudelft.mmsr.privacy.detection.FaceDetection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;


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
                handleButtonEncrypt();
            }
        });
        initializeMenu();
    }

    private void initializeMenu() {
        menuLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleMenuLoad();
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

    private void handleMenuLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image Resource File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // load the file
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageSrc.setImage(image);
                faceDetection.setImageSrcPath(file);
                textImagePath.setText("Current File: " + file.getAbsolutePath());

            } catch (IOException ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void handleMenuSave() {
        throw new NotImplementedException();
    }

    private void handleMenuAddToList() {
        throw new NotImplementedException();
    }

    private void handleMenuAbout() {
        throw new NotImplementedException();
    }

    private void handleButtonEncrypt() {
        faceDetection.DetectFaces();
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
}
