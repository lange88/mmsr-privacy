package nl.tudelft.mmsr.privacy;

import javafx.scene.image.ImageView;
import nl.tudelft.mmsr.privacy.detection.FaceDetection;
import nl.tudelft.mmsr.privacy.detection.FaceDetectionFactory;
import nl.tudelft.mmsr.privacy.detection.HaarCascadeStrategy;
import nl.tudelft.mmsr.privacy.encryption.AESDecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.AESEncryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.DecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.EncryptionStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;

import java.io.File;
import java.util.Iterator;

/**
 * Created by Jeroen Vrijenhoef on 7/2/17.
 */
public class BenchmarkingTest {

    private FaceDetectionFactory factory;

    @Before
    public void setUp() {
        factory = new FaceDetectionFactory();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void testEncryptionSpeedAES() {
        String testDir = System.getProperty("user.dir") +  "/src/test/java/resources/images_single_face";

        FaceDetection detection = factory.createFaceDetection(
                new HaarCascadeStrategy(),
                new AESEncryptionStrategy(),
                new AESDecryptionStrategy());

        int processed_images = 0;
        int total_images = 0;
        int detected_faces = 0;
        int total_faces = 450;

        // loop through all the test images
        Iterator it = FileUtils.iterateFiles(new File(testDir), null, false);
        while(it.hasNext()){
            File image = (File) it.next();

            detection.setImageSrcPath(image);
            detection.setImageResult(new ImageView());
            detection.detectFaces();
        }
    }
}
