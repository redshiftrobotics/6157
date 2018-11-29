package org.firstinspires.ftc.teamcode;

public class encoderUnitConversion {
    static double wheelCircumference = 18.85;
    static int gRatio = 2;
    static int encRot = 1680;

    static private double rotCircleD = 13.625;//diameter of rotation circle
    static double rotCircleC = rotCircleD * Math.PI;//circumference of rotation circle

    int inchesToCounts(double inches) {
        return (int) (inches / wheelCircumference * gRatio * encRot);
    }

    int degreesToCounts(double degrees) {
        return (int) (degrees / 360 * rotCircleC / wheelCircumference * (gRatio * encRot));
    }
}
