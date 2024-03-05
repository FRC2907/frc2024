package frc.robot.constants;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Range;
import org.opencv.core.Size;

@Deprecated
public class CalibrationConstants {
  public final Mat cameraMatrix;
  public final Mat distCoeffs;

  public CalibrationConstants(
      double fx
    , double cx
    , double fy
    , double cy
    , double d1
    , double d2
    , double d3
    , double d4
    , double d5
  ) {
    this(new MatOfDouble(fx, 0, cx, 0, fy, cy, 0, 0, 1).reshape(1, new int[] {3,3}), new MatOfDouble(d1, d2, d3, d4, d5));
  }

  public CalibrationConstants(Mat cameraMatrix, Mat distCoeffs) {
    this.cameraMatrix = cameraMatrix;
    this.distCoeffs = distCoeffs;
  }

  public Mat[] getUndistortMaps(int w, int h) {
    Size frameSize = new Size(w, h);
    Mat newCameraMatrix = Calib3d.getOptimalNewCameraMatrix(cameraMatrix, distCoeffs, frameSize, 1, frameSize);

    Mat K = cameraMatrix;
    Mat D = distCoeffs.rowRange(new Range(0, 4));
    Mat R = new Mat(); // one camera, default to identity
    Mat P = newCameraMatrix;
    System.out.println(K);
    System.out.println(D);
    System.out.println(P);
    Mat out1 = new Mat(), out2 = new Mat();
    Calib3d.fisheye_initUndistortRectifyMap(K, D, R, P, frameSize, CvType.CV_16SC2, out1, out2);
    Calib3d.initUndistortRectifyMap(K, D, R, P, frameSize, CvType.CV_16SC2, out1, out2);
    return new Mat[]{out1, out2};
  }

  public static final CalibrationConstants CameraAtest = new CalibrationConstants(
    648.5
    , 360
    , 648
    , 260
    , -0.36
    , 0.07
    , 0.0009
    , 0.001
    , 0.1
  );
  public static final CalibrationConstants CameraA = new CalibrationConstants(
 648.06442631
		, 365.20478336
		, 648.27425979
		, 261.31362841
		, -0.37921246
		, 0.07881152
		, -0.00088953
		, 0.00097307
		, 0.1209214
  );
}
