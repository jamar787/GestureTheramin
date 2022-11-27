import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math;
import java.util.*;

import com.jsyn.instruments.DualOscillatorSynthVoice;
import com.leapmotion.leap.*;
import com.jsyn.*;
import com.jsyn.unitgen.*;
import com.leapmotion.leap.Vector;
import com.softsynth.shared.time.TimeStamp;

public class TheraminController {

    private class Range{
        public Float lowerBound;
        public Float upperBound;
        public Range(Float lowerBound, Float upperBound){
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;

        }

    }
    private Vector leftPalmPosition;
    private Vector rightPalmPosition;
    //private Synthesizer synth;
    //private LineOut lineOut;
    TheraminSynth synth;
    //private SineOscillator sinOsc;

    //private DualOscillatorSynthVoice dualOsc;

    //private DualOscillatorSynthVoice osc1, osc2, osc3, osc4, osc5, osc6;

    //private ArrayList<DualOscillatorSynthVoice> oscillators;
    //private ArrayList<Double> frequencies;

    //private int numHarmonics;
    private boolean[] numHarmonics;
    private double amplitude;
    private float frequency;

    //private Double freq1, freq2, freq3, freq4, freq5, freq6;
    private float minY = 0;
    private float maxY = 400;
    private float minFrequency = 15;
    private float maxFrequency = 8000;
    private float minAmplitude = 0;
    private float maxAmplitude = 1;
    private float frequencySlope;
    private float amplitudeSlope;

    private ArrayList<Float> notes = new ArrayList<Float>();
  //private HashMap<Float, Float> positionToNotes = new HashMap<Float, Float>();
    private NavigableMap<Float, Integer> positionToNotes = new TreeMap<Float, Integer>();

    /*NavigableMap<Integer, Range> map = new TreeMap<Integer, Range>();
map.put(0, new Range(3, 0));       // 0..3     => 0
map.put(5, new Range(10, 1));      // 5..10    => 1
map.put(100, new Range(200, 2));   // 100..200 => 2

    // To do a lookup for some value in 'key'
    Map.Entry<Integer,Range> entry = map.floorEntry(key);
if (entry == null) {
        // too small
    } else if (key <= entry.getValue().upper) {
        return entry.getValue().value;
    } else {
        // too large or in a hole
    }*/
    public TheraminController() throws FileNotFoundException {
        leftPalmPosition = new Vector();
        rightPalmPosition = new Vector();
        amplitude = 0.5;
        frequency = 200;
        synth = new TheraminSynth();
        numHarmonics = new boolean[6];
        //frequencies = new ArrayList<Double>();
        //oscillators = new ArrayList<DualOscillatorSynthVoice>();
        /*freq1 = frequency;
        freq2 = frequency;
        freq3 = frequency;
        freq4 = frequency;
        freq5 = frequency;
        freq6 = frequency;
        frequencies.add(freq1);
        frequencies.add(freq2);
        frequencies.add(freq3);
        frequencies.add(freq4);
        frequencies.add(freq5);
        frequencies.add(freq6);
*/
        minAmplitude = 0;
        frequencySlope = (maxFrequency-minFrequency)/(maxY-minY);
        amplitudeSlope = (maxAmplitude-minAmplitude)/(maxY-minY);
        //oscillators.add(osc1);
        //oscillators.add(osc2);
        //oscillators.add(osc3);
        //oscillators.add(osc4);
        //oscillators.add(osc5);
        //oscillators.add(osc6);
        //synth = JSyn.createSynthesizer();
        //sinOsc = new SineOscillator();
        //dualOsc = new DualOscillatorSynthVoice();
        //synth.add(dualOsc.getUnitGenerator());
        //synth.add(lineOut = new LineOut());
        //sinOsc.getOutput().connect(0, lineOut.input, 0);
        //sinOsc.getOutput().connect(0, lineOut.input, 1);
        //dualOsc.getOutput().connect(0, lineOut.input, 0);
        //dualOsc.getOutput().connect(0, lineOut.input, 1);
        //synth.startUnit(lineOut);
        populateNotes();
        setPositionToNotes();
        //initializeSynth();

    }
    private void initializeHarmonics(){
        numHarmonics[0] = true;
        numHarmonics[1] = false;
        numHarmonics[2] = false;
        numHarmonics[3] = false;
        numHarmonics[4] = false;
        numHarmonics[5] = false;
    }
 /*   private void initializeSynth(){
        synth = JSyn.createSynthesizer();
        synth.add(lineOut= new LineOut());
        for(DualOscillatorSynthVoice osc : oscillators){
            osc = new DualOscillatorSynthVoice();
            synth.add(osc.getUnitGenerator());
            osc.getOutput().connect(0, lineOut.input, 0);
            osc.getOutput().connect(0, lineOut.input, 1);
        }
        synth.startUnit(lineOut);
    }*/

