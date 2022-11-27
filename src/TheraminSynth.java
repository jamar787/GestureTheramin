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


public class TheraminSynth {
    private DualOscillatorSynthVoice osc1, osc2, osc3, osc4, osc5, osc6;

    private float freq1, freq2, freq3, freq4, freq5, freq6;

    private float[] frequencies;

    private float maxFrequency = 8000;

    private float minFrequency = 15;

    int maxHarmonics = 5;
    private double amplitude;
    private Synthesizer synth;

    private LineOut lineOut;

    public TheraminSynth(){
        freq1 = freq2 = freq3 = freq4 = freq5 = freq6 = 200;
        frequencies = new float[6];
        amplitude = 0.5;
        synth = JSyn.createSynthesizer();
        osc1 = new DualOscillatorSynthVoice();
        osc2 = new DualOscillatorSynthVoice();
        osc3 = new DualOscillatorSynthVoice();
        osc4 = new DualOscillatorSynthVoice();
        osc5 = new DualOscillatorSynthVoice();
        osc6 = new DualOscillatorSynthVoice();
        synth.add(osc1.getUnitGenerator());
        synth.add(osc2.getUnitGenerator());
        synth.add(osc3.getUnitGenerator());
        synth.add(osc4.getUnitGenerator());
        synth.add(osc5.getUnitGenerator());
        synth.add(osc6.getUnitGenerator());
        synth.add(lineOut = new LineOut());
        osc1.getOutput().connect(0, lineOut.input, 0);
        osc1.getOutput().connect(0, lineOut.input, 1);
        osc2.getOutput().connect(0, lineOut.input, 0);
        osc2.getOutput().connect(0, lineOut.input, 1);
        osc3.getOutput().connect(0, lineOut.input, 0);
        osc3.getOutput().connect(0, lineOut.input, 1);
        osc4.getOutput().connect(0, lineOut.input, 0);
        osc4.getOutput().connect(0, lineOut.input, 1);
        osc5.getOutput().connect(0, lineOut.input, 0);
        osc5.getOutput().connect(0, lineOut.input, 1);
        osc6.getOutput().connect(0, lineOut.input, 0);
        osc6.getOutput().connect(0, lineOut.input, 1);
        synth.startUnit(lineOut);
        initializeFrequencies();

    }

    public void initializeFrequencies(){
        frequencies[0] = freq1;
        frequencies[1] = freq2;
        frequencies[2] = freq3;
        frequencies[3] = freq4;
        frequencies[4] = freq5;
        frequencies[5] = freq6;
    }

    public void startSynth(){
        synth.start();
        lineOut.start();
    }
    public void stopSynth(){
        synth.stop();
        lineOut.stop();
    }

    public void updateActiveNotes(boolean[] numHarmonics){
        double timeNow = synth.getCurrentTime();
        TimeStamp timeStamp = new TimeStamp(timeNow);
        for(int i = 0; i < numHarmonics.length; i++){
            if(numHarmonics[i]){
                if(i == 0) osc1.noteOn(frequencies[i], amplitude, timeStamp);
                else if(i == 1) osc2.noteOn(frequencies[i], amplitude, timeStamp);
                else if(i == 2) osc3.noteOn(frequencies[i], amplitude, timeStamp);
                else if(i == 3) osc4.noteOn(frequencies[i], amplitude, timeStamp);
                else if(i == 4) osc5.noteOn(frequencies[i], amplitude, timeStamp);
                else if(i == 5) osc6.noteOn(frequencies[i], amplitude, timeStamp);
            }
            else{
                if(i == 0) osc1.noteOff( timeStamp);
                else if(i == 1) osc2.noteOff( timeStamp);
                else if(i == 2) osc3.noteOff( timeStamp);
                else if(i == 3) osc4.noteOff( timeStamp);
                else if(i == 4) osc5.noteOff(timeStamp);
                else if(i == 5) osc6.noteOff(timeStamp);
            }

        }
    }

    //public void stopNotes()

    public void setAmplitude(double amplitude){
        this.amplitude = amplitude;
    }

    public void setFrequencies(float baseFrequency, boolean[] numHarmonics){
        for(int i = 0; i < numHarmonics.length; i++){
            if(numHarmonics[i]){
                frequencies[i] = baseFrequency * (i + 1);
            }
        }
    }
   // public void setFrequencies(float baseFrequency, int numHarmonics){
        /*for(int i = 0; i < numHarmonics; i++){
            if(i==0) frequencies[i] = baseFrequency;
            else frequencies[i] = frequencies[i-1] *2;
            if(frequencies[i] > maxFrequency) frequencies[i] = baseFrequency;
        }*/
        /*for(int i=0; i<numHarmonics; i++){
            if(i == 0) freq1 = baseFrequency;
            else if(i == 1) freq2 = freq1 * 2;
            else if(i == 2) freq3 = freq2 * 2;
            else if(i == 3) freq4 = freq3 * 2;
            else if(i == 4) freq5 = freq4 * 2;
            else if(i == 5) freq6 = freq5 * 2;
        }*/
    //}

}
