package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;



@Autonomous(name = "Autonomous Two", group = "Things")
// @Disabled
public class autoTwo extends LinearOpMode {

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

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.

        telemetry.addLine("Initializing...");
        telemetry.update();

        this.leftDrive = hardwareMap.dcMotor.get("left_drive");
        this.rightDrive = hardwareMap.dcMotor.get("right_drive");

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

            while (opModeIsActive()) {//Additional to do: perhaps make this (the recognition) a separate class/function so that it can be called from both autonomous scripts


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
                                    telemetry.addData("Gold Mineral Position", "Left");
                                    position = MineralPosition.LEFT;
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    position = MineralPosition.RIGHT;
                                } else {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    position = MineralPosition.CENTER;
                                }
                                telemetry.addData("X-position & Width", goldMineralX + " " + goldMineralWidth);
                            } else {
                                telemetry.addLine("More objects needed to identify position.");
                            }
                        }
                        telemetry.update();//extra credit: use visual coordinates (goldMineralX) to help robot direct itself towards the gold mineral or, for extra extra credit, *explicitly* identify the gold mineral object and track it for the duration (rather than using its reference position once to execute preplanned maneuvers)
                    }

                    if (position == MineralPosition.LEFT) {

                    } else if (position == MineralPosition.RIGHT) {

                    } else {

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