
package frc.robot.commands;

import frc.robot.VisionHelper;
import frc.robot.subsystems.Turret;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.vision.VisionThread;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.core.*;
import java.util.ArrayList;

public class AlignShooter extends CommandGroup {
    public Mat img = new Mat(), prevImg = new Mat();
    public int errorx, errory;

    public double P = 0.01; // tune this
    public double I = 0.0;
    public double D = 0.0003;

    // this is the number of loops to run the PID while there is no contour
    // detected, before we halt the PID. Each loop is 0.02s
    public int noiseLoopThreshold = 15;
    public int numNoiseLoops = 0; // a "noise loop" is a loop during which there's no contour detected
    public boolean noiseHalt = false;

    public int cameraX = Robot.imgWidth;
    public int targetX;
    public double voltage = 0;

    public static double integral, previous_error, derivative = 0;
    public double dt = 0.02;
    public double ff = 0.02; // correct to these 2 significant digits, using the setup on 2/28/20
    public double maxVoltage = 0.08 + ff; // conservative; would set the max around [0.4, 0.6]

    public double threshold = 5; // pixel error required before stopping

    public Turret turret = Robot.m_turret;

    public int[] findCenter(MatOfPoint contour) {
        // [x,y]
        int[] centerCoor = null;
        if(contour!= null)
        {
            centerCoor = new int[2];
            Rect boundingRect = Imgproc.boundingRect(contour);

            // centerCoor[0] grabs the centerX of the boundingRect
            // centerCoor[0] grabs the topY of the boundingRect, since we want this
            centerCoor[0] = (int) (boundingRect.x + (boundingRect.width / 2.0));
            centerCoor[1] = (int) (boundingRect.y);
            return centerCoor;
        }
        return centerCoor;
    }

    public AlignShooter() {
        
        //this.img = img;
    }

    protected void initialize() {
        numNoiseLoops = 0;
        noiseHalt = false;
        System.out.println("began turning");
    }

    protected void execute() {
        SmartDashboard.putNumber("numContours", Robot.pipeline.filterContoursOutput().size());
        //System.out.println("This is running");
        Robot.img.copyTo(img);
        if (img.equals(prevImg)) {
            //Robot.outputStream.notifyError(Robot.cvSink.getError());
            if(numNoiseLoops >= noiseLoopThreshold){
                noiseHalt = true;
            }
            else{
                numNoiseLoops++;
                SmartDashboard.putNumber("numNoiseLoops", numNoiseLoops);
            }
        }
        else  
        {
            prevImg = img;
            //MatOfPoint contour = VisionHelper.getLargestContour(img);
            ArrayList<MatOfPoint> contours = (ArrayList<MatOfPoint>) Robot.pipeline.filterContoursOutput().clone();
            MatOfPoint contour = null;
            if(contours.size()>0)
            {
                int index = 0;
                int largestArea = 0;
                for (int i = 0; i < contours.size(); i++) {
                    Rect boundingRect = Imgproc.boundingRect(contours.get(i));
                    if (boundingRect.width * boundingRect.height > largestArea) {
                        index = i;
                        largestArea = boundingRect.width * boundingRect.height;
                    }
                    contour = contours.get(index);
                }
            }
            if(contour != null)
            {
                numNoiseLoops = 0;
                SmartDashboard.putNumber("numNoiseLoops", numNoiseLoops);

                // this is the "center" of the CONTOUR, not of the image
                // it's also not actually the center, as the x is the center, but y is the top
                int[] center = findCenter(contour); 

                // if contour center is to the right, errorx > 0. Voltage will then be positive,
                // and the turret will turn right towards the target
                errorx = center[0] - Robot.imgWidth / 2;
                errory = center[1] - Robot.imgHeight / 2;
                if (Math.abs(errorx) > threshold) {
                    // The condition if error = 0 is being checked in visionController, thats why you dont need it here
                    // this turn vertical command is only being called when the condition ^ is false
                    integral += (errorx * dt); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
                    derivative = (errorx - previous_error) / dt;
                    previous_error = errorx; //keep updating error to the most recently measured one
                    double voltage = (P * errorx + I * integral + D * derivative);
                    voltage += (voltage > 0 ? ff : -ff);
                    if (Math.abs(voltage) >= maxVoltage) {
                        voltage = Math.signum(voltage) * maxVoltage;
                    }
                    
                    SmartDashboard.putNumber("turretVoltage", voltage);
                    SmartDashboard.putNumber("errorx", errorx);
                    SmartDashboard.putNumber("numNoiseLoops", numNoiseLoops);
                    Robot.m_turret.rotate(voltage); // must be Robot.m_turret, not the turret variable declared above
                }
            }
            else{
                if(numNoiseLoops >= noiseLoopThreshold){
                    noiseHalt = true;
                }
                else{
                    numNoiseLoops++;
                    SmartDashboard.putNumber("numNoiseLoops", numNoiseLoops);
                }
            }
        }
    }

    protected boolean isFinished() {
        return (Math.abs(errorx) < threshold) || noiseHalt;
    }

    protected void end() {
        /*Robot.m_turret.rotate(0);
        MatOfPoint visionTarget = VisionHelper.getLargestContour(img);
        if(visionTarget != null)
        {
            double distance = VisionHelper.averageVisionDistance(visionTarget);
            double shooterSpeed = VisionHelper.getShooterSpeed(distance);
        }
        System.out.println("Done turning");*/

        //addSequential(new StopTurning());
        //addSequential(new Shoot(getDistMeter(), Robot.heightOuterPort));
    }

    protected void interrupted() {
    }
}
