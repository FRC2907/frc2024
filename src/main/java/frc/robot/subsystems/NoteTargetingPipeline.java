package frc.robot.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;

// https://docs.wpilib.org/en/stable/docs/software/vision-processing/roborio/using-the-cameraserver-on-the-roborio.html

public class NoteTargetingPipeline implements Runnable {

    private CvSink cvSink;
    private CvSource outputStream;
    private Mat mat;
    public NoteTargetingPipeline(int w, int h) {
        CameraServer
            .startAutomaticCapture()
            .setResolution(w, h)
            ;
        this.cvSink = CameraServer.getVideo();
        this.outputStream = CameraServer.putVideo("Note Targeting", w, h);
        this.mat = new Mat();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (this.cvSink.grabFrame(mat) == 0) {
                outputStream.notifyError(cvSink.getError());
                continue;
            }

            Imgproc.rectangle(
                mat
                , new Point(100, 100)
                , new Point(400, 400)
                , new Scalar(255, 255, 255)
                , 5
            );
            outputStream.putFrame(mat);
        }
    }
}
