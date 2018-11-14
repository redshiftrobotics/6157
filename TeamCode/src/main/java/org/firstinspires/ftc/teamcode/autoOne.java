package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Eclipse autoOne")
public class autoOne extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;

    @Override
    public void runOpMode() {
        telemetry.addLine("Inintializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");

        telemetry.addLine("Done.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (position = left) {

            }
            
            if (position = right) {

            }

            if (position = center) {

            }

        }
    }
}
