#!/usr/bin/python

import cv2 as cv
import numpy as np

from parameters import *

"""
-c camera id
-f filename of distortion parameters
"""


def main():
    import sys
    import getopt

    args, _ = getopt.getopt(sys.argv[1:], "c:f:")
    args = dict(args)
    args.setdefault("-c", 0)
    args.setdefault("-f", "distortion.npz")

    camera_id = int(args.get("-c"))
    filename = str(args.get("-f"))

    with np.load(filename) as mats:
        K = mats["K"]
        D = mats["D"]

    cap = cv.VideoCapture(camera_id)
    if not cap.isOpened():
        print("Cannot open camera")
        return

    newK = None
    map1, map2 = None, None
    while True:
        if cv.waitKey(1) == ord("q"):
            break

        ret, frame = cap.read()
        if not ret:
            print("Failed to receive frame")

        h, w, _ = frame.shape
        if newK is None:
            newK, _ = cv.getOptimalNewCameraMatrix(K, D, (w, h), 0, (w, h))
        if map1 is None or map2 is None:
            map1, map2 = cv.initUndistortRectifyMap(
                K, D, None, newK, (w, h), cv.CV_16SC2
            )
        
        if kEnableUndistort:
          frame = cv.remap(frame, map1, map2, cv.INTER_LINEAR)
        #cv.imshow("undistorted", out)
        # TODO stream as mjpeg
        # TODO process note tracking
        # TODO publish note tracking info to NT

        if kEnableNoteTracking:
          hsv = cv.cvtColor(frame, cv.COLOR_BGR2HSV)
          i_mask = None
          frame = cv.inRange(hsv, kOrangeLow, kOrangeHigh, i_mask)
          if kBlackoutNoteFeed:
            cv.bitwise_not(i_mask, hsv)
          pts = None
          contours, _ = cv.findContours(i_mask, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
          contours = sorted(contours, key=lambda c: cv.contourArea(c))
          # TODO filter by size
          if len(contours) > 0:
            # TODO process contour for this cycle

            ###
            #        pts.removeIf(sizeFilter);
            #        pts.sort(sizeSorter);
            #
            #        if (pts.size() > 0) {
            #            targetLockFrameCount += 1;
            #            Rect bound = Imgproc.boundingRect(pts.get(0));
            #            Imgproc.rectangle(i_color, bound, drawColor);
            #            double line_y = bound.y + bound.height / 2;
            #            double line_x = bound.x + bound.width / 2;
            #            Point note_ctr = new Point(line_x, line_y);
            #            Point screen_ctr = new Point(w / 2, line_y);
            #            Imgproc.line(i_color, note_ctr, screen_ctr, drawColor, 3);
            #
            #            //RotatedRect ell = Imgproc.fitEllipseDirect(i_mask);
            #            //Imgproc.fitEllipse(null)
            #            //Point[] verts = new Point[4];
            #            //ell.points(verts);
            #            //for (int i = 0; i < 4; i++)
            #            //    Imgproc.line(i_color, verts[i], verts[(i + 1) % 4], drawColor);
            #
            #            x = line_x / w;
            #            dx = x - 0.5;
            #            y = line_y / h;
            #
            #        } else targetLockFrameCount = 0;
            #
            #        outputStream.putFrame(i_color);
            ###

    cap.release()

if __name__ == "__main__":
    print(__doc__)
    main()
    cv.destroyAllWindows()