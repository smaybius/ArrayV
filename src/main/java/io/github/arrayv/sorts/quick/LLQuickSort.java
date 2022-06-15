package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class LLQuickSort extends Sort {
    public LLQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Left/Left Quick");
        this.setRunAllSortsName("Quick Sort, Left/Left Pointers");
        this.setRunSortName("Left/Left Quicksort");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int partition(int[] array, int lo, int hi) {
        int pivot = array[hi];
        int i = lo;

        for (int j = lo; j < hi; j++) {
            Highlights.markArray(1, j);
            if (Reads.compareValues(array[j], pivot) < 0) {
                Writes.swap(array, i, j, 1, true, false);
                i++;
            }
            Delays.sleep(1);
        }
        Writes.swap(array, i, hi, 1, true, false);
        return i;
    }

    private void quickSort(int[] array, int lo, int hi, int depth) {
        if (lo < hi) {
            int p = this.partition(array, lo, hi);
            Writes.recordDepth(depth);
            Writes.recursion();
            this.quickSort(array, lo, p - 1, depth + 1);
            Writes.recursion();
            this.quickSort(array, p + 1, hi, depth + 1);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1, 0);
    }
}
