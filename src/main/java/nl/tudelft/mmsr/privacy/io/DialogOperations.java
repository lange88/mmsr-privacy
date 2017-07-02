package nl.tudelft.mmsr.privacy.io;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import nl.tudelft.mmsr.privacy.gui.FotoCryptGuiController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ptekieli on 7/2/17.
 */
public class DialogOperations {
    private FotoCryptGuiController controller;


    public DialogOperations(FotoCryptGuiController controller) {
        this.controller = controller;
    }

    public File loadSourceImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.jpeg", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                controller.getImageSource().setImage(image);
                controller.getTextImagePath().setText("Current File: " + file.getAbsolutePath());
                return file;
            } catch (IOException ex) {
                System.out.println(ex.getStackTrace());
            }
        }
        else {
            showAlert();
        }
        return null;
    }

    public File loadRSAfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open RSA Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("RSA resource", "*.rsa")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            return file;
        }
        else {
            showAlert();
        }
        return null;
    }

    public void displayAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About FotoCrypt");
        alert.setHeaderText("About");
        alert.setContentText("Wubba Lubba Dub Dub");
        alert.showAndWait();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Cancelled");
        alert.setHeaderText("No files chosen!");
        alert.setContentText("This action was interrupted by user.");
        alert.showAndWait();
    }
}
