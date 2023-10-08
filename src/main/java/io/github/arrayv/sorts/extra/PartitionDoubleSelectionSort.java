package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2021 Lu

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

@SortMeta(name = "Partition Double Selection")
final public class PartitionDoubleSelectionSort extends Sort {

    public PartitionDoubleSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private final double percent = 0.8;

    private void innerSort(int[] array, final int from, final int to, double sleep) {
        if (from == to) {
            return;
        }
        if (from + 1 == to) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
            }
            return;
        }

        // enhancement
        // int k = from, l = to;
        // boolean isForward = true;
        // while (k < l) {
        // if (Reads.compareIndices(array, k, l, 0.5, true) == 1) {
        // Writes.swap(array, k, l, 0.5, true, false);
        // }
        //
        // if (isForward) {
        // k++;
        // } else {
        // l--;
        // }
        // isForward = !isForward;
        // }

        int middle = (int) ((to + 1 - from) * percent + from);

        // enhancement
        int k = middle, l = to;
        boolean isForward = true;
        while (k >= from) {
            if (Reads.compareIndices(array, k, l, sleep, true) == 1) {
                Writes.swap(array, k, l, sleep, true, false);
            }

            if (isForward) {
                k--;
            } else {
                l--;
                if (l < middle) {
                    l = to;
                }
            }
            isForward = !isForward;
        }

        boolean isNeedContinue = true;
        while (isNeedContinue) {
            isNeedContinue = false;
            int minIndex = middle, maxIndex = from;
            for (int left = from + 1; left < middle; left++) {
                if (Reads.compareIndices(array, left, maxIndex, sleep, true) == 1) {
                    maxIndex = left;
                }
            }
            for (int right = middle + 1; right <= to; right++) {
                if (Reads.compareIndices(array, right, minIndex, sleep, true) == -1) {
                    minIndex = right;
                }
            }
            if (Reads.compareIndices(array, maxIndex, minIndex, sleep, true) == 1) {
                Writes.swap(array, maxIndex, minIndex, sleep, true, false);
                isNeedContinue = true;
            }
        }

        innerSort(array, from, middle - 1, sleep);
        innerSort(array, middle, to, sleep);
    }

    public void halfwayDoubleSelectionSort(int[] array, int length, double sleep, boolean auxwrite) {
        innerSort(array, 0, length - 1, sleep);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        halfwayDoubleSelectionSort(array, length, 0.01, false);
    }
}