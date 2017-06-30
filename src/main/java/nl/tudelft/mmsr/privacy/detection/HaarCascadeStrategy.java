package nl.tudelft.mmsr.privacy.detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

/**
 * Created by jeroen on 6/27/17.
 */
public class HaarCascadeStrategy implements FaceDetectionStrategy {

    @Override
    public MatOfRect detectFaces(Mat image) {
        CascadeClassifier faceDetector = new CascadeClassifier(new File("configuration/haarcascade_frontalface_alt_tree.xml").getAbsolutePath());

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        return faceDetections;
    }
}
