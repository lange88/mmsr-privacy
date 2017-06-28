package nl.tudelft.mmsr.privacy.encryption;

import javafx.scene.image.Image;
import org.opencv.core.MatOfRect;

/**
 * Created by jeroen on 6/27/17.
 */
public interface EncryptionStrategy {
    Image encryptImageRegions(Image image, MatOfRect rect);
}
