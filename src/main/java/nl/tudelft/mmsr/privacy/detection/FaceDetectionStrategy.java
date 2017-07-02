package nl.tudelft.mmsr.privacy.detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;


/**
 * Created by Jeroen Vrijenhoef on 6/27/17.
 */
public interface FaceDetectionStrategy {

    /**
     * Function that does that actual face detection on an image
     * @return
     */
    void detectFaces(boolean rsa);
}
