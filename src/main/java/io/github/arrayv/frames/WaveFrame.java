package io.github.arrayv.frames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.ArrayWaveSound;

/*
 *
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2021-2022 ArrayV Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */
public class WaveFrame extends javax.swing.JFrame {
    JFrame frame;

    public WaveFrame(ArrayWaveSound arrayWaveSound) {
        setBounds(500, 100, 300, 100);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gblContentPane = new GridBagLayout();
        gblContentPane.columnWidths = new int[] { 0, 0 };
        gblContentPane.rowHeights = new int[] { 0, 0 };
        contentPane.setLayout(gblContentPane);

        JLabel lblFreq = new JLabel("Wave Freq:");
        GridBagConstraints gbcLblFreq = new GridBagConstraints();
        gbcLblFreq.gridx = 0;
        gbcLblFreq.gridy = 0;
        contentPane.add(lblFreq, gbcLblFreq);

        SpinnerModel sldFreq = new SpinnerNumberModel((int) arrayWaveSound.getFrequency(), 10, 5000, 10);
        sldFreq.addChangeListener(
                e -> arrayWaveSound.setFrequency((((SpinnerNumberModel) e.getSource()).getNumber().floatValue())));
        GridBagConstraints gbcsldFreq = new GridBagConstraints();
        gbcsldFreq.gridx = 0;
        gbcsldFreq.gridy = 1;
        JSpinner spinFreq = new JSpinner(sldFreq);
        contentPane.add(spinFreq, gbcsldFreq);

        JLabel lblVol = new JLabel("Wave Vol(in %):");
        GridBagConstraints gbcLblVol = new GridBagConstraints();
        gbcLblVol.gridx = 1;
        gbcLblVol.gridy = 0;
        contentPane.add(lblVol, gbcLblVol);

        SpinnerModel sldVol = new SpinnerNumberModel((int) (arrayWaveSound.getGain() * 100), 0, 70, 10);
        sldVol.addChangeListener(
                e -> arrayWaveSound.setGain(((SpinnerNumberModel) e.getSource()).getNumber().floatValue() / 100));
        GridBagConstraints gbcsldVol = new GridBagConstraints();
        gbcsldVol.gridx = 1;
        gbcsldVol.gridy = 1;
        JSpinner spinVol = new JSpinner(sldVol);
        contentPane.add(spinVol, gbcsldVol);

        JLabel lblFlt = new JLabel("Filter Cutoff:");
        GridBagConstraints gbcLblFlt = new GridBagConstraints();
        gbcLblFlt.gridx = 2;
        gbcLblFlt.gridy = 0;
        contentPane.add(lblFlt, gbcLblFlt);

        SpinnerModel sldFlt = new SpinnerNumberModel((int) (48000), 0, 48000, 100);
        sldFlt.addChangeListener(
                e -> ArrayVisualizer.getInstance().getSounds()
                        .setFilter(((SpinnerNumberModel) e.getSource()).getNumber().floatValue()));
        GridBagConstraints gbcsldFlt = new GridBagConstraints();
        gbcsldFlt.gridx = 2;
        gbcsldFlt.gridy = 1;
        JSpinner spinFlt = new JSpinner(sldFlt);
        contentPane.add(spinFlt, gbcsldFlt);
        setAlwaysOnTop(true);
    }
}
