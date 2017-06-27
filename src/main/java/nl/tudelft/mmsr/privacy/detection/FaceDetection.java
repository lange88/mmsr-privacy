/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.mmsr.privacy.detection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import nl.tudelft.mmsr.privacy.gui.FotoCryptGuiController;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;

/**
 *
 * @author Piotr Tekieli
 */
public class FaceDetection {

    private File imageSrcFile;
    private ImageView imageResult;
    private Mat toProcess;
    private FotoCryptGuiController controller;

    public void loadSourceImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // load the file
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                controller.getImageSource().setImage(image);
                controller.getTextImagePath().setText("Current File: " + file.getAbsolutePath());
                imageSrcFile = file;

            } catch (IOException ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    public void saveImage() {

    }

    public void detectFaces() {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        CascadeClassifier faceDetector = new CascadeClassifier(new File("configuration/haarcascade_frontalface_alt.xml").getAbsolutePath());
        toProcess = Highgui.imread(imageSrcFile.getAbsolutePath());
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(toProcess, faceDetections);
 
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
 
        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(toProcess, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                new Scalar(0, 255, 0), 5);
        }
 
        String filename = "ouput.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, toProcess);
        loadResultImage(new File(filename));
    }

    private void loadResultImage(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageResult.setImage(image);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    public void setImageSrcPath(File file) {
        this.imageSrcFile = file;
    }

    public void setImageResult(ImageView imageResult) {
        this.imageResult = imageResult;
    }

    public void setFotoCryptGuiController(FotoCryptGuiController fotoCryptGuiController) {
        this.controller = fotoCryptGuiController;
    }
}
