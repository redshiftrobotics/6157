package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Eclipse Teleop")
public class Teleop extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor liftDrive;//Make sure to add this back on the phone!

//    DcMotor armLiftBottomA;
//    DcMotor armLiftBottomB;
//    DcMotor armLiftTopA;
//    DcMotor armLiftTopB;
//
//    DcMotor intakeMotor;

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");
        this.liftDrive = hardwareMap.dcMotor.get("lift_drive");//thanks for this name
//        this.rightDrive2 = hardwareMap.dcMotor.get("right_drive_2");

        this.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);//Joysticks are reversed (in theory, haven't checked) so the left drive has to be reversed while the joysticks negate the reversed motors
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState;
        boolean ludicrousMode = true;
        double speedMultiplier = 1;
        

//        this.armLiftBottomA = hardwareMap.dcMotor.get("bottom_a");
//        this.armLiftBottomB = hardwareMap.dcMotor.get("bottom_b");
//        this.armLiftTopA = hardwareMap.dcMotor.get("top_a");
//        this.armLiftTopB = hardwareMap.dcMotor.get("top_b");
//
//        this.intakeMotor = hardwareMap.dcMotor.get("intake");

        telemetry.addLine("Done.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            bCurrState = (gamepad1.right_trigger >= 0.75);

            // check for button state transitions.
            if (bCurrState && (bCurrState != bPrevState))  {

                // button is transitioning to a pressed state. So Toggle LED
                ludicrousMode = !ludicrousMode;
                if (ludicrousMode) {
                    speedMultiplier = 1;
                    telemetry.addLine("Ludicrous mode enabled.");
                } else {
                    speedMultiplier = 0.5;
                    telemetry.addLine("Ludicrous mode disabled.");
                }
                telemetry.update();
            }

            // update previous state variable.
            bPrevState = bCurrState;

            this.leftDrive.setPower(gamepad1.left_stick_y * speedMultiplier);
            this.rightDrive.setPower(gamepad1.right_stick_y * speedMultiplier);

            this.liftDrive.setPower(gamepad2.right_stick_y);

//            this.armLiftBottomA.setPower(gamepad2.left_stick_y);
//            this.armLiftBottomB.setPower(gamepad2.left_stick_y);
//            this.armLiftTopA.setPower(gamepad2.right_stick_y);
//            this.armLiftTopB.setPower(gamepad2.right_stick_y);
//
//            this.intakeMotor.setPower(gamepad2.left_trigger);

            idle();
        }
    }
}
