package nl.tudelft.mmsr.privacy.encryption;

import javafx.scene.image.Image;
import nl.tudelft.mmsr.privacy.detection.FaceRectangle;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by jeroen on 6/27/17.
 */
public interface EncryptionStrategy {
    void encryptImageRegions(ArrayList<FaceRectangle> faceRectangles, String fileName, File RSAfile);

}
