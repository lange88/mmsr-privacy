package nl.tudelft.mmsr.privacy.detection;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ptekieli on 7/2/17.
 */
public class OpenCVOperations {
    public BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b);
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public byte[] BufferedImage2ByteArray(BufferedImage image, String formatName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.flush();
            ImageIO.write(image, formatName, baos);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public BufferedImage ByteArray2BufferedImage(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Mat BufferedImage2Mat(BufferedImage myBufferedImage)
    {
        byte[] data = ((DataBufferByte) myBufferedImage.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(myBufferedImage.getHeight(), myBufferedImage.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }
}
