package nl.tudelft.mmsr.privacy.detection;

import nl.tudelft.mmsr.privacy.encryption.AESDecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.AESEncryptionStrategy;

/**
 * Created by jeroen on 6/27/17.
 */
public class FaceDetectionFactory {

    public FaceOperation createFaceDetection() {
        return new FaceOperation(new AESEncryptionStrategy(), new AESDecryptionStrategy(), new OpenCVOperations());
    }
}
