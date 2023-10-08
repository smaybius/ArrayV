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
@SortMeta(name = "Partition Double Heap")
final public class PartitionDoubleHeapSort extends Sort {

    public PartitionDoubleHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void innerSort(int[] array, final int from, final int to, int base, boolean isLeftPartAlreadyMaxHeap,
            boolean isSubArrayAlreadyMinHeap, double sleep) {
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
        if (!isSubArrayAlreadyMinHeap) {
            Writes.reversal(array, from, to, 1, true, false);
            // int k = from, l = to;
            // boolean isForward = true;
            // while (k < l) {
            // if (Reads.compareIndices(array, k, l, 0.5, true) == 1) {
            // Writes.swap(array, k, l, 0.5, true, false);
            // }
            // if (isForward) {
            // k++;
            // } else {
            // l--;
            // }
            // isForward = !isForward;
            // }
        }

        int middle = (from + to) >> 1;
        // int middle = (int) ((to - from) * 0.5 + from);

        // if (!isLeftPartAlreadyMaxHeap) {
        // if (isSubArrayAlreadyMinHeap) {
        // Writes.reversal(array, from, middle, 0.5, true, false);
        // }
        heapify(array, from, middle - 1, 0.3, true);
        // }
        heapify(array, middle, to, 0.3, false);

        while (true) {
            if (Reads.compareIndices(array, from, middle, sleep, true) == 1) {
                Writes.swap(array, from, middle, sleep, true, false);
                siftDownBase2(array, from, middle - 1, from, sleep, true);
                siftDownBase2(array, middle, to, middle, sleep, false);
            } else
                break;
        }

        innerSort(array, from, middle - 1, base, true, false, sleep);
        innerSort(array, middle, to, base, false, true, sleep);
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

    // private void shiftDown(int[] array, int root, int end, double sleep, boolean
    // isMax) {
    // int compareVal = isMax ? -1 : 1;
    //
    // final int offset = root;
    // int val = array[root];
    // while (true) {
    // int leftChild = (root - offset) * 2 + 1 + offset, rightChild = leftChild + 1;
    // if (leftChild > end) {
    // Writes.write(array, root, val, 0, true, false);
    // break;
    // }
    // int biggerIndex = leftChild;
    // if (leftChild < end) {
    // if (Reads.compareIndices(array, leftChild, rightChild, 0, true) ==
    // compareVal) {
    // biggerIndex = rightChild;
    // }
    // }
    // Delays.sleep(sleep);
    // if (Reads.compareValueIndex(array, val, biggerIndex, 0, true) == compareVal)
    // {
    // Writes.write(array, root, array[biggerIndex], 0, true, false);
    // } else {
    // Writes.write(array, root, val, 0, true, false);
    // break;
    // }
    // root = biggerIndex;
    // }
    // }
    //
    // private void shiftUp(int[] array, final int offset, int leaf, double sleep,
    // boolean isMax) {
    // int compareVal = isMax ? -1 : 1;
    //
    // int val = array[leaf];
    // int parent;
    // while (true) {
    // parent = (leaf - 1 - offset) / 2 + offset;
    // if (leaf == parent) {
    // Writes.write(array, leaf, val, 0, true, false);
    // break;
    // }
    // Delays.sleep(sleep);
    // if (Reads.compareIndexValue(array, parent, val, 0, true) == compareVal) {
    // Writes.write(array, leaf, array[parent], 0, true, false);
    // } else {
    // Writes.write(array, leaf, val, 0, true, false);
    // break;
    // }
    // leaf = parent;
    // }
    // }
    //
    // protected void heapify(int[] arr, int start, int end, double sleep, boolean
    // isMax) {
    // for (int i = start + 1; i <= end; i++) {
    // shiftUp(arr, start, i, sleep, isMax);
    // }
    // }

    public void halfwayDoubleHeapSort(int[] array, int length, int base, double sleep, boolean auxwrite) {
        innerSort(array, 0, length - 1, base, false, false, sleep);
    }

    @Override
    public void runSort(int[] array, int length, int base) {
        halfwayDoubleHeapSort(array, length, base, 0.01, false);
        // InsertionSort insertionSort = new InsertionSort(arrayVisualizer);
        // insertionSort.runSort(array, length, 0);
    }
}