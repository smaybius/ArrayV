package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.sorts.insert.InsertionSort;

// From C#'s standard library (Array.Sort) https://github.com/dotnet/runtime/blob/main/src/libraries/System.Private.CoreLib/src/System/Array.cs

public final class IntroSort extends Sort {
    private int sizeThreshold = 16;

    public IntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Intro");
        // this.setRunAllID("Introspective Sort (std::sort)");
        this.setRunAllSortsName("Introspective Sort (std::sort on C++, Array.Sort on C#)");
        this.setRunSortName("Introsort (std::sort on C++, Array.Sort on C#)");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void SwapIfGreater(int[] keys, int a, int b) {
        if (a != b) {
            if (Reads.compareIndices(keys, a, b, 0.5, true) > 0) {
                Writes.swap(keys, a, b, 0, false, false);
            }
        }
    }

    private void IntrospectiveSort(int[] array, int left, int length) {
        if (length < 2)
            return;
        IntroSorter(array, left, length + left - 1, (int) (2 * (Math.log((int) length) + 1) / Math.log(2)));

    }

    private void IntroSorter(int[] array, int lo, int hi, int depthLimit) {

        while (hi > lo) {
            int partitionSize = hi - lo + 1;
            if (partitionSize <= sizeThreshold) {

                if (partitionSize == 2) {
                    SwapIfGreater(array, lo, hi);
                    return;
                }

                if (partitionSize == 3) {
                    SwapIfGreater(array, lo, hi - 1);
                    SwapIfGreater(array, lo, hi);
                    SwapIfGreater(array, hi - 1, hi);
                    return;
                }

                InsertionSort(array, lo, hi);
                return;
            }

            if (depthLimit == 0) {
                Heapsort(array, lo, hi);
                return;
            }
            depthLimit--;

            int p = PickPivotAndPartition(array, lo, hi);
            IntroSorter(array, p + 1, hi, depthLimit);
            hi = p - 1;
        }
    }

    private int PickPivotAndPartition(int[] keys, int lo, int hi) {

        // Compute median-of-three. But also partition them, since we've done the
        // comparison.
        int mid = lo + (hi - lo) / 2;

        // Sort lo, mid and hi appropriately, then pick mid as the pivot.
        SwapIfGreater(keys, lo, mid);
        SwapIfGreater(keys, lo, hi);
        SwapIfGreater(keys, mid, hi);

        int pivot = keys[mid];
        Writes.swap(keys, mid, hi - 1, 1, true, false);
        int left = lo, right = hi - 1; // We already partitioned lo and hi and put the pivot in hi - 1. And we
                                       // pre-increment & decrement below.

        while (left < right) {
            Highlights.markArray(1, left);
            Delays.sleep(1);
            while (Reads.compareValues(keys[++left], pivot) < 0) {
                Highlights.markArray(1, left);
                Delays.sleep(1);
            }
            Highlights.markArray(1, right);
            Delays.sleep(1);
            while (Reads.compareValues(pivot, keys[--right]) < 0) {
                Highlights.markArray(1, right);
                Delays.sleep(1);
            }

            if (left >= right)
                break;

            Writes.swap(keys, left, right, 1, true, false);
        }

        // Put pivot in the right location.
        if (left != hi - 1) {
            Writes.swap(keys, left, hi - 1, 1, true, false);
        }
        return left;
    }

    private void Heapsort(int[] array, int lo, int hi) {
        int n = hi - lo + 1;
        for (int i = n / 2; i >= 1; i--) {
            DownHeap(array, i, n, lo);
        }
        for (int i = n; i > 1; i--) {
            Writes.swap(array, lo, lo + i - 1, 1, true, false);

            DownHeap(array, 1, i - 1, lo);
        }
    }

    private void DownHeap(int[] keys, int i, int n, int lo) {
        int d = keys[lo + i - 1];
        int child;
        while (i <= n / 2) {
            child = 2 * i;
            if (child < n && Reads.compareIndices(keys, lo + child - 1, lo + child, 1, true) < 0) {
                child++;
            }
            Highlights.markArray(1, lo + child - 1);
            Delays.sleep(1);
            if (!(Reads.compareValues(d, keys[lo + child - 1]) < 0))
                break;
            Writes.write(keys, lo + i - 1, keys[lo + child - 1], 1, true, false);
            i = child;
        }
        Writes.write(keys, lo + i - 1, d, 1, true, false);
    }

    private void InsertionSort(int[] keys, int lo, int hi) {
        InsertionSort insert = new InsertionSort(arrayVisualizer);
        insert.customInsertSort(keys, lo, hi, 1, false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        IntrospectiveSort(array, 0, length);
    }
}
