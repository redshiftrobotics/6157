package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Eclipse Teleop")
public class Teleop extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;

    DcMotor armLiftBottomA;
    DcMotor armLiftBottomB;
    DcMotor armLiftTopA;
    DcMotor armLiftTopB;

    DcMotor intakeMotor;

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");

        this.armLiftBottomA = hardwareMap.dcMotor.get("bottom_a");
        this.armLiftBottomB = hardwareMap.dcMotor.get("bottom_b");
        this.armLiftTopA = hardwareMap.dcMotor.get("top_a");
        this.armLiftTopB = hardwareMap.dcMotor.get("top_b");

        this.intakeMotor = hardwareMap.dcMotor.get("intake");

        telemetry.addLine("Done.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            this.leftDrive.setPower(gamepad1.left_stick_y);
            this.rightDrive.setPower(gamepad1.right_stick_y);

            this.armLiftBottomA.setPower(gamepad2.left_stick_y);
            this.armLiftBottomB.setPower(gamepad2.left_stick_y);
            this.armLiftTopA.setPower(gamepad2.right_stick_y);
            this.armLiftTopB.setPower(gamepad2.right_stick_y);

            this.intakeMotor.setPower(gamepad2.left_trigger);

            idle();
        }
    }
}
