package nl.tudelft.mmsr.privacy;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.tudelft.mmsr.privacy.detection.FaceOperation;
import nl.tudelft.mmsr.privacy.detection.FaceDetectionFactory;
import nl.tudelft.mmsr.privacy.gui.FotoCryptGui;

/**
 * Created by Jeroen Vrijenhoef on 6/25/17.
 */
public class Launcher extends Application {

    private FotoCryptGui fotoCryptGui;

    private FaceOperation FaceOperation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new Launcher().launchGui(primaryStage);
    }

    public void launchGui(Stage theStage) {
        FaceOperation = makeFaceDetection();
        fotoCryptGui = new FotoCryptGui(FaceOperation, theStage);

        theStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                theStage.close();
            }
        });
    }

    private FaceOperation makeFaceDetection() {
        FaceDetectionFactory factory = new FaceDetectionFactory();
        return factory.createFaceDetection();
    }
}
