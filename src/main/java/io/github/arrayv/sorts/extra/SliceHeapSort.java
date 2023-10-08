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
@SortMeta(name = "Slice Heap", question = "set slice:", defaultAnswer = 8)
final public class SliceHeapSort extends Sort {
    public SliceHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int totalSlice;

    public void sliceHeapSort(int[] array, int length, double sleep, boolean auxwrite) {
        final int start = 0, end = length - 1;

        for (int slice = 1; slice <= totalSlice; slice++) {
            int sliceStart = getSliceStart(start, end, slice);
            int sliceEnd = getSliceEnd(start, end, slice);
            heapify(array, sliceStart, sliceEnd, sleep, true);
        }

        int sortedEnd = end;
        int compare = 0;
        while (sortedEnd > start) {
            int maxIndex = start;
            int maxSlice = 1;
            for (int slice = 2; slice <= totalSlice; slice++) {
                int sliceStart = getSliceStart(start, end, slice);
                if (sliceStart > sortedEnd)
                    break;
                compare++;
                if (Reads.compareIndices(array, sliceStart, maxIndex, sleep, true) == 1) {
                    maxIndex = sliceStart;
                    maxSlice = slice;
                }
            }
            Writes.swap(array, maxIndex, sortedEnd--, sleep, true, false);
            int sliceEnd = getSliceEnd(start, end, maxSlice);
            if (sliceEnd > sortedEnd)
                sliceEnd = sortedEnd;
            siftDownBase2(array, maxIndex, sliceEnd, maxIndex, sleep, true);
        }
        System.out.println("compare = " + compare);
    }

    public void sliceHeapSort2(int[] array, int length, double sleep, boolean auxwrite) {
        final int start = 0, end = length - 1;

        for (int slice = 1; slice <= totalSlice; slice++) {
            int sliceStart = getSliceStart(start, end, slice);
            int sliceEnd = getSliceEnd(start, end, slice);
            heapify(array, sliceStart, sliceEnd, sleep, true);
        }

        int sortedEnd = end;

        while (sortedEnd > start) {
            int maxIndex = start;
            int maxSlice = 1;
            for (int slice = 2; slice <= totalSlice; slice++) {
                int sliceStart = getSliceStart(start, end, slice);
                if (sliceStart > sortedEnd)
                    break;

                if (Reads.compareIndices(array, sliceStart, maxIndex, sleep, true) == 1) {
                    maxIndex = sliceStart;
                    maxSlice = slice;
                }
            }
            Writes.swap(array, maxIndex, sortedEnd--, sleep, true, false);
            int sliceEnd = getSliceEnd(start, end, maxSlice);
            if (sliceEnd > sortedEnd)
                sliceEnd = sortedEnd;
            siftDownBase2(array, maxIndex, sliceEnd, maxIndex, sleep, true);
        }
    }

    /**
     * 获取第 n 个 slice 的 slice 开始 index
     *
     * @param start arr开始下标
     * @param end   arr结束下标
     * @param slice 第 n 个 slice（从1开始）
     * @return slice 开始 index
     */
    private int getSliceStart(int start, int end, int slice) {
        if (slice == 1) {
            return start;
        } else {
            return (int) (start + (end - start) * ((slice - 1) * 1.0 / totalSlice)) + 1;
        }
    }

    private int getSliceEnd(int start, int end, int slice) {
        return (int) (start + (end - start) * (slice * 1.0 / totalSlice));
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
        this.totalSlice = bucketCount;
        sliceHeapSort(array, length, 0.5, false);
    }
}