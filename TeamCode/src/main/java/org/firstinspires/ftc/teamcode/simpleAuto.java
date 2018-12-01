package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Eclipse Auto")
public class simpleAuto extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor hevenlyLight;



    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");
        this.hevenlyLight = hardwareMap.dcMotor.get("lift_drive");

        this.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);


        telemetry.addLine("Done.");
        telemetry.update();

        

        waitForStart();

        while (opModeIsActive()) {
            this.leftDrive.setPower(gamepad1.left_stick_y);
            this.rightDrive.setPower(gamepad1.right_stick_y);


            idle();
        }
    }
}
