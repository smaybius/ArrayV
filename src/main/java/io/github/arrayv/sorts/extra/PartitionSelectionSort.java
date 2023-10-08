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
@SortMeta(name = "Partition Selection")

final public class PartitionSelectionSort extends Sort {
    public PartitionSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void innerSort(int[] array, final int from, final int to, int[] externalArray,
            final int externalArrayLength, double sleep) {
        if (from == to) {
            return;
        }
        if (from + 1 == to) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
            }
            return;
        }

        int middle = (int) ((to + 1 - from) * percent + from);
        // if (middle == from || middle == to) {
        // middle = ((to - from) >> 1) + from;
        // }
        if (middle == from) {
            middle++;
        } else if (middle == to) {
            middle--;
        }

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

        int smallArrayMaxIndex = from;
        boolean isNeedFindMin = true;
        // int minIndex = externalArrayLength > 0 ? externalArray[bufferIndexStart] :
        // from;
        int minIndex = middle;
        int saveSmallIndex = 0;
        for (int left = from; left < middle; left++) {
            // 减少比较次数
            if (left > 0 && Reads.compareIndices(array, smallArrayMaxIndex, left, sleep, true) == 1) {
                if (left >= (int) ((middle - from) * percent + from)) {
                    Writes.write(externalArray, saveSmallIndex++, left, 0, false, true);
                }
                continue;
            } else {
                smallArrayMaxIndex = left;
            }
            if (isNeedFindMin) {
                isNeedFindMin = false;
                // for (int right = middle; right <= to; right++) {
                for (int i = bufferIndexStart; i < bufferIndexStart + externalArrayLength; i++) {
                    int right = externalArray[i];
                    if (Reads.compareIndices(array, minIndex, right, sleep, true) == 1) {
                        minIndex = right;
                    }
                }
            }
            if (Reads.compareIndices(array, left, minIndex, sleep, true) == 1) {
                Writes.swap(array, left, minIndex, sleep, true, false);
                minIndex = middle;
                isNeedFindMin = true;
            }
        }
        Writes.arraycopy(externalArray, 0, externalArray, bufferIndexStart, saveSmallIndex, sleep, true, true);
        innerSort(array, from, middle, externalArray, saveSmallIndex, sleep);
        int j = bufferIndexStart;
        for (int i = (int) ((to + 1 - middle) * percent + middle); i <= to;) {
            Writes.write(externalArray, j++, i++, 0, false, true);
        }
        innerSort(array, middle, to, externalArray, j - bufferIndexStart, sleep);
    }

    private int bufferIndexStart;
    private final double percent = 0.86;

    public void halfwaySelectionSort(int[] array, int length, double sleep, boolean auxwrite) {
        int[] externalArray = Writes.createExternalArray(length + (length >> 1));
        int j = bufferIndexStart;
        for (int i = (int) (length * percent); i <= length - 1;) {
            Writes.write(externalArray, j++, i++, 0, false, true);
        }
        innerSort(array, 0, length - 1, externalArray, j - bufferIndexStart, sleep);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.bufferIndexStart = length >> 1;
        halfwaySelectionSort(array, length, 0.05, false);
    }
}