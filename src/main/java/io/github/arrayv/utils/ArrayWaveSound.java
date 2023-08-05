package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class ArrayWaveSound implements AudioProcessor {
    public class Phasor { // By Florian Mrugalla, The Audio Programmer Discord server
        private float phase, inc;

        public void setInc(float hz, float sampleRate) {
            inc = hz / sampleRate;
        }

        public float process() {
            phase += inc;
            if (phase >= 1.0f) {
                --phase;
            }
            return ArrayVisualizer.getInstance()
                    .getArray()[(int) (phase * ArrayVisualizer.getInstance().getCurrentLength())] * 0.01f;
        }
    }

    private double gain;
    private double oldGain;
    private double frequency;
    private double phase;
    private Phasor phs;
    private final float sampleRate = 192000;
    private boolean enabled;

    public ArrayWaveSound() {
        this(0.0, 440);
    }

    public ArrayWaveSound(double gain, double frequency) {
        this.enabled = true;
        this.gain = gain;
        this.oldGain = gain;
        this.frequency = frequency;
        this.phase = 0;
        this.phs = new Phasor();
        phs.setInc((float) frequency, sampleRate);
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double val) {
        frequency = val;
        phs.setInc((float) frequency, sampleRate);
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double val) {
        if (enabled)
            gain = val;
    }

    public void enable() {
        this.enabled = true;
        this.gain = this.oldGain;
    }

    public void disable() {
        this.oldGain = this.gain;
        this.gain = 0;
        this.enabled = false;
    }

    @Override
    public boolean process(AudioEvent audioEvent) {
        float[] buffer = audioEvent.getFloatBuffer();
        double sampleRate = audioEvent.getSampleRate();
        double twoPiF = 2 * Math.PI * frequency;
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (float) ((gain / 200) * (32768.0 / ArrayVisualizer.getInstance().getCurrentLength())
                    * phs.process());
        }
        phase = twoPiF * buffer.length / sampleRate + phase;
        return true;
    }

    @Override
    public void processingFinished() {
    }
}
