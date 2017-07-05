/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.mmsr.privacy.detection;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import nl.tudelft.mmsr.privacy.encryption.DecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.EncryptionPack;
import nl.tudelft.mmsr.privacy.encryption.EncryptionStrategy;
import nl.tudelft.mmsr.privacy.gui.FotoCryptGuiController;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Piotr Tekieli
 */
public class FaceOperation implements FaceDetectionStrategy{

    private File imageSrcFile;
    private File RSAfile;
    private ImageView imageResult;
    private Mat image;
    private MatOfRect faceDetections;
    private double correctionParamTempWidth = 0;
    private double correctionParamTempHeight = 0;
    private ArrayList<FaceRectangle> faceRectangles = new ArrayList<>();
    private FotoCryptGuiController controller;

    private EncryptionStrategy encryptionStrategy;
    private DecryptionStrategy decryptionStrategy;
    private OpenCVOperations openCVOperations;

    public FaceOperation(EncryptionStrategy encryptionStrategy, DecryptionStrategy decryptionStrategy, OpenCVOperations openCVOperations) {
        this.encryptionStrategy = encryptionStrategy;
        this.decryptionStrategy = decryptionStrategy;
        this.openCVOperations = openCVOperations;
    }

    public void detectFaces(CascadeClassifier cascadeClassifier) {
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());
        faceDetections = new MatOfRect();
        cascadeClassifier.detectMultiScale(image, faceDetections);
    }

    public void encryptFaces(String filter) {
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());
        for (Rect rect : faceDetections.toArray()) {
            rect.x =  rect.x - (int)(correctionParamTempWidth / 2);
            rect.y = rect.y - (int)(correctionParamTempHeight / 2);
            rect.width = rect.width + (int)(correctionParamTempWidth);
            rect.height = rect.height + (int)(correctionParamTempHeight);

            faceRectangles.add(new FaceRectangle(openCVOperations.BufferedImage2ByteArray(openCVOperations.Mat2BufferedImage(image.submat(rect)),
                    FilenameUtils.getExtension(imageSrcFile.getAbsolutePath())), rect.x, rect.y, rect.width, rect.height));

            switch(filter) {
                case "gaussianblur" :
                    OpenCVFilters selFilter = new OpenCVFilters();
                    selFilter.GaussianBlur(image.submat(rect), new Size(55, 55), 55);
                    break;
                case "erode" :
                    OpenCVFilters erodeFilter = new OpenCVFilters(6);
                    erodeFilter.Erode(image.submat(rect));
                    break;
                case "dilate" :
                    OpenCVFilters dilateFilter = new OpenCVFilters(6);
                    dilateFilter.Dilate(image.submat(rect));
                    break;
                default :
                    break;
            }
        }

        encryptionStrategy.encryptImageRegions(faceRectangles, FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()), RSAfile);

        loadResultImage(openCVOperations.Mat2BufferedImage(image));
        Imgcodecs.imwrite(FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()) + ".encrypted." +
                FilenameUtils.getExtension(imageSrcFile.getAbsolutePath()), image);
    }

    public void displayFaces(int correctionParam) {
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());
        for (Rect rect : faceDetections.toArray()) {
            correctionParamTempWidth = correctionParam * rect.width / 100;
            correctionParamTempHeight = correctionParam * rect.height / 100;
            Imgproc.rectangle(image,
                    new Point(rect.x - (correctionParamTempWidth / 2), rect.y - (correctionParamTempHeight / 2)),
                    new Point(rect.x + rect.width + (correctionParamTempWidth / 2), rect.y + rect.height + (correctionParamTempHeight / 2)),
                    new Scalar(0, 255, 0), 5);
        }

        loadResultImage(openCVOperations.Mat2BufferedImage(image));
    }

    public void decryptFaces() {
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());
        EncryptionPack encryptionPack = decryptionStrategy.decryptImageRegions(
        FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()) + ".json", RSAfile);
        Mat submat = null;
        for (FaceRectangle face : encryptionPack.faces) {
            Mat m = openCVOperations.BufferedImage2Mat(openCVOperations.ByteArray2BufferedImage(face.face));
            submat = image.submat(new Rect(face.x, face.y, face.width, face.height));
            m.copyTo(submat);
        }
        loadResultImage(openCVOperations.Mat2BufferedImage(image));
        Imgcodecs.imwrite(FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()) + ".decrypted." +
            FilenameUtils.getExtension(imageSrcFile.getAbsolutePath()), image);
    }

    public CascadeClassifier setCascadeClassifier(String pathname) {
        return new CascadeClassifier(new File(pathname).getAbsolutePath());
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
