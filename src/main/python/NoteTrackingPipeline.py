#!/usr/bin/python

import cv2 as cv
import numpy as np

from parameters import *

"""
-c camera id
-f filename of distortion parameters
"""


def lerp(a, t, b):
    return (a * (1 - t)) + (b * t)


def lerpp(a1, a2, t, b1, b2):
    return lerp(a1, t, b1), lerp(a2, t, b2)


def lerp2d(A, B, C, D, x, y):
    return lerpp(*lerpp(*A, x, *B), y, *lerpp(*C, x, *D))


def publishImage(frame):
    # TODO stream as mjpeg
    cv.imshow("frame", frame)
    pass


def publishData(point, targetLock):
    # TODO publish note tracking info to NT
    if targetLock:
        print(point)
    pass


def hsvThresh(frame):
    i_color = cv.cvtColor(frame, cv.COLOR_BGR2HSV)
    i_mask = cv.inRange(i_color, kOrangeLow, kOrangeHigh)
    i_mask = cv.morphologyEx(
        i_mask, cv.MORPH_OPEN, np.ones((kOpenKernel, kOpenKernel)), iterations=kOpenIter
    )
    i_mask = cv.morphologyEx(
        i_mask,
        cv.MORPH_CLOSE,
        np.ones((kCloseKernel, kCloseKernel)),
        iterations=kCloseIter,
    )
    return i_mask


def contourMethod(frame, targetLockFrameCount):
    out = frame
    robotSpacePoint = (0, 0)

    h, w, _ = frame.shape
    i_mask = hsvThresh(frame)
    mmmask = cv.merge([i_mask, i_mask, i_mask])

    contours, _ = cv.findContours(i_mask, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
    contours = [c for c in contours if cv.contourArea(c) > (kAreaFilterFactor * w * h)]
    contours = sorted(contours, key=lambda c: cv.contourArea(c))

    if len(contours) > 0:
        targetLockFrameCount += 1

        c = contours[-1]
        rect = cv.boundingRect(c)
        rx, ry, rw, rh = rect
        center = (rx + rw / 2, ry + rh / 2)
        cx, cy = center
        robotSpacePoint = lerp2d(kA, kB, kC, kD, cx / w, cy / h)

        if kBlackoutBackgroundFeed:
            out = cv.bitwise_and(out, mmmask)
        if kBlackoutNoteFeed:
            out = cv.bitwise_and(out, 255 - mmmask)

        out = cv.rectangle(out, rect, kMarkerColorNoLock, kMarkerThickness)
        if targetLockFrameCount >= kTargetLockFrameCountThreshold:
            lockSymbol = np.array(
                [
                    [cx + h * kLockMarkerSize / 2, cy],
                    [cx, cy + h * kLockMarkerSize / 2],
                    [cx - h * kLockMarkerSize / 2, cy],
                    [cx, cy - h * kLockMarkerSize / 2],
                ],
                np.int32,
            ).reshape((-1, 1, 2))
            out = cv.polylines(
                out, [lockSymbol], True, kMarkerColorWithLock, kMarkerThickness
            )
    else:
        targetLockFrameCount = 0
    return out, robotSpacePoint, targetLockFrameCount


def demoThresh(frame):
    return hsvThresh(frame), (0, 0)


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
    targetLockFrameCount = 0
    robotSpacePoint = (0, 0)

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

        if kEnableNoteTracking:
            frame, robotSpacePoint, targetLockFrameCount = contourMethod(
                frame, targetLockFrameCount
            )

        publishData(
            point=robotSpacePoint,
            targetLock=targetLockFrameCount >= kTargetLockFrameCountThreshold,
        )
        publishImage(frame)

    cap.release()


if __name__ == "__main__":
    main()
    cv.destroyAllWindows()