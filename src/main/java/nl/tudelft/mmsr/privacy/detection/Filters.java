package nl.tudelft.mmsr.privacy.detection;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ptekieli on 7/5/17.
 */
public class Filters {

    int erosion_size = 6;
    Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS,
            new Size(2 * erosion_size + 1, 2 * erosion_size + 1),
            new Point(erosion_size, erosion_size));

    public Filters() {}

    public Filters(int erosion_size) {
        this.erosion_size = erosion_size;
    }

    public void GaussianBlur(Mat subMatrix, Size sizeField, int sigmax) {
        Imgproc.GaussianBlur(subMatrix, subMatrix, sizeField, sigmax);
    }

    public void Erode(Mat subMatrix) {
        Imgproc.erode(subMatrix, subMatrix, element);
    }

    public void Dilate(Mat subMatrix) {
        Imgproc.dilate(subMatrix, subMatrix, element);
    }


}
