#!/usr/bin/python

def lerp(a, t, b):
    return (a * (1 - t)) + (b * t)


def lerpp(a1, a2, t, b1, b2):
    return lerp(a1, t, b1), lerp(a2, t, b2)


def lerp2d(A, B, C, D, x, y):
    return lerpp(*lerpp(*A, x, *B), y, *lerpp(*C, x, *D))
