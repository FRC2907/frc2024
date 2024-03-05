#!/usr/bin/python

import cv2 as cv
import numpy as np

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
        
        out = cv.remap(frame, map1, map2, cv.INTER_LINEAR)
        cv.imshow("undistorted", out)

    cap.release()

if __name__ == "__main__":
    print(__doc__)
    main()
    cv.destroyAllWindows()
