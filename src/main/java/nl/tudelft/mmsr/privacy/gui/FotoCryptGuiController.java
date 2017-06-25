package nl.tudelft.mmsr.privacy.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.tudelft.mmsr.privacy.detection.FaceDetection;

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
    private ImageView imageSrc;

    @FXML
    private ImageView imageResult;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert buttonEncrypt != null;
        buttonEncrypt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // handle event
            }
        });
        initializeMenu();
    }

    public void initializeMenu() {
        menuLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Image Resource File");
                fileChooser.showOpenDialog(null);
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

    public ImageView getImageSource() {
        return imageSrc;
    }

    public ImageView getImageResult() {
        return imageResult;
    }
}