    private void setPositionToNotes(){
        //map.put(0, 0);    // 0..4     => 0
        //map.put(5, 1);    // 5..10    => 1
        //map.put(11, 2);   // 11..200  => 2
        Double step = 3.9037;
        Float lowerYBound = minY;
        Float upperYBound = lowerYBound + step.floatValue();
        for(int i = 0; i < notes.size(); i++){

            System.out.println(upperYBound);
            positionToNotes.put(upperYBound, i);
            upperYBound += step.floatValue();
        }


    }

    private void populateNotes() throws FileNotFoundException {
        File noteFID = new File("src/notes");
        Scanner scanner = new Scanner(noteFID);
        while (scanner.hasNext()) {
            notes.add(scanner.nextFloat());
        }
    }

    /*private void getFingerHarmonics(Hand hand) {
        numHarmonics = 0;
        for (Finger finger : hand.fingers()) {
            if (finger.isExtended()) {
                numHarmonics++;
            }
        }
        for (int i = 0; i < numHarmonics; i++) {
            if (i != 0) {
                double nextHarmonic = frequencies.get(i-1) * 2;
                frequencies.set(i, nextHarmonic);
            }
        }
    }*/
 //TYPE_THUMB, TYPE_INDEX, TYPE_MIDDLE, TYPE_RING, TYPE_PINKY;
    private void fingerHarmonics(Finger finger) {
        if (!finger.isExtended()){
            if(finger.type() == Finger.Type.TYPE_THUMB) numHarmonics[1] = true;
            else if(finger.type() == Finger.Type.TYPE_INDEX) numHarmonics[2] = true;
            else if(finger.type() == Finger.Type.TYPE_MIDDLE) numHarmonics[3] = true;
            else if(finger.type() == Finger.Type.TYPE_RING) numHarmonics[4] = true;
            else if(finger.type() == Finger.Type.TYPE_PINKY) numHarmonics[5] = true;
        }
        else{
            if(finger.type() == Finger.Type.TYPE_THUMB) numHarmonics[1] = false;
            else if(finger.type() == Finger.Type.TYPE_INDEX) numHarmonics[2] = false;
            else if(finger.type() == Finger.Type.TYPE_MIDDLE) numHarmonics[3] = false;
            else if(finger.type() == Finger.Type.TYPE_RING) numHarmonics[4] = false;
            else if(finger.type() == Finger.Type.TYPE_PINKY) numHarmonics[5] = false;

        }
    }

    private void processLeftHand(Hand hand){
        setLeftPalmPosition(hand.palmPosition());
        if(leftPalmPosition.getY() < minY){
            leftPalmPosition.setY(minY);
        }
        if(leftPalmPosition.getY() > maxY){
            leftPalmPosition.setY(maxY);
        }
        //frequency = mapFrequency(leftPalmPosition.getY());
        frequency = mapPositionToNotes(leftPalmPosition.getY());
        for(Finger finger : hand.fingers()){
            fingerHarmonics(finger);
        }

        synth.setFrequencies(frequency, numHarmonics);
        //frequencies.set(0, (double) mapPositionToNotes(leftPalmPosition.getY()));
        //getFingerHarmonics(hand);
    }

