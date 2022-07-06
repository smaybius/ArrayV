package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * 
MIT License
Copyright (c) 2020 fungamer2 & yuji
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

final public class GarbageMergeSort extends Sort {
    public GarbageMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Garbage Merge");
        this.setRunAllSortsName("Garbage Merge Sort");
        this.setRunSortName("Garbage Merge Sort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32); // To be decided
        this.setBogoSort(false);
    }

    public void garbageMerge(int[] a, int start, int end, int depth) {
        if (start < end) {
            if (Reads.compareIndices(a, start, end, 0.001, true) > 0) {
                Writes.swap(a, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                Writes.recordDepth(depth);
                Writes.recursion();
                garbageMerge(a, start, end - 1, depth + 1);
                Writes.recursion();
                garbageMerge(a, start + 1, end, depth + 1);
            }
        }
    }

    public void garbageMergeSort(int[] a, int start, int end, int depth) {
        if (start < end) {
            if (Reads.compareIndices(a, start, end, 0.001, true) > 0) {
                Writes.swap(a, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                Writes.recordDepth(depth);
                Writes.recursion();
                garbageMergeSort(a, start, mid, depth + 1);
                Writes.recursion();
                garbageMergeSort(a, mid + 1, end, depth + 1);
                garbageMerge(a, start, end, depth + 1);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.garbageMergeSort(array, 0, currentLength - 1, 0);
    }
}