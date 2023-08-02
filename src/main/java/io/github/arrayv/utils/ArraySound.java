package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

//
// ArraySoundjava (a part of PitchLab)
// (Version 0.6)
//
// Created by Gavin Shriver on 4/17/09.
// This is a class written as part of PitchLab for a research project involving
// human pitch perception. This class can generate a continuous sine wave at
// a specified frequency. The frequency can be changed on the fly as nessasary.
//
@RunWith(RobolectricTestRunner.class)
public class ArraySound extends Thread {
    public class AndroidAudioDevice {
        AudioTrack track;
        short[] buffer = new short[1024];

        public AndroidAudioDevice() {
            int minSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    minSize, AudioTrack.MODE_STREAM);
            track.play();
        }

        public void writeSamples(float[] samples) {
            fillBuffer(samples);
            track.write(buffer, 0, samples.length);
        }

        private void fillBuffer(float[] samples) {
            if (buffer.length < samples.length)
                buffer = new short[samples.length];

            for (int i = 0; i < samples.length; i++)
                buffer[i] = (short) (samples[i] * Short.MAX_VALUE);
            ;
        }
    }

    public void run() {

        final float frequency = 440;
        float increment = (float) (2 * Math.PI) * frequency / 44100; // angular increment for each sample
        float angle = 0;
        AndroidAudioDevice device = new AndroidAudioDevice();
        float samples[] = new float[1024];

        while (true) {
            for (int i = 0; i < samples.length; i++) {
                samples[i] = (float) Math.sin(angle);
                angle += increment;
            }

            device.writeSamples(samples);
        }
    }
}
