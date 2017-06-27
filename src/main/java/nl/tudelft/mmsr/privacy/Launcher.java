package nl.tudelft.mmsr.privacy;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.tudelft.mmsr.privacy.detection.FaceDetection;
import nl.tudelft.mmsr.privacy.detection.FaceDetectionFactory;
import nl.tudelft.mmsr.privacy.gui.FotoCryptGui;

/**
 * Created by Jeroen Vrijenhoef on 6/25/17.
 */
public class Launcher extends Application {

    private FotoCryptGui fotoCryptGui;

    private FaceDetection faceDetection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new Launcher().launchGui(primaryStage);
    }

    public void launchGui(Stage theStage) {
        faceDetection = makeFaceDetection();
        fotoCryptGui = new FotoCryptGui(faceDetection, theStage);

        theStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                theStage.close();
            }
        });
    }

    private FaceDetection makeFaceDetection() {
        FaceDetectionFactory factory = new FaceDetectionFactory();
        return factory.createFaceDetection();
    }
}
