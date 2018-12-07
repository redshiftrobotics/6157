package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MethodyShit {

    DcMotor leftDrive;//0
    DcMotor rightDrive;//1
    Servo servo;
    DcMotor liftDrive;//3

    HardwareMap hardwareMap;

    public MethodyShit(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");
        this.servo = hardwareMap.servo.get("servo");
        this.liftDrive = hardwareMap.dcMotor.get("lift_drive");

        this.rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);//IMPORTANT: MAY NEEED TO REVERSE leftDrive2 AS WELL!
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void resetEncoders() {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);//Again, need encoders to do this for leftDrive2
    }

//    int targetPosition = 0;
//    double power = 0;

//    int leftPosition = leftDrive.getCurrentPosition();
//    int rightPosition = rightDrive.getCurrentPosition();

    public void waitToFinish() {
        while ((leftDrive.isBusy() || rightDrive.isBusy())) {
//            telemetry.addData("Left Encoder Position", leftPosition);
//            telemetry.addData("Right Encoder Position", rightPosition);
//            telemetry.update();
            continue;
        }
    }

    public void placeTeamMarker() {
        servo.setPosition(0.35);
    }

    public void rotateLeft(int targetPosition, double power) {
        resetEncoders();
        leftDrive.setTargetPosition(-targetPosition/4);
        leftDrive.setPower(power);
        rightDrive.setTargetPosition(targetPosition/4);
        rightDrive.setPower(power);
        waitToFinish();
    }

    public void rotateRight(int targetPosition, double power) {
        resetEncoders();
        leftDrive.setTargetPosition(targetPosition/4);
        leftDrive.setPower(power);
        rightDrive.setTargetPosition(-targetPosition/4);
        rightDrive.setPower(power);
        waitToFinish();
    }

    public void driveForeward(int targetPosition, double power) {
        resetEncoders();
        leftDrive.setTargetPosition(targetPosition/4);
        leftDrive.setPower(power);
        rightDrive.setTargetPosition(targetPosition/4);
        rightDrive.setPower(power);
        waitToFinish();
    }

    public void driveBack(int targetPosition, double power) {
        resetEncoders();
        leftDrive.setTargetPosition(-targetPosition/4);
        leftDrive.setPower(power);
        rightDrive.setTargetPosition(-targetPosition/4);
        rightDrive.setPower(power);
        waitToFinish();
    }

    public void driveToMarkerAndBack() {//* = using calculated values

        driveForeward(2852, 0.5); //16 inches

        rotateLeft(1272, 0.5); //*60 degrees

        driveForeward(7665, 0.5); //*43 inches

        rotateLeft(1590, 0.5); //*75 degrees

        driveForeward(11764, 0.5); //*66 inches

        placeTeamMarker();

        driveBack(11764, 0.5); //*66 inches

        rotateRight(1590, 0.5); //*75 degrees

        driveBack(7665, 0.5); //*43 inches

        rotateRight(1272, 0.5); //*60 degrees
    }

    public void descendTheMast() throws InterruptedException {
        liftDrive.setPower(-1);
        Thread.sleep(10000);
        liftDrive.setPower(0);
        driveBack(1069, 0.5); //.5 inches
    }

    public void walkThePlank() {
        rotateLeft(954, 0.5); //45 degrees
        driveForeward(1069, 0.5); //.5 inches
        rotateLeft(954, 0.5); //45 degrees
        driveBack(1069, 0.5); //.5 inches
    }

}
