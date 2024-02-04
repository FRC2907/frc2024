package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;

// https://docs.wpilib.org/en/stable/docs/software/vision-processing/roborio/using-the-cameraserver-on-the-roborio.html

public class NoteTargetingPipeline implements Runnable {

    private int w, h;
    private CvSink cvSink;
    private CvSource outputStream;
    private Mat mat, u_mat;
    private List<MatOfPoint> pts;
    private Scalar drawColor;
    private Comparator<MatOfPoint> sizeSorter;

    private Scalar orangeLow, orangeHigh;

    public NoteTargetingPipeline(int w, int h) {
        this.w = w; this.h = h;
        CameraServer
            .startAutomaticCapture()
            .setResolution(w, h)
            ;
        this.cvSink = CameraServer.getVideo();
        this.outputStream = CameraServer.putVideo("Note Targeting", w, h);
        this.mat = new Mat();
        this.pts = new ArrayList<>();
        this.u_mat = new Mat();
        this.drawColor = new Scalar(180, 255, 255); // it's grayscale sorry
        this.sizeSorter = (a, b) -> {
            double aa = Imgproc.contourArea(a);
            double bb = Imgproc.contourArea(b);
            if (aa == bb) return 0;
            if (aa > bb) return -1; // sort descending
            return 1;
        };

        this.orangeLow = new Scalar(4, 127, 127);
        this.orangeHigh = new Scalar(16, 255, 255);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (this.cvSink.grabFrame(mat) == 0) {
                outputStream.notifyError(cvSink.getError());
                continue;
            }

            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);
            Core.inRange(mat, orangeLow, orangeHigh, mat);

            pts.clear();
            Imgproc.findContours(mat, pts, u_mat, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            pts.sort(sizeSorter);

            if (pts.size() > 0) {
                Rect bound = Imgproc.boundingRect(pts.get(0));
                //Imgproc.rectangle(mat, bound, drawColor);
                double line_y = bound.y + bound.height / 2;
                double line_x = bound.x + bound.width / 2;
                Point note_ctr = new Point(line_x, line_y);
                Point screen_ctr = new Point(this.w / 2, line_y);
                Imgproc.line(mat, note_ctr, screen_ctr, drawColor, 3);

                // TODO report off-centeredness somehow, maybe to NT
            }

            outputStream.putFrame(mat);
        }
    }
}
