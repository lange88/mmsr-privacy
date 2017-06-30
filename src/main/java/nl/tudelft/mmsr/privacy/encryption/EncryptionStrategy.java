package nl.tudelft.mmsr.privacy.encryption;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by jeroen on 6/27/17.
 */
public interface EncryptionStrategy {
    // not sure if returned object should be Mat as well in order to save it
    // with opencv
    ArrayList<byte[]> encryptImageRegions(ArrayList<BufferedImage> encryptionRegions);
}
