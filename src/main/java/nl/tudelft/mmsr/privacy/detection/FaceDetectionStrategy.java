package nl.tudelft.mmsr.privacy.detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;


/**
 * Created by Jeroen Vrijenhoef on 6/27/17.
 */
public interface FaceDetectionStrategy {

    /**
     * Function that does that actual face detection on an image
     * @return
     */
    void detectFaces(CascadeClassifier cascadeClassifier);
    void displayFaces(int correctionParam);
}
