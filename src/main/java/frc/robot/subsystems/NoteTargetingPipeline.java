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
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.constants.MechanismConstraints;

// https://docs.wpilib.org/en/stable/docs/software/vision-processing/roborio/using-the-cameraserver-on-the-roborio.html

public class NoteTargetingPipeline implements Runnable, ISubsystem {

    private int w, h;
    private CvSink cvSink;
    private CvSource outputStream;
    private Mat i_color, i_mask, m_tmp;
    private List<MatOfPoint> pts;
    private Scalar drawColor;
    private Comparator<MatOfPoint> sizeSorter;
    private Predicate<MatOfPoint> sizeFilter;
    private Scalar orangeLow, orangeHigh;

    private double dx, y;
    private DoublePublisher p_dx, p_y;

    public NoteTargetingPipeline() {
        this.w = MechanismConstraints.camera.kWidth;
        this.h = MechanismConstraints.camera.kHeight;
        CameraServer
                .startAutomaticCapture()
                .setResolution(w, h);
        if (MechanismConstraints.camera.kNoteTrackingEnabled) {
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
                if (aa == bb)
                    return 0;
                if (aa > bb)
                    return -1; // sort descending
                return 1;
            };
            // true means "remove this item from consideration"
            // stuff at the bottom should be bigger to pass the filter
            this.sizeFilter = (contour) -> {
                return false; // for now let's just allow everything
                //double y = Imgproc.boundingRect(contour).y;
                //return Imgproc.contourArea(contour) < w * y * Control.camera.kAreaFilterFactor;
            };

            this.orangeLow = MechanismConstraints.camera.kOrangeLow;
            this.orangeHigh = MechanismConstraints.camera.kOrangeHigh;

            this.dx = 0;
            this.y = 0;
            NetworkTable nt = NetworkTableInstance.getDefault().getTable("note-tracking");
            this.p_dx = nt.getDoubleTopic("dx").publish();
            this.p_y = nt.getDoubleTopic("y").publish();
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted())
            onLoop();

    }

    public static Transform2d pixelToTranslation(double dx, double y) {
        return new Transform2d(); // TODO implement: map dx (how far off center) and y (how far up the image) to a translation on the field
    }
    public static Pose2d pixelToPose(Pose2d robot, double dx, double y) {
        return robot.transformBy(pixelToTranslation(dx, y));
    }

    @Override
    public void onLoop() {
        if (MechanismConstraints.camera.kNoteTrackingEnabled)
            runNoteTracking();
    }

    private void runNoteTracking() {
        if (cvSink.grabFrame(i_color) == 0) {
            outputStream.notifyError(cvSink.getError());
            return;
        }

        Imgproc.cvtColor(i_color, m_tmp, Imgproc.COLOR_BGR2HSV);
        Core.inRange(m_tmp, orangeLow, orangeHigh, i_mask);
        Core.bitwise_not(i_mask, m_tmp);
        i_color.setTo(new Scalar(0, 0, 0), m_tmp);

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
            Point screen_ctr = new Point(w / 2, line_y);
            Imgproc.line(i_color, note_ctr, screen_ctr, drawColor, 3);

            //RotatedRect ell = Imgproc.fitEllipseDirect(i_mask);
            //Imgproc.fitEllipse(null)
            //Point[] verts = new Point[4];
            //ell.points(verts);
            //for (int i = 0; i < 4; i++)
            //    Imgproc.line(i_color, verts[i], verts[(i + 1) % 4], drawColor);

            dx = (line_x / w) - 0.5;
            y = line_y / h;
            submitTelemetry();
        }

        outputStream.putFrame(i_color);
    }

    @Override
    public void submitTelemetry() {
        if (MechanismConstraints.camera.kNoteTrackingEnabled) {
            p_dx.set(dx);
            p_y.set(y);
        }
    }

    @Override
    public void receiveOptions() {
        // not used atm
    }
}