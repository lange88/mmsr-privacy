/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.mmsr.privacy.detection;

import java.io.File;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Piotr Tekieli
 */
public class FaceDetection {
        
        public void DetectFaces(File faceImage) {
            System.out.println(System.getProperty("java.library.path"));
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            CascadeClassifier faceDetector = new CascadeClassifier(new File("configuration/haarcascade_frontalface_alt.xml").getAbsolutePath());
            Mat toProcess = Highgui.imread(faceImage.getAbsolutePath()); 
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(toProcess, faceDetections);
 
            System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
 
            for (Rect rect : faceDetections.toArray()) {
                Core.rectangle(toProcess, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }
 
        String filename = "ouput.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, toProcess);
        }
       
}
