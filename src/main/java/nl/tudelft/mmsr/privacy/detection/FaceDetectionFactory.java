package nl.tudelft.mmsr.privacy.detection;

import nl.tudelft.mmsr.privacy.encryption.AESDecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.AESEncryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.DecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.EncryptionStrategy;

/**
 * Created by jeroen on 6/27/17.
 */
public class FaceDetectionFactory {

    public FaceDetection createFaceDetection() {

        // create the facedetection object with encryption and
        // detection strategies


        return new FaceDetection(new HaarCascadeStrategy(), new AESEncryptionStrategy(), new AESDecryptionStrategy());
    }

    public FaceDetection createFaceDetection(FaceDetectionStrategy detectionStrategy,
                                             EncryptionStrategy encryptionStrategy,
                                             DecryptionStrategy decryptionStrategy) {

        return new FaceDetection(detectionStrategy, encryptionStrategy, decryptionStrategy);
    }
}
