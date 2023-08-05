package io.github.arrayv.utils;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class ArrayWaveEqualizer implements AudioProcessor {

    @Override
    public boolean process(AudioEvent audioEvent) {
        float[] buffer = audioEvent.getFloatBuffer(); // the incoming audio stream
        buffer = applyRMSCompressor(buffer);
        return true;
    }

    private float[] applyRMSCompressor(float[] inputArray) {
        // Calculate the RMS value
        float sumOfSquares = 0;
        for (float value : inputArray) {
            sumOfSquares += value * value;
        }
        float rmsValue = (float) Math.sqrt(sumOfSquares / inputArray.length);

        // Apply the RMS compressor
        float[] compressedArray = new float[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            compressedArray[i] = inputArray[i] / rmsValue;
        }

        return compressedArray;
    }

    @Override
    public void processingFinished() {
    }
}
