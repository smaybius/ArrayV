package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * This version of Odd-Even Merge Sort was taken from here, written by H.W. Lang:
 * http://www.inf.fh-flensburg.de/lang/algorithmen/sortieren/networks/oemen.htm
 *
 * Thanks to Piotr Grochowski for rewriting code to allow this implementation to
 * work for array lengths other than powers of two!
 */

public final class OddEvenMergeSortRecursive extends Sort {
    public OddEvenMergeSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Odd-Even Merge (Recursive)");
        this.setRunAllSortsName("Batcher's Odd-Even Merge Sort");
        this.setRunSortName("Recursive Odd-Even Mergesort");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void oddEvenMergeCompare(int[] array, int i, int j) {
        if (Reads.compareIndices(array, i, j, 0.5, true) > 0)
            Writes.swap(array, i, j, 0.5, true, false);
    }

    /**
     * lo is the starting position,
     * m2 is the halfway point, and
     * n is the length of the piece to be merged,
     * r is the distance of the elements to be compared
     */
    private void oddEvenMerge(int[] array, int lo, int m2, int n, int r, int depth) {
        int m = r * 2;
        if (m < n) {
            if ((n / r) % 2 != 0) {
                Writes.recordDepth(depth++);
                Writes.recursion(2);
                oddEvenMerge(array, lo, (m2 + 1) / 2, n + r, m, depth); // even subsequence
                oddEvenMerge(array, lo + r, m2 / 2, n - r, m, depth); // odd subsequence
            } else {
                Writes.recordDepth(depth++);
                Writes.recursion(2);
                oddEvenMerge(array, lo, (m2 + 1) / 2, n, m, depth); // even subsequence
                oddEvenMerge(array, lo + r, m2 / 2, n, m, depth); // odd subsequence
            }

            if (m2 % 2 != 0) {
                for (int i = lo; i + r < lo + n; i += m) {
                    oddEvenMergeCompare(array, i, i + r);
                }
            } else {
                for (int i = lo + r; i + r < lo + n; i += m) {
                    oddEvenMergeCompare(array, i, i + r);
                }
            }
        } else {
            if (n > r) {
                oddEvenMergeCompare(array, lo, lo + r);
            }
        }
    }

    void oddEvenMergeSort(int[] array, int lo, int n, int depth) {
        if (n > 1) {
            int m = n / 2;
            Writes.recordDepth(depth++);
            Writes.recursion(2);
            oddEvenMergeSort(array, lo, m, depth);
            oddEvenMergeSort(array, lo + m, n - m, depth);
            oddEvenMerge(array, lo, m, n, 1, depth);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.oddEvenMergeSort(array, 0, sortLength, 0);
    }
}
