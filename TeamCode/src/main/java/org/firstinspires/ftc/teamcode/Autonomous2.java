package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "Autonomous2")
public class Autonomous2 extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;

    DcMotor armLiftBottomA;
    DcMotor armLiftBottomB;
    DcMotor armLiftTopA;
    DcMotor armLiftTopB;

    DcMotor intakeMotor;
    public ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");
        //Q: is this important? (below)
        this.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        this.armLiftBottomA = hardwareMap.dcMotor.get("bottom_a");
        this.armLiftBottomB = hardwareMap.dcMotor.get("bottom_b");
        this.armLiftTopA = hardwareMap.dcMotor.get("top_a");
        this.armLiftTopB = hardwareMap.dcMotor.get("top_b");

        this.intakeMotor = hardwareMap.dcMotor.get("intake");

        telemetry.addLine("Done.");
        telemetry.update();


        while (getRuntime()< 3) {
            //makes robot move, but how does it stop? Need:rotation count

            this.leftDrive.setPower(.5);
            this.rightDrive.setPower(.5);

            //this.armLiftBottomA.setPower(gamepad2.left_stick_y);
            //this.armLiftBottomB.setPower(gamepad2.left_stick_y);
            //this.armLiftTopA.setPower(gamepad2.right_stick_y);
            //this.armLiftTopB.setPower(gamepad2.right_stick_y);

            //this.intakeMotor.setPower(gamepad2.left_trigger);
            //this.intakeMotor.setPower(-gamepad2.right_trigger);

            idle();
        }

        waitForStart();
    }
}
