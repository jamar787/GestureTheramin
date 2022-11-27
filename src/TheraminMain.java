import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math;
import java.util.Timer;
import java.util.TimerTask;

import com.leapmotion.leap.*;

class TheraminListener extends Listener {
    TheraminController theraminController;

    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        theraminController.processMotion(frame);
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                    + ", palm position: " + hand.palmPosition());
            if(hand.isLeft()){
                for(Finger finger :hand.fingers()){
                    System.out.println(" " + finger.type() + ", id: " + finger.id() + " extended: " + finger.isExtended());
                }
            }
        }
        System.out.println("Frame id: " + frame.id()
                + ", timestamp: " + frame.timestamp()
                + ", hands: " + frame.hands().count()
                + ", fingers: " + frame.fingers().count());
        // Get arm bone

        //Get hands
       /* for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                    + ", palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                    + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                    + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

            // Get arm bone
            Arm arm = hand.arm();
            System.out.println("  Arm direction: " + arm.direction()
                    + ", wrist position: " + arm.wristPosition()
                    + ", elbow position: " + arm.elbowPosition());

            // Get fingers
            for (Finger finger : hand.fingers()) {
                System.out.println("    " + finger.type() + ", id: " + finger.id()
                        + ", length: " + finger.length()
                        + "mm, width: " + finger.width() + "mm");

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    System.out.println("      " + bone.type()
                            + " bone, start: " + bone.prevJoint()
                            + ", end: " + bone.nextJoint()
                            + ", direction: " + bone.direction());
                }
            }
        }*/

        if (!frame.hands().isEmpty()) {
            System.out.println();
        }
    }

    public void addListener(TheraminController theraminController){
        this.theraminController = theraminController;
    }
}

class TherListener{
    private TheraminController theraminController;
    private Controller controller;
    class FrameTimer extends TimerTask{

        @Override
        public void run() {
            Frame frame = controller.frame();
            System.out.println("Frame id: " + frame.id()
                    + ", timestamp: " + frame.timestamp()
                    + ", hands: " + frame.hands().count());
            theraminController.processMotion(frame);
        }
    }
    public TherListener(TheraminController theraminController, Controller controller){
        this.theraminController = theraminController;
        this.controller = controller;
        theraminController.startSynth();

    }

    public void run(){
        Timer timer = new Timer();
        TimerTask updateFrame = new FrameTimer();
        timer.schedule(updateFrame, 300);

    }

    public void addController(Controller controller) {
        this.controller = controller;
    }
    public void addListener(TheraminController theraminController){
        this.theraminController = theraminController;
    }
}



public class TheraminMain {


    Controller controller;
    TheraminController theraminController;
    public static void main(String[] args) throws FileNotFoundException {
        // Create a sample listener and controller
        TheraminController theraminController = new TheraminController();
        Controller controller = new Controller();
        TherListener therListener = new TherListener(theraminController, controller);
        while(controller.isConnected()) {
            therListener.run();
        }
        //theraminController.startSynth();

        //Timer timer = new Timer();
        //TimerTask updateFrame = new FrameTimer();
        //timer.schedule(updateFrame, 200);

}
}
