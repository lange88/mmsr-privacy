package nl.tudelft.mmsr.privacy.detection;

import java.awt.image.BufferedImage;

/**
 * Created by ptekieli on 6/30/17.
 */
public class FaceRectangle {
    public byte[] face;
    public int x, y, width, height;

    public FaceRectangle(byte[] face, int x, int y, int width, int height) {
        this.face = face;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
