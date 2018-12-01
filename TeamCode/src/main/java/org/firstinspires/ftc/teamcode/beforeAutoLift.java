package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Before Auto Lift")
public class beforeAutoLift extends LinearOpMode {
    DcMotor liftDrive;



    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing...");
        telemetry.update();

        this.liftDrive = hardwareMap.dcMotor.get("lift_drive");
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Done.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

        }
    }

}
