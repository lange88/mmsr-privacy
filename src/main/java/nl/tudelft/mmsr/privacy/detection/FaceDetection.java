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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
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
public class FaceDetection {

    private File imageSrcFile;
    private ImageView imageResult;
    private Mat image;
    private FotoCryptGuiController controller;

    private FaceDetectionStrategy faceDetectionStrategy;
    private EncryptionStrategy encryptionStrategy;

    public FaceDetection(FaceDetectionStrategy faceDetectionStrategy, EncryptionStrategy encryptionStrategy) {
        this.faceDetectionStrategy = faceDetectionStrategy;
        this.encryptionStrategy = encryptionStrategy;
    }

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
                setImageSrcPath(file);

            } catch (IOException ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    public void detectFaces() {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        CascadeClassifier faceDetector = new CascadeClassifier(new File("configuration/haarcascade_frontalface_alt.xml").getAbsolutePath());
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        ArrayList<FaceRectangle> faceRectangles = new ArrayList<>();

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // paint rectangles for faces
        for (Rect rect : faceDetections.toArray()) {
            /*Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                new Scalar(0, 255, 0), 5);*/
            faceRectangles.add(new FaceRectangle(BufferedImage2ByteArray(Mat2BufferedImage(image.submat(rect)),
                    FilenameUtils.getExtension(imageSrcFile.getAbsolutePath())), rect.x, rect.y, rect.width, rect.height));
            Imgproc.GaussianBlur(image.submat(rect), image.submat(rect), new Size(55, 55), 55);
        }

        encryptionStrategy.encryptImageRegions(faceRectangles, FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()));

        loadResultImage(Mat2BufferedImage(image));
        Imgcodecs.imwrite(FilenameUtils.getBaseName(imageSrcFile.getAbsolutePath()) + ".encrypted." +
                        FilenameUtils.getExtension(imageSrcFile.getAbsolutePath()), image);
    }

    public void detectdecryptFaces() {
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());
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

    public BufferedImage Mat2BufferedImage(Mat m) {
        // Fastest code
        // output can be assigned either to a BufferedImage or to an Image

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public byte[] BufferedImage2ByteArray(BufferedImage image, String formatName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.flush();
            ImageIO.write(image, formatName, baos);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
