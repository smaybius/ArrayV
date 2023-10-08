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
@SortMeta(name = "Partition Sandpaper")
final public class PartitionSandpaperSort extends Sort {

    public PartitionSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

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
        int k = from, l = to;
        boolean isForward = true;
        while (k < l) {
            if (Reads.compareIndices(array, k, l, 0.5, true) == 1) {
                Writes.swap(array, k, l, 0.5, true, false);
            }

            if (isForward) {
                k++;
            } else {
                l--;
            }
            isForward = !isForward;
        }

        int middle = (from + to) >> 1;

        int smallArrayMaxIndex = from;
        for (int left = from; left < middle; left++) {
            if (left > 0 && Reads.compareIndices(array, smallArrayMaxIndex, left, sleep, true) == 1) {
                continue;
            } else {
                smallArrayMaxIndex = left;
            }
            for (int right = middle; right <= to; right++) {
                if (Reads.compareIndices(array, left, right, sleep, true) == 1) {
                    Writes.swap(array, left, right, sleep, true, false);
                }
            }
        }

        innerSort(array, from, middle, sleep);
        innerSort(array, middle, to, sleep);
    }

    public void halfwaySandpaperSort(int[] array, int length, double sleep, boolean auxwrite) {
        innerSort(array, 0, length - 1, sleep);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        halfwaySandpaperSort(array, length, 0.01, false);
    }
}