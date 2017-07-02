/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.mmsr.privacy.detection;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import nl.tudelft.mmsr.privacy.encryption.AESDecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.DecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.EncryptionPack;
import nl.tudelft.mmsr.privacy.encryption.EncryptionStrategy;
import nl.tudelft.mmsr.privacy.gui.FotoCryptGuiController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;

/**
 *
 * @author Piotr Tekieli
 */
public class FaceOperation implements FaceDetectionStrategy{

    private File imageSrcFile;
    private File RSAfile;
    private ImageView imageResult;
    private Mat image;
    private FotoCryptGuiController controller;

    private EncryptionStrategy encryptionStrategy;
    private DecryptionStrategy decryptionStrategy;
    private OpenCVOperations openCVOperations;

    public FaceOperation(EncryptionStrategy encryptionStrategy, DecryptionStrategy decryptionStrategy, OpenCVOperations openCVOperations) {
        this.encryptionStrategy = encryptionStrategy;
        this.decryptionStrategy = decryptionStrategy;
        this.openCVOperations = openCVOperations;
    }

    public void detectFaces(boolean rsa) {
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());
        MatOfRect faceDetections = new MatOfRect();
        setCascadeFilter().detectMultiScale(image, faceDetections);
        ArrayList<FaceRectangle> faceRectangles = new ArrayList<>();
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        /* Apply anonimization filters */
        for (Rect rect : faceDetections.toArray()) {
            /*Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                new Scalar(0, 255, 0), 5);*/
            faceRectangles.add(new FaceRectangle(openCVOperations.BufferedImage2ByteArray(openCVOperations.Mat2BufferedImage(image.submat(rect)),
                    FilenameUtils.getExtension(imageSrcFile.getAbsolutePath())), rect.x, rect.y, rect.width, rect.height));
            Imgproc.GaussianBlur(image.submat(rect), image.submat(rect), new Size(55, 55), 55);
        }

        if (rsa == false) {
            encryptionStrategy.encryptImageRegions(faceRectangles, FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()));
        }
        else {

        }

        loadResultImage(openCVOperations.Mat2BufferedImage(image));
        Imgcodecs.imwrite(FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()) + ".encrypted." +
                   FilenameUtils.getExtension(imageSrcFile.getAbsolutePath()), image);
    }

    public void decryptFaces(boolean rsa) {
        // nie wiem ile z tego możesz wykorzystać ponownie, sam oceń i wydziel z funkcji
        if (rsa == false) {
            image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());
            EncryptionPack encryptionPack = decryptionStrategy.decryptImageRegions(
                    FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()) + ".json");
            Mat submat = null;
            for (FaceRectangle face : encryptionPack.faces) {
                Mat m = openCVOperations.BufferedImage2Mat(openCVOperations.ByteArray2BufferedImage(face.face));
                submat = image.submat(new Rect(face.x, face.y, face.width, face.height));
                m.copyTo(submat);
            }
            loadResultImage(openCVOperations.Mat2BufferedImage(image));
            Imgcodecs.imwrite(FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()) + ".original." +
                    FilenameUtils.getExtension(imageSrcFile.getAbsolutePath()), image);
        }
        else {

        }
    }

    private CascadeClassifier setCascadeFilter() {
        return new CascadeClassifier(new File("configuration/haarcascade_frontalface_alt.xml").getAbsolutePath());
    }

    private void loadResultImage(BufferedImage bufferedImage) {
        imageResult.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
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

    public void setRSAfile(File RSAfile) {
        this.RSAfile = RSAfile;
    }

    public FotoCryptGuiController getController() {
        return controller;
    }
}
