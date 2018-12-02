package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Before Auto Lift")
public class beforeAutoLift extends LinearOpMode {
    DcMotor liftDrive;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing...");
        telemetry.update();

        this.liftDrive = hardwareMap.dcMotor.get("lift_drive");
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        telemetry.addLine("Done. Press start to lift robot.");
        telemetry.update();

        waitForStart();
        double timeSinceStart = runtime.time();
        telemetry.addData("Time since start is", timeSinceStart);
        telemetry.update();
        double timePassed = 0;

        while (opModeIsActive()) {
            timePassed = runtime.time() - timeSinceStart;
            if (timePassed <= 10) {
                liftDrive.setPower(1);
                telemetry.addData("Lifting... seconds to go", 10 - timePassed);
                telemetry.update();
            } else {
                liftDrive.setPower(0);
                telemetry.addLine("Done. Press stop to end program.");
                telemetry.update();
                idle();
            }
        }
        if (timePassed <= 10) {
            telemetry.addLine("ERROR: Motor moved less than 10 seconds, may not be in correct position.");
        }
        telemetry.addLine("Done.");
        telemetry.update();
    }

}
