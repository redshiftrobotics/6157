package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.util.Position;



@Autonomous(name = "autonomous")
public class autonomous extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;

//    DcMotor armLiftBottomA;
//    DcMotor armLiftBottomB;
//    DcMotor armLiftTopA;
//    DcMotor armLiftTopB;
//
//    DcMotor intakeMotor;

    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");
        //Q: is this important? (below)
        //Otherwise the drive controls for the joysticks are fucked.
        this.rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        this.armLiftBottomA = hardwareMap.dcMotor.get("bottom_a");
//        this.armLiftBottomB = hardwareMap.dcMotor.get("bottom_b");
//        this.armLiftTopA = hardwareMap.dcMotor.get("top_a");
//        this.armLiftTopB = hardwareMap.dcMotor.get("top_b");
//
//        this.intakeMotor = hardwareMap.dcMotor.get("intake");
//          We don't have these on our bot anymore

        telemetry.addLine("Done.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            //makes robot move, but how does it stop? Need:rotation count
            // This could be *Encoders* (A piece of hardware that you put on your wheels that can
            //help you control for things like battery power etc. OR PID... --- Alek is looking into this
//            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            int leftPosition = leftDrive.getCurrentPosition();
            int rightPosition = rightDrive.getCurrentPosition();

            telemetry.addData("Left Encoder Position", leftPosition);
            telemetry.addData("Right Encoder Position", rightPosition);

            leftDrive.setTargetPosition(10286);
            leftDrive.setPower(-0.5);
            rightDrive.setTargetPosition(-10286);
            rightDrive.setPower(-0.5);
            //10000 is 56 in
            //1000 is 35 degrees

            //wait(until leftPosition = X);

            telemetry.addData("Left Encoder Position", leftPosition);
            telemetry.addData("Right Encoder Position", rightPosition);
            telemetry.update();



            //this.armLiftBottomA.setPower(gamepad2.left_stick_y);
            //this.armLiftBottomB.setPower(gamepad2.left_stick_y);
            //this.armLiftTopA.setPower(gamepad2.right_stick_y);
            //this.armLiftTopB.setPower(gamepad2.right_stick_y);

            //this.intakeMotor.setPower(gamepad2.left_trigger);
            //this.intakeMotor.setPower(-gamepad2.right_trigger);

            idle();
        }
    }
}
