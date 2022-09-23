package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

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

public final class TernaryInsertionSort extends Sort {
    public TernaryInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Ternary Insertion");
        this.setRunAllSortsName("Ternary Insertion Sort");
        this.setRunSortName("Ternary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int rightTernarySearch(int[] ar, int l, int r, int key, double searchdelay) {
        while (r >= l) {

            // Find the mid1 mid2
            int mid1 = l + (r - l) / 3;
            int mid2 = r - (r - l) / 3;

            // Check if key is present at any mid
            Highlights.markArray(1, mid1);
            Highlights.markArray(2, key);
            Delays.sleep(searchdelay);
            if (Reads.compareValues(ar[mid1], key) == 0) {
                Highlights.clearAllMarks();
                return mid1;
            }
            Highlights.markArray(1, mid2);
            Highlights.markArray(2, key);
            Delays.sleep(searchdelay);
            if (Reads.compareValues(ar[mid2], key) == 0) {
                Highlights.clearAllMarks();
                return mid2;
            }

            // Since key is not present at mid,
            // check in which region it is present
            // then repeat the Search operation
            // in that region
            Highlights.markArray(1, mid1);
            Highlights.markArray(2, key);
            Delays.sleep(searchdelay);
            if (Reads.compareValues(key, ar[mid1]) < 0) {

                // The key lies in between l and mid1
                r = mid1 - 1;
            } else if (Reads.compareValues(key, ar[mid2]) > 0) {
                Highlights.markArray(1, mid2);
                Highlights.markArray(2, key);
                Delays.sleep(searchdelay);

                // The key lies in between mid2 and r
                l = mid2 + 1;
            } else {

                // The key lies in between mid1 and mid2
                l = mid1 + 1;
                r = mid2 - 1;
            }
        }

        // Key not found
        Highlights.clearAllMarks();
        return -1;
    }

    private void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        if (a > b) {
            int temp = array[a];
            do
                Writes.write(array, a, array[--a], sleep, true, false);
            while (a > b);
            Writes.write(array, b, temp, sleep, true, false);
        }
    }

    private void insertionSort(int[] array, int a, int b, double compSleep, double writeSleep) {
        for (int i = a + 1; i < b; i++) {
            insertTo(array, i, rightTernarySearch(array, a, i, array[i], compSleep), writeSleep);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength, 2, 0.05);
    }
}