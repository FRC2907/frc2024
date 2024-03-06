#!/usr/bin/python

"""
camera calibration for distorted images with chess board samples
pulls frames from video feed, calculates calibration, writes constants to file

usage:
    calibrate.py [-w <width>] [-h <height>] [-c <camera index>] [-n frame count] [-o filename]

usage example:
    calibrate.py -w 9 -h 6 -c 1 -n 100 -o myCamera

default values:
    -w: 9
    -h: 6
    -c: 0
    -n: 10
    -o: distortion
"""

import numpy as np
import cv2 as cv


def main():
    import sys
    import getopt

    args, _ = getopt.getopt(sys.argv[1:], "w:h:c:n:o:")
    args = dict(args)
    args.setdefault("-w", 9)
    args.setdefault("-h", 6)
    args.setdefault("-c", 0)
    args.setdefault("-f", 10)
    args.setdefault("-o", "distortion")

    width = int(args.get("-w"))
    height = int(args.get("-h"))
    camera_id = int(args.get("-c"))
    desired_frame_count = int(args.get("-f"))
    filename = str(args.get("-o"))

    pattern_size = (width, height)
    pattern_points = np.zeros((np.prod(pattern_size), 3), np.float32)
    pattern_points[:, :2] = np.indices(pattern_size).T.reshape(-1, 2)
    # pattern_points *= square_size #unsure whether need this for something

    obj_points = []
    img_points = []

    cap = cv.VideoCapture(camera_id)
    if not cap.isOpened():
        print("Cannot open camera")
        return

    ret, frame = cap.read()
    h, w, _ = frame.shape

    def processFrame(img):
        found = False
        corners = 0
        found, corners = cv.findChessboardCorners(img, pattern_size)
        if found:
            term = (cv.TERM_CRITERIA_EPS + cv.TERM_CRITERIA_COUNT, 30, 0.1)
            cv.cornerSubPix(img, corners, (5, 5), (-1, -1), term)
            frame_img_points = corners.reshape(-1, 2)
            frame_obj_points = pattern_points
            return (frame_img_points, frame_obj_points)
        print("pattern not found")
        return None

    chessboards = []
    while len(chessboards) < desired_frame_count:
        for _ in range(10):  # delay
            ret, frame = cap.read()
        if not ret:
            print("Failed to recv frame, dying")
            return
        gray = cv.cvtColor(frame, cv.COLOR_BGR2GRAY)
        cv.imshow("frame", gray)
        out = processFrame(gray)
        if out != None:
            chessboards.append(out)
            print(f"{len(chessboards)}/{desired_frame_count} boards detected ({100*len(chessboards)/desired_frame_count})%")
        if cv.waitKey(1) == ord("q"):
            break

    for corners, pattern_points in chessboards:
        img_points.append(corners)
        obj_points.append(pattern_points)

    # calculate camera distortion
    print(f"Calculating distortion from {len(chessboards)} images...")
    rms, camera_matrix, dist_coefs, _rvecs, _tvecs = cv.calibrateCamera(
        obj_points, img_points, (w, h), None, None
    )
    print("Done!")

    print("\nRMS:", rms)
    print("camera matrix:\n", camera_matrix)
    print("distortion coefficients: ", dist_coefs.ravel())

    np.savez(filename, K=camera_matrix, D=dist_coefs)
    print(f"saved to file: {filename}")


    cap.release()
    print("Done")


if __name__ == "__main__":
    print(__doc__)
    main()
    cv.destroyAllWindows()
