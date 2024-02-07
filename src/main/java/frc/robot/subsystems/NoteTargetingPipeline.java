package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import frc.robot.constants.Control;

// https://docs.wpilib.org/en/stable/docs/software/vision-processing/roborio/using-the-cameraserver-on-the-roborio.html

public class NoteTargetingPipeline implements Runnable {

    private int w, h;
    private CvSink cvSink;
    private CvSource outputStream;
    private Mat i_color, i_mask, m_tmp;
    private List<MatOfPoint> pts;
    private Scalar drawColor;
    private Comparator<MatOfPoint> sizeSorter;
    private Predicate<MatOfPoint> sizeFilter;

    private Scalar orangeLow, orangeHigh;

    public NoteTargetingPipeline(int w, int h, Scalar orangeLow, Scalar orangeHigh) {
        this.w = w; this.h = h;
        CameraServer
            .startAutomaticCapture()
            .setResolution(w, h)
            ;
        this.cvSink = CameraServer.getVideo();
        this.outputStream = CameraServer.putVideo("Note Targeting", w, h);
        this.i_color = new Mat();
        this.i_mask = new Mat();
        this.pts = new ArrayList<>();
        this.m_tmp = new Mat();
        this.drawColor = new Scalar(0, 255, 0);
        this.sizeSorter = (a, b) -> {
            double aa = Imgproc.contourArea(a);
            double bb = Imgproc.contourArea(b);
            if (aa == bb) return 0;
            if (aa > bb) return -1; // sort descending
            return 1;
        };
        this.sizeFilter = (contour) -> {
            // stuff at the bottom should be bigger to pass the filter
            double y = Imgproc.boundingRect(contour).y;
            // true means "remove this item from consideration"
            return Imgproc.contourArea(contour) < w*y*Control.camera.kAreaFilterFactor;
        };

        this.orangeLow = orangeLow;
        this.orangeHigh = orangeHigh;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (this.cvSink.grabFrame(i_color) == 0) {
                outputStream.notifyError(cvSink.getError());
                continue;
            }

            Imgproc.cvtColor(i_color, m_tmp, Imgproc.COLOR_BGR2HSV);
            Core.inRange(m_tmp, orangeLow, orangeHigh, i_mask);
            //Core.bitwise_not(i_mask, m_tmp);
            //i_color.setTo(new Scalar(0, 0, 0), m_tmp);

            pts.clear();
            Imgproc.findContours(i_mask, pts, m_tmp, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            pts.removeIf(sizeFilter);
            pts.sort(sizeSorter);

            if (pts.size() > 0) {
                Rect bound = Imgproc.boundingRect(pts.get(0));
                Imgproc.rectangle(i_color, bound, drawColor);
                double line_y = bound.y + bound.height / 2;
                double line_x = bound.x + bound.width / 2;
                Point note_ctr = new Point(line_x, line_y);
                Point screen_ctr = new Point(this.w / 2, line_y);
                Imgproc.line(i_color, note_ctr, screen_ctr, drawColor, 3);

                RotatedRect ell = Imgproc.fitEllipseDirect(i_mask);
                //Imgproc.fitEllipse(null)
                Point[] verts = new Point[4];
                ell.points(verts);
                for (int i = 0; i < 4; i++)
                    Imgproc.line(i_color, verts[i], verts[(i+1)%4], drawColor);

                // TODO report off-centeredness somehow, maybe to NT
            }

            outputStream.putFrame(i_color);
        }
    }
}
