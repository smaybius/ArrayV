package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

// From C#'s standard library (Array.Sort) https://github.com/dotnet/runtime/blob/main/src/libraries/System.Private.CoreLib/src/System/Array.cs
@SortMeta(listName = "Intro", showcaseName = "Introspective Sort (std::sort on C++, Array.Sort on C#)", runName = "Introspective Sort (std::sort on C++, Array.Sort on C#)", question = "Enter max recursions before fallback (0: log n)", defaultAnswer = 0)
public final class IntroSort extends Sort {
    private static int IntrosortSizeThreshold = 16;

    public IntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void SwapIfGreater(int[] keys, int a, int b) {
        if (a != b) {
            if (Reads.compareIndices(keys, a, b, 1, true) > 0) {
                Writes.swap(keys, a, b, 0.5, true, false);
            }
        }
    }

    private void introSort(int[] keys, int lo, int hi, int depthLimit) {
        while (hi > lo) {
            int partitionSize = hi - lo + 1;
            if (partitionSize <= IntrosortSizeThreshold) {
                if (partitionSize == 2) {
                    SwapIfGreater(keys, lo, hi);
                    return;
                }

                if (partitionSize == 3) {
                    SwapIfGreater(keys, lo, hi - 1);
                    SwapIfGreater(keys, lo, hi);
                    SwapIfGreater(keys, hi - 1, hi);
                    return;
                }

                insertionSort(keys, lo, hi);
                return;
            }

            if (depthLimit == 0) {
                Heapsort(keys, lo, hi);
                return;
            }
            depthLimit--;

            int p = PickPivotAndPartition(keys, lo, hi);
            introSort(keys, p + 1, hi, depthLimit);
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
            while (Reads.compareValues(keys[++left], pivot) < 0) {
                Highlights.markArray(1, left);
                Delays.sleep(1);
            }
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

    private void Heapsort(int[] keys, int lo, int hi) {
        int n = hi - lo + 1;
        for (int i = n / 2; i >= 1; i--) {
            DownHeap(keys, i, n, lo);
        }
        for (int i = n; i > 1; i--) {
            Writes.swap(keys, lo, lo + i - 1, 0, true, false);

            DownHeap(keys, 1, i - 1, lo);
        }
    }

    private void DownHeap(int[] keys, int i, int n, int lo) {
        int d = keys[lo + i - 1];
        int child;
        while (i <= n / 2) {
            child = 2 * i;
            if (child < n && Reads.compareIndices(keys, lo + child - 1, lo + child, 0.5, true) < 0) {
                child++;
            }
            if (!(Reads.compareValues(d, keys[lo + child - 1]) < 0)) {
                Highlights.markArray(1, lo + child - 1);
                Delays.sleep(0.5);
                break;
            }
            Writes.write(keys, lo + i - 1, keys[lo + child - 1], 0.5, true, false);
            i = child;
        }
        Writes.write(keys, lo + i - 1, d, 0.2, true, false);
    }

    private void insertionSort(int[] keys, int lo, int hi) {
        int i, j;
        int t;
        for (i = lo; i < hi; i++) {
            j = i;
            t = keys[i + 1];
            while (j >= lo && Reads.compareValues(t, keys[j]) < 0) {
                Highlights.markArray(1, j);
                Delays.sleep(1);
                Writes.write(keys, j + 1, keys[j], 0.5, true, false);
                j--;
            }
            Writes.write(keys, j + 1, t, 1, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        introSort(array, 0, length - 1,
                bucketCount == 0 ? (int) (2 * (Math.log(length + 1) / Math.log(2))) : bucketCount);
    }
}
