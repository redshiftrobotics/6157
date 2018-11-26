package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.util.Position;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
    //Cannot resolve symbol 'tfod' -- Could be fixed by; updating to 4.3, checking for presence of tfod library and adding if needed.

@Autonomous(name = "Autonomous Crater", group = "Things")
// @Disabled
public class autoFacingCreater extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "Acq/trz/////AAAAGXUOe/SqwE6QmoOrIXJe3S4va9eMDKZHt3tVhm6SIswWHx9Je8DgPAhpIK/mvU8vKs30ybIcUOPqYu738fQl/zQe0DKmzR0SxpqcpnH2VIiteHk8vjfmCuZQGX0PWcaJ/rgat+Fm999sc4UgPuyRo86DpJZ73ZVSZ7zpvyQgH5xkrj42cgFpcfPeMuQGgrqxf4+LT8FXV0NlLbXJzCIbniMEvyHiWd/YMbAO2x83oXa6bjZBUjZlCCUN+EhuKKQlCdfwxzN0aqQEHy8U1svpOPGd7SDp5FmIZsVdt089IGrcwDPYqkgg61sFL7PgaVQAffT6WeUrZq0xXg78FH5i1VYsVkETDu/I69lOdQXKjGlY";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    private MineralPosition position;

    enum MineralPosition {

        CENTER, LEFT, RIGHT

    }

    public void resetEncoders() {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    int leftPosition = leftDrive.getCurrentPosition();
    int rightPosition = rightDrive.getCurrentPosition();

    public void waitToFinish() {
        while (opModeIsActive() && (leftDrive.isBusy() || rightDrive.isBusy())) {
            telemetry.addData("Left Encoder Position", leftPosition);
            telemetry.addData("Right Encoder Position", rightPosition);
            telemetry.update();
            idle();
        }
    }

    public void rotateLeft(int targetPosition, double power) {
        resetEncoders();
        leftDrive.setTargetPosition(targetPosition);
        leftDrive.setPower(power);
        rightDrive.setTargetPosition(-targetPosition);
        rightDrive.setPower(power);
        waitToFinish();
    }

    public void rotateRight(int targetPosition, double power) {
        resetEncoders();
        leftDrive.setTargetPosition(-targetPosition);
        leftDrive.setPower(power);
        leftDrive.setTargetPosition(targetPosition);
        leftDrive.setPower(power);
        waitToFinish();
    }

    public void driveForeward(int targetPosition, double power) {
        resetEncoders();
        leftDrive.setTargetPosition(targetPosition);
        leftDrive.setPower(power);
        rightDrive.setTargetPosition(targetPosition);
        rightDrive.setPower(power);
        waitToFinish();
    }

    public void driveToMarkerAndBack() {

        rotateLeft(1714, -0.2); //60 degrees

        driveForeward(7679, -0.5); //43 inches

        rotateLeft(2143, -0.2); //75 degrees

        driveForeward(11786, -0.75); //66 inches

        //placeTeamMarker():

        driveForeward(11786, 0.75); //66 inches, power is positive to indicate reverse

        rotateRight(2143, -0.2); //75 degrees

        driveForeward(7679, 0.5); //43 inches, power is positive to indicate reverse

        rotateRight(1714, -0.2); //60 degrees
    }






    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.

        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");

        this.rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        telemetry.addLine("Done.");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Activate Tensor Flow Object Detection.
            if (tfod != null) {
                tfod.activate();
            }

            while (opModeIsActive()) {


                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Left");
                                    position = org.firstinspires.ftc.teamcode.autoFacingCreater.MineralPosition.LEFT;
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    position = org.firstinspires.ftc.teamcode.autoFacingCreater.MineralPosition.RIGHT;
                                } else {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    position = org.firstinspires.ftc.teamcode.autoFacingCreater.MineralPosition.CENTER;
                                }
                            }
                        }
                        telemetry.update();
                    }





                    if (position == org.firstinspires.ftc.teamcode.autoFacingCreater.MineralPosition.LEFT) {

                        driveToMarkerAndBack();

                        rotateLeft(857, -0.2); //30 degrees

                        driveForeward(5268, -0.5); //29.5 inches

                        rotateRight(2400, -0.2); //84 degrees

                        driveForeward(3214, 0.5); //18 inches


                    } else if (position == org.firstinspires.ftc.teamcode.autoFacingCreater.MineralPosition.RIGHT) {

                        driveToMarkerAndBack();

                        rotateRight(857, -0.2); //30 degrees

                        driveForeward(5268, -0.5); //29.5 inches

                        rotateLeft(2400, -0.2); //84 degrees

                        driveForeward(3214, -0.5); //18 inches

                    } else { //mineralPosition.CENTER or not found

                        driveToMarkerAndBack();

                        driveForeward(6429, 0.5); //36 inches



                    }



                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
