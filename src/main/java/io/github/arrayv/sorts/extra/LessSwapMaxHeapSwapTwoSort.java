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

@SortMeta(name = "Less Swap Max Heap Swap 2Element")
final public class LessSwapMaxHeapSwapTwoSort extends Sort {

    public LessSwapMaxHeapSwapTwoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void siftDownBase2(int[] array, int root, int end, double sleep, boolean isMax) {
        int compareVal = isMax ? -1 : 1;

        int leaf = root;
        Writes.changeAuxWrites(1);
        int val = array[leaf];
        while (true) {
            int leftChildIndex = getLeftChildIndexBase2(leaf);
            if (leftChildIndex > end) {
                break;
            }
            int rightChildIndex = getRightChildIndexBase2(leaf);
            int extremum = leftChildIndex;
            if (rightChildIndex <= end) {
                int result = Reads.compareIndices(array, leftChildIndex, rightChildIndex, sleep, true);
                if (result == compareVal || result == 0) {
                    extremum = rightChildIndex;
                }
            }
            if (Reads.compareValueIndex(array, val, extremum, sleep, true) == compareVal) {
                Writes.write(array, leaf, array[extremum], sleep, true, false);
            } else {
                break;
            }
            leaf = extremum;
        }
        if (leaf != root) {
            Writes.write(array, leaf, val, sleep, true, false);
        }
    }

    private void heapify(int[] arr, int start, int end, double sleep, boolean isMax) {
        for (int i = getParentIndexBase2(end); i >= start; i--) {
            siftDownBase2(arr, i, end, sleep, isMax);
        }
    }

    private int getLeftChildIndexBase2(int parentIndex) {
        return parentIndex * 2 + 1;
    }

    private int getRightChildIndexBase2(int parentIndex) {
        return parentIndex * 2 + 2;
    }

    private int getParentIndexBase2(int childIndex) {
        return (childIndex - 1) / 2;
    }

    @Override
    public void runSort(int[] array, int length, int base) {
        int start = 0, end = length - 1;
        double sleep = 0.5;
        heapify(array, start, end, sleep, true);

        while (end > 0) {
            Writes.swap(array, 0, end--, sleep, true, false);
            int secondMaxIndex = 1;
            if (end >= 2) {
                if (Reads.compareIndices(array, 1, 2, sleep, true) == -1) {
                    secondMaxIndex = 2;
                }
                Writes.swap(array, secondMaxIndex, end--, sleep, true, false);
                siftDownBase2(array, secondMaxIndex, end, sleep, true);
            }
            siftDownBase2(array, 0, end, sleep, true);
        }
    }
}