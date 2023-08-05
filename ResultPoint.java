package com.google.zxing;

import com.google.zxing.common.detector.MathUtils;

public class ResultPoint {

    /* renamed from: x */
    private final float f45x;

    /* renamed from: y */
    private final float f46y;

    public ResultPoint(float x, float y) {
        this.f45x = x;
        this.f46y = y;
    }

    public final float getX() {
        return this.f45x;
    }

    public final float getY() {
        return this.f46y;
    }

    public final boolean equals(Object other) {
        if (!(other instanceof ResultPoint)) {
            return false;
        }
        ResultPoint otherPoint = (ResultPoint) other;
        if (this.f45x == otherPoint.f45x && this.f46y == otherPoint.f46y) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return (Float.floatToIntBits(this.f45x) * 31) + Float.floatToIntBits(this.f46y);
    }

    public final String toString() {
        return "(" + this.f45x + ',' + this.f46y + ')';
    }

    public static void orderBestPatterns(ResultPoint[] patterns) {
        ResultPoint pointC;
        ResultPoint pointA;
        ResultPoint pointB;
        float zeroOneDistance = distance(patterns[0], patterns[1]);
        float oneTwoDistance = distance(patterns[1], patterns[2]);
        float zeroTwoDistance = distance(patterns[0], patterns[2]);
        if (oneTwoDistance >= zeroOneDistance && oneTwoDistance >= zeroTwoDistance) {
            pointB = patterns[0];
            pointA = patterns[1];
            pointC = patterns[2];
        } else if (zeroTwoDistance < oneTwoDistance || zeroTwoDistance < zeroOneDistance) {
            pointB = patterns[2];
            pointA = patterns[0];
            pointC = patterns[1];
        } else {
            pointB = patterns[1];
            pointA = patterns[0];
            pointC = patterns[2];
        }
        if (crossProductZ(pointA, pointB, pointC) < 0.0f) {
            ResultPoint temp = pointA;
            pointA = pointC;
            pointC = temp;
        }
        patterns[0] = pointA;
        patterns[1] = pointB;
        patterns[2] = pointC;
    }

    public static float distance(ResultPoint pattern1, ResultPoint pattern2) {
        return MathUtils.distance(pattern1.f45x, pattern1.f46y, pattern2.f45x, pattern2.f46y);
    }

    private static float crossProductZ(ResultPoint pointA, ResultPoint pointB, ResultPoint pointC) {
        float bX = pointB.f45x;
        float bY = pointB.f46y;
        return ((pointC.f45x - bX) * (pointA.f46y - bY)) - ((pointC.f46y - bY) * (pointA.f45x - bX));
    }
}
