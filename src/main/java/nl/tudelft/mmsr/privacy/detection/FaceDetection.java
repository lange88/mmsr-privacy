/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.mmsr.privacy.detection;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import nl.tudelft.mmsr.privacy.encryption.EncryptionStrategy;
import nl.tudelft.mmsr.privacy.gui.FotoCryptGuiController;
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

    public void saveImage() {

    }

    public void detectFaces() {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        CascadeClassifier faceDetector = new CascadeClassifier(new File("configuration/haarcascade_frontalface_alt.xml").getAbsolutePath());
        image = Imgcodecs.imread(imageSrcFile.getAbsolutePath());

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        //ArrayList<Mat> regions = new ArrayList<>();
        ArrayList<BufferedImage> regions= new ArrayList<>();
 
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // paint rectangles for faces
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                new Scalar(0, 255, 0), 5);
            regions.add(Mat2BufferedImage(image.submat(rect)));
        }

        ArrayList<byte[]> effects = encryptionStrategy.encryptImageRegions(regions);
        Mat mat = Imgcodecs.imdecode(new MatOfByte(effects.get(0)), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

        for (Rect rect : faceDetections.toArray()) {
            mat.copyTo(image.rowRange(rect.x, rect.x+rect.width).colRange(rect.y, rect.y+rect.height));
        }

        Imgcodecs.imwrite("test.png", mat);
        /*
        libpng warning: Image width is zero in IHDR
        libpng warning: Image height is zero in IHDR
        libpng error: Invalid IHDR data
        */

        imageResult.setImage(SwingFXUtils.toFXImage(Mat2BufferedImage(image), null));

        String filename = "ouput.png";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);


        //loadResultImage(new File(filename));
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
}