    private void processRightHand(Hand hand){
        setRightPalmPosition(hand.palmPosition());
        if(rightPalmPosition.getY() < minY){
            rightPalmPosition.setY(minY);
        }
        if(rightPalmPosition.getY() > maxY){
            rightPalmPosition.setY(maxY);
        }
        amplitude = mapAmplitude(rightPalmPosition.getY());
        synth.setAmplitude(amplitude);
    }

    /*private void playFrequencies() {
        double timeNow = synth.getCurrentTime();
        TimeStamp timeStamp = new TimeStamp(timeNow);
        *//*if (numHarmonics == 0) {
            oscillators.get(0).noteOn(frequencies.get(0), amplitude, timeStamp);
            oscillators.get(1).noteOff(timeStamp);
            oscillators.get(2).noteOff(timeStamp);
            oscillators.get(3).noteOff(timeStamp);
            oscillators.get(4).noteOff(timeStamp);
            oscillators.get(5).noteOff(timeStamp);
        } else {
            for (int i = 0; i < oscillators.size(); i++) {
                if (i <= numHarmonics) {
                    oscillators.get(i).noteOn(frequencies.get(i), amplitude, timeStamp);
                } else {
                    oscillators.get(i).noteOff(timeStamp);
                }
            }
        }*//*
        oscillators.get(0).noteOn(frequency, amplitude, timeStamp);
    }*/

      /*  private void stopNotes(){
        double timeNow = synth.getCurrentTime();
        TimeStamp timeStamp = new TimeStamp(timeNow);
        for(int i = 0; i < numHarmonics; i++){
            oscillators.get(i).noteOff(timeStamp);
        }
    }*/

     public void processMotion(Frame frame){
        if(frame.hands().count() > 0) {
            for (Hand hand : frame.hands()) {
                if(hand.isLeft()){
                    processLeftHand(hand);
                }
                else{
                    processRightHand(hand);
                }

                //playFrequencies();
                //double timeNow = synth.getCurrentTime();
                //TimeStamp timeStamp = new TimeStamp(timeNow);
                //dualOsc.noteOn(frequency, amplitude, timeStamp);
            }
        }
        synth.updateActiveNotes(numHarmonics);
        /*else{
            //double timeNow = synth.getCurrentTime();
            //TimeStamp timeStamp = new TimeStamp(timeNow);
            //dualOsc.noteOff(timeStamp);
            //stopNotes();
        }*/
     }

public void startSynth(){
        synth.startSynth();
    }
    public void stopSynth(){
        synth.stopSynth();
    }

    private float mapFrequency(float currentYPosition){
        return minFrequency + frequencySlope * (currentYPosition - minY);
    }
    private float mapPositionToNotes(float currentYPosition){
       // float lastYBound = 0;
        float yBound;
        if(currentYPosition < minY){
            yBound = positionToNotes.floorKey(minY);
        }
        else if(currentYPosition > maxY){
            yBound = positionToNotes.ceilingKey(maxY);
        }
        else{
            yBound = positionToNotes.ceilingKey(currentYPosition);
        }
        //System.out.println(yBound);
        return notes.get(positionToNotes.get(yBound));

        //for(int i = 0; i < positionToNotes.size(); i++){

//}
    }
    private float mapAmplitude(float currentYPosition){
        return minAmplitude + amplitudeSlope * (currentYPosition - minY);
    }
    public void setLeftPalmPosition(Vector leftPalmPosition) {
        this.leftPalmPosition = leftPalmPosition;
    }
    public void setRightPalmPosition(Vector rightPalmPosition) {
        this.rightPalmPosition = rightPalmPosition;
}

}
