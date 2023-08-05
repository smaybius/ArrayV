package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;

/*
 *
Copyright (c) rosettacode.org.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */

public abstract class ParallelCircleSorting extends Sort {
    protected ParallelCircleSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected int[] array;
    protected int end;

    protected volatile boolean swapped;
    protected volatile int swapCount;

    private class CircleSort extends Thread {
        private int a, b;

        private double sleep;

        public CircleSort(int a, int b, double sleep) {
            this.a = a;
            this.b = b;
            this.sleep = sleep;
        }

        public void run() {
            ParallelCircleSorting.this.circleSortRoutine(a, b, sleep);
        }
    }

    protected int circleSortRoutine(int a, int b, double sleep) {
        if (a >= this.end)
            return swapCount;

        for (int i = a, j = b - 1; i < j; i++, j--)
            if (j < this.end && Reads.compareIndices(array, i, j, sleep / 2, true) > 0) {
                Writes.swap(array, i, j, sleep, true, false);
                this.swapped = true;
                this.swapCount++;
            }

        if (b - a < 4)
            return swapCount;

        int m = (a + b) / 2;

        CircleSort l = new CircleSort(a, m, sleep);
        CircleSort r = new CircleSort(m, b, sleep);

        l.start();
        r.start();

        try {
            l.join();
            r.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return swapCount;
    }
}
