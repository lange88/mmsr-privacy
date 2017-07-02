package nl.tudelft.mmsr.privacy;

import javafx.scene.image.ImageView;
import nl.tudelft.mmsr.privacy.detection.FaceDetection;
import nl.tudelft.mmsr.privacy.detection.FaceDetectionFactory;
import nl.tudelft.mmsr.privacy.detection.FaceDetectionStrategy;
import nl.tudelft.mmsr.privacy.detection.HaarCascadeStrategy;
import nl.tudelft.mmsr.privacy.encryption.AESDecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.AESEncryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.DecryptionStrategy;
import nl.tudelft.mmsr.privacy.encryption.EncryptionStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;

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

    /**
     * Effectively also does face detection, so total time also depends on this.
     */
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

        long tStart = System.currentTimeMillis();

        // loop through all the test images (450 total)
        Iterator it = FileUtils.iterateFiles(new File(testDir), null, false);
        while(it.hasNext()){
            File image = (File) it.next();

            detection.setImageSrcPath(image);
            detection.setImageResult(new ImageView());
            detection.detectFaces();
        }

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        double avgSeconds = elapsedSeconds / 450.0;

        System.out.println("Total time elapsed: " + elapsedSeconds + " avg " + avgSeconds + " seconds.");
    }

    /**
     * Only finds the faces, does no encryption
     */
    @Test
    public void testFaceDetectionSpeed() {
        String testDir = System.getProperty("user.dir") +  "/src/test/java/resources/images_single_face";

        FaceDetectionStrategy strategy = new HaarCascadeStrategy();

        int processed_images = 0;
        int total_images = 0;
        int detected_faces = 0;
        int total_faces = 450;
        int total_wrong_detections = 0; // we know each image should only contain 1 face

        long tStart = System.currentTimeMillis();

        // loop through all the test images
        Iterator it = FileUtils.iterateFiles(new File(testDir), null, false);
        while(it.hasNext()){
            File imageFile = (File) it.next();
            Mat image = Imgcodecs.imread(imageFile.getAbsolutePath());

            MatOfRect faceDetections = strategy.detectFaces(image);

            int numberOfFaces = faceDetections.toArray().length;
            if (numberOfFaces == 0) {
                total_wrong_detections += 1;
            }
            else if (numberOfFaces > 1) {
                total_wrong_detections += numberOfFaces;
            }
        }

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        double avgSeconds = elapsedSeconds / 450.0;

        System.out.println("Total time elapsed: " + elapsedSeconds + " avg " + avgSeconds + " seconds\n" +
                "Wrong detections: " + total_wrong_detections);
    }
}
