package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class BoseNelsonSortRecursive extends Sort {

    public BoseNelsonSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bose-Nelson (Recursive)");
        this.setRunAllSortsName("Recursive Bose-Nelson Sorting Network");
        this.setRunSortName("Recursive Bose-Nelson Sort");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void compareSwap(int[] array, int start, int end, double sleep) {
        if (Reads.compareIndices(array, start, end, sleep, true) == 1) {
            Writes.swap(array, start, end, 2 * sleep, true, false);
        }
    }

    private void boseNelson(int[] array, int start, int length, double sleep, int depth) {

        if (length > 1) {
            int mid = length / 2;
            Writes.recordDepth(depth++);
            Writes.recursion(2);
            boseNelson(array, start, mid, sleep, depth);
            boseNelson(array, start + mid, length - mid, sleep, depth);
            boseNelsonMerge(array, start, mid, start + mid, length - mid, sleep, depth);
        }
    }

    private void boseNelsonMerge(int[] array, int start1, int len1, int start2, int len2, double sleep, int depth) {

        if (len1 == 1 && len2 == 1) {
            compareSwap(array, start1, start2, sleep);
        } else if (len1 == 1 && len2 == 2) {
            compareSwap(array, start1, start2 + 1, sleep);
            compareSwap(array, start1, start2, sleep);
        } else if (len1 == 2 && len2 == 1) {
            compareSwap(array, start1, start2, sleep);
            compareSwap(array, start1 + 1, start2, sleep);
        } else {
            int mid1 = len1 / 2;
            int mid2 = len1 % 2 == 1 ? len2 / 2 : (len2 + 1) / 2;
            Writes.recordDepth(depth++);
            Writes.recursion(3);
            boseNelsonMerge(array, start1, mid1, start2, mid2, sleep, depth);
            boseNelsonMerge(array, start1 + mid1, len1 - mid1, start2 + mid2, len2 - mid2, sleep, depth);
            boseNelsonMerge(array, start1 + mid1, len1 - mid1, start2, mid2, sleep, depth);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.boseNelson(array, 0, length, 0.25, 0);
    }
}
