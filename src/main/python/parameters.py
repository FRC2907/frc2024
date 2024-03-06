def hsv2cv(h, s, v):
  return (h/2, s*255, v*255)

def rgb2bgr(r, g, b):
  return (b, g, r)

kWidth = 320
kHeight = 240
kOrangeLow  = hsv2cv(0, 1/2, 1/4)
kOrangeHigh = hsv2cv(36, 1, 1)
kOranceLch = (68.86, 79.46, 1.11)
kOrangeLab = (70, 50, 75)
kOpenKernel = 3
kOpenIter = 3
kCloseKernel = 3
kCloseIter = 3
kAreaFilterFactor = (1/4)**4
kEnableNoteTracking = True
kBlackoutBackgroundFeed = False
kBlackoutNoteFeed = False
kEnableUndistort = True
kTargetLockFrameCountThreshold = 15
kMarkerColorNoLock = rgb2bgr(0, 255, 0)
kMarkerColorWithLock = rgb2bgr(255, 0, 0)
kLockMarkerSize = 0.10 # pct (0..1) of frame height
kMarkerThickness = 2 # px

#  Y+
#   \
#    A-----------B
#     \            \
#      \             \
#       \              \
#        \               \
#         C----------------D---X+
# TODO
kA = ( 0, 10)
kB = (10, 10)
kC = ( 0,  0)
kD = (10,  0)