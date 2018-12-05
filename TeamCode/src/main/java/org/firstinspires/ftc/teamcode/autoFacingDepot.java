package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
    //Cannot resolve symbol 'tfod' -- Could be fixed by; updating to 4.3, checking for presence of tfod library and adding if needed.


@Autonomous(name = "Autonomous Depot", group = "Things")
// @Disabled
public class autoFacingDepot extends LinearOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;
    Servo servo;
    DcMotor liftDrive;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "Acq/trz/////AAAAGXUOe/SqwE6QmoOrIXJe3S4va9eMDKZHt3tVhm6SIswWHx9Je8DgPAhpIK/mvU8vKs30ybIcUOPqYu738fQl/zQe0DKmzR0SxpqcpnH2VIiteHk8vjfmCuZQGX0PWcaJ/rgat+Fm999sc4UgPuyRo86DpJZ73ZVSZ7zpvyQgH5xkrj42cgFpcfPeMuQGgrqxf4+LT8FXV0NlLbXJzCIbniMEvyHiWd/YMbAO2x83oXa6bjZBUjZlCCUN+EhuKKQlCdfwxzN0aqQEHy8U1svpOPGd7SDp5FmIZsVdt089IGrcwDPYqkgg61sFL7PgaVQAffT6WeUrZq0xXg78FH5i1VYsVkETDu/I69lOdQXKjGlY";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    private MineralPosition position;

    enum MineralPosition {

        CENTER, LEFT, RIGHT //NOTFOUND

    }

    MethodyShit methodyShit;






    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.

        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");
        this.servo = hardwareMap.servo.get("servo");
        this.liftDrive = hardwareMap.dcMotor.get("lift_drive");
        this.methodyShit = new MethodyShit(hardwareMap);

        this.rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        telemetry.addLine("Done.");
        telemetry.update();

        int ranVuforia = 0;

        waitForStart();

        if (opModeIsActive()) {
            // Activate Tensor Flow Object Detection.
            if (tfod != null) {
                tfod.activate();
            }
            methodyShit.descendTheMast();

            while (opModeIsActive()) {


                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() >= 3) {//next to do: alter code to be more flexible with more than 3 objects and distinguish starting with size
                            int goldMineralX = -1; //should these be floats? (for increased accuracy)
                            int goldMineralWidth = 0; //privatize to scope?
                            int silverMineral1X = -1;
                            int silverMineral1Width = 0;
                            int silverMineral2X = -1;
                            int silverMineral2Width = 0;

                            for (Recognition recognition : updatedRecognitions) {//establishes the width and x position of the widest gold mineral and 2 widest silver minerals
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL) && recognition.getWidth() > goldMineralWidth) {//Compares width to previously identified gold object to identify which is closer (possibly should compare width*height?)
                                    goldMineralX = (int) recognition.getTop();//Camera is flipped with its top side on the left, so we use .getTop()
                                    goldMineralWidth = (int) recognition.getWidth();
                                } else if (recognition.getLabel().equals(LABEL_SILVER_MINERAL) && silverMineral1Width <= recognition.getWidth()) {
                                    silverMineral2X = silverMineral1X;
                                    silverMineral2Width = silverMineral1Width;
                                    silverMineral1X = (int) recognition.getTop();
                                    silverMineral1Width = (int) recognition.getWidth();
                                } else if (recognition.getLabel().equals(LABEL_SILVER_MINERAL) && silverMineral2Width <= recognition.getWidth()) {
                                    silverMineral2X = (int) recognition.getTop();
                                    silverMineral2Width = (int) recognition.getWidth();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Left");//not working for some reason
                                    position = MineralPosition.LEFT;
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    position = MineralPosition.RIGHT;
                                } else if ((goldMineralX > silverMineral1X && goldMineralX < silverMineral2X) || (goldMineralX < silverMineral1X && goldMineralX > silverMineral2X)) { //if it is inbetween
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    position = MineralPosition.CENTER;
                                } else {
                                    telemetry.addLine("Found 3+ objects, but could not determine position.");
//                                    position = MineralPosition.NOTFOUND;
                                }

                                telemetry.addData("X-position & Width", goldMineralX + " " + goldMineralWidth);
                            }
                        } else {
                            telemetry.addLine("Did not identify 3 or more objects.");
                        }
                        ranVuforia ++;
                        telemetry.addData("Vuforia ran", ranVuforia);
                        telemetry.update();
                    }


//                    telemetry.addLine("we have liftoff");
//                    telemetry.update();





                    if (position == MineralPosition.LEFT) {//left's current tune: needs improvement
                        telemetry.addData("Executing: Gold Mineral Position", "Left");
                        telemetry.update();

                        methodyShit.walkThePlank();

                        methodyShit.rotateRight(636, 1); //30 degrees

                        methodyShit.driveForeward(7843, 1); //44 inches

                        methodyShit.rotateRight(1568, 1); //74 degrees

                        methodyShit.driveForeward(5437, 1); //30.5 inches

                        methodyShit.placeTeamMarker();

//                        methodyShit.driveBack(15062, 1); //84.5 inches


                    } else if (position == MineralPosition.RIGHT) {//right's current tune: needs improvement
                        telemetry.addData("Executing: Gold Mineral Position", "Right");
                        telemetry.update();
                        methodyShit.walkThePlank();

                        methodyShit.rotateLeft(636, 1); //30 degrees

                        methodyShit.driveForeward(7843, 1); //44 inches

                        methodyShit.rotateLeft(1568, 1); //74 degrees

                        methodyShit.driveForeward(5437, 1); //30.5 inches

                        methodyShit.placeTeamMarker();

//                        methodyShit.rotateLeft(1907, 1); //90 degrees
//
//                        methodyShit.driveForeward(15062, 1); //84.5 inches


                    } else if (position == MineralPosition.CENTER) {//center's current tune: good enough!
                        telemetry.addData("Executing: Gold Mineral Position", "Center");
                        telemetry.update();
                        methodyShit.walkThePlank();

                        methodyShit.driveForeward(10800, 1); //60 inches (greater)

                        methodyShit.placeTeamMarker();

//                        methodyShit.driveForeward(350, 1);
//
//                        methodyShit.rotateRight(2754, 1); //137 degrees (needed to be greater)
//
//                        methodyShit.driveForeward(5062, 1); //84.5 inches
//
//                        methodyShit.rotateRight(500, 1);
//
//                        methodyShit.driveForeward(10000, 1);

                    } else if (ranVuforia == 15) {//abusing the vuforia loop counter as a timer
                        telemetry.addData("Executing: Gold Mineral Position", "Did not find, executing center");
                        telemetry.update();
                        methodyShit.walkThePlank();
                        methodyShit.driveForeward(10695, 1); //60 inches

                        methodyShit.placeTeamMarker();

//                        methodyShit.driveForeward(-450, 1);
//
//                        methodyShit.rotateLeft(2904, 1); //137 degrees
//
//                        methodyShit.driveForeward(15062, 1); //84.5 inches
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