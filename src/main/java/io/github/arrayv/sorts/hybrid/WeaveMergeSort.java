package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.sorts.insert.InsertionSort;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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

public final class WeaveMergeSort extends Sort {
    public WeaveMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Weave Merge");
        this.setRunAllSortsName("Weave Merge Sort");
        this.setRunSortName("Weave Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    InsertionSort insert = new InsertionSort(arrayVisualizer);

    private void weaveMerge(int[] arr, int min, int max, int mid) {
        int i = 1;
        int target = (mid - min);

        while (i <= target) {
            Writes.multiSwap(arr, mid + i, min + (i * 2) - 1, 0.05, true, false);
            i++;
        }

        insert.customInsertSort(arr, min, max + 1, 0.05, false);
    }

    private void weaveMergeSort(int[] array, int min, int max, int depth) {
        if (max - min == 0) { // only one element.
            Delays.sleep(1); // no swap
        } else if (max - min == 1) { // only two elements and swaps them
            if (Reads.compareIndices(array, min, max, 0.1, true) == 1) {
                Writes.swap(array, min, max, 0.01, true, false);
            }
        } else {
            int mid = (int) Math.floor((min + max) / 2); // The midpoint
            Writes.recordDepth(depth);
            Writes.recursion();
            this.weaveMergeSort(array, min, mid, depth + 1); // sort the left side
            Writes.recursion();
            this.weaveMergeSort(array, mid + 1, max, depth + 1); // sort the right side
            this.weaveMerge(array, min, max, mid); // combines them
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.weaveMergeSort(array, 0, currentLength - 1, 0);
    }
}
