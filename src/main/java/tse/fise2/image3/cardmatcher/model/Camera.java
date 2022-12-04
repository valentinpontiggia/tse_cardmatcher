package tse.fise2.image3.cardmatcher.model;



import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class Camera{

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture();
    // a flag to change the button behavior
    private boolean cameraActive = false;
    // the id of the camera to be used
    private static int cameraId = 0;
    // learning mode
    private boolean learningmode;
    //testing mode
    private boolean testingmode;
    private String PictureName ="classname";

    //
    private Label label = new Label();
    //
    private Mat frame = new Mat();
    private ImageView imagedetection;

    public void openCamera(ImageView crframe, Button btn) {
        // TODO Autogenerated
        if (!this.cameraActive)
        {
            // start the video capture
            this.capture.open(cameraId);

            // is the video stream available?
            if (this.capture.isOpened())
            {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run()
                    {
                        // grab and process a single frame
                        frame = grabFrame();
                        if (learningmode) {
                            // rectangle in the frame
                            Imgproc.rectangle(frame,new Point(100, 80), new Point(440, 600), new Scalar(0, 0, 255), 2);
                        }
                        if(testingmode){
                            // rectangle in the frame
                            Imgproc.rectangle(frame,new Point(100, 80), new Point(440, 600), new Scalar(0,255, 0), 2);


                        }

                        // convert and show the frame
                        MatOfByte buffer1 = new MatOfByte();
                        Imgcodecs.imencode(".jpg", frame, buffer1);
                        // à revoir car pas trop bien compris
                        Image imageToShow = new Image(new ByteArrayInputStream(buffer1.toArray()));
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                crframe.setImage(imageToShow);
                            }
                        });


                    }
                };
                // à revoir aussi pour bien comprendre
                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
                if (this.learningmode || this.testingmode) {
                    btn.setText("Capture");
                }

                // close webcam when no capture taken
                Stage stage =(Stage)(btn.getScene().getWindow());
                stage.setOnCloseRequest((new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we)
                    {
                        stopAcquisition();
                    }
                }));
            }
            else
            {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
        }
        else
        {
            // the camera is not active at this point
            this.cameraActive = false;

            // stop the timer
            this.stopAcquisition();
            if (this.learningmode || this.testingmode) {
                // update again the button content
                btn.setText("Restart Camera");

                // Name the capture and save it in a folder
                this.showInputTextDialog();
                AddImageDetection(crframe);
                System.out.println(this.label.getText());
            }
        }
    }

    public void AddImageDetection(ImageView  det_frame)
    {
        imagedetection = det_frame;
    }

    public void setTestingmode(boolean testingmode) {
        this.testingmode = testingmode;
    }


    public abstract void saveImage( ) ;

    private void showInputTextDialog() {
        if(learningmode) {
            TextInputDialog dialog = new TextInputDialog("Write here");
            dialog.setTitle("Save picture");
            dialog.setHeaderText("Enter the name of the picture ");
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                this.label.setText(name);
                this.saveImage();
            });
        }else if(testingmode)
        {
            Alert a = new Alert(Alert.AlertType.NONE,
                    "This  card belongs to class X", ButtonType.OK);


            // show the dialog
            a.show();
            this.saveImage();
            InputStream stream = null;
            try {
                stream = new FileInputStream("/Users/mac/Desktop/projet_informatique/apprentissage/images.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image image = new Image(stream);
            imagedetection.setImage(image);


        }

    }

    private Mat grabFrame() {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    //mettre en nuances de gris
                    //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR5652RGB);
                }
            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }
        return frame;

    }

    public Label getLabel() {
        return label;
    }

    public Mat getFrame() {
        return frame;
    }

    public void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
                this.capture.release();
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }
        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    public boolean isCameraActive() {
        return cameraActive;
    }

    public void setCameraActive(boolean cameraActive) {
        this.cameraActive = cameraActive;
    }

    public boolean isLearningmode() {
        return learningmode;
    }

    public void setLearningmode(boolean learningmode) {
        this.learningmode = learningmode;
    }

    public String getPictureName() {
        return PictureName;
    }
}

