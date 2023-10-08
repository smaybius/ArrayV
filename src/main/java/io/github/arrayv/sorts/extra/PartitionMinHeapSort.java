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

@SortMeta(name = "Partition Min Heap")
final public class PartitionMinHeapSort extends Sort {
    public PartitionMinHeapSort(ArrayVisualizer arrayVisualizer) {
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

        final int middle = (from + to) >> 1;

        // enhancement
        if (from == 0) {
            int k = from, l = middle;
            boolean isForward = true;
            while (l <= to) {
                if (Reads.compareIndices(array, k, l, 0.5, true) == 1) {
                    Writes.swap(array, k, l, 0.5, true, false);
                }

                if (isForward) {
                    k++;
                } else {
                    l++;
                }
                isForward = !isForward;
            }
        }

        // 建立最小堆
        heapify(array, middle, to, sleep, false);
        for (int left = from; left < middle; left++) {
            // 最小值交换到左侧
            if (Reads.compareIndices(array, left, middle, sleep, true) > 0) {
                Writes.swap(array, left, middle, sleep, true, false);
                siftDownBase2(array, middle, to, middle, sleep, false);
            }
        }

        innerSort(array, from, middle - 1, sleep);
        innerSort(array, middle, to, sleep);
    }

    public void partitionHeapSort(int[] array, int length, double sleep, boolean auxwrite) {
        innerSort(array, 0, length - 1, sleep);
    }

    private void siftDownBase2(int[] array, int root, int end, int offset, double sleep, boolean isMax) {
        int compareVal = isMax ? -1 : 1;

        int leaf = root;
        Writes.changeAuxWrites(1);
        int val = array[leaf];
        while (true) {
            int leftChildIndex = getLeftChildIndexBase2(leaf, offset);
            if (leftChildIndex > end) {
                break;
            }
            int rightChildIndex = getRightChildIndexBase2(leaf, offset);
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
        for (int i = getParentIndexBase2(end, start); i >= start; i--) {
            siftDownBase2(arr, i, end, start, sleep, isMax);
        }
    }

    private int getLeftChildIndexBase2(int parentIndex, int offset) {
        return (parentIndex - offset) * 2 + 1 + offset;
    }

    private int getRightChildIndexBase2(int parentIndex, int offset) {
        return (parentIndex - offset) * 2 + 2 + offset;
    }

    private int getParentIndexBase2(int childIndex, int offset) {
        return (childIndex - offset - 1) / 2 + offset;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        partitionHeapSort(array, length, 0.5, false);
    }
}