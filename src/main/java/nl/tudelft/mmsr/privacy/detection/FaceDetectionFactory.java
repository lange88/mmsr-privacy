package nl.tudelft.mmsr.privacy.detection;

import nl.tudelft.mmsr.privacy.encryption.AESDecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.AESEncryptionStrategy;

/**
 * Created by jeroen on 6/27/17.
 */
public class FaceDetectionFactory {

    public FaceDetection createFaceDetection() {

        // create the facedetection object with encryption and
        // detection strategies


        return new FaceDetection(new HaarCascadeStrategy(), new AESEncryptionStrategy(), new AESDecryptionStrategy());
    }
}
