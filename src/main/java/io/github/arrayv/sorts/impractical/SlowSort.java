package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

// Code refactored from Python: http://wiki.c2.com/?SlowSort

public final class SlowSort extends Sort {
    public SlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Slow");
        this.setRunAllSortsName("Slow Sort");
        this.setRunSortName("Slowsort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(150);
        this.setBogoSort(false);
    }

    private void slowSort(int[] A, int i, int j, int depth) {
        if (i >= j) {
            return;
        }

        int m = i + ((j - i) / 2);
        Writes.recordDepth(depth);
        Writes.recursion();
        this.slowSort(A, i, m, depth + 1);
        Writes.recursion();
        this.slowSort(A, m + 1, j, depth + 1);

        if (Reads.compareIndices(A, m, j, 0, true) == 1) {
            Writes.swap(A, m, j, 1, true, false);
        }

        Highlights.markArray(1, j);
        Highlights.markArray(2, m);
        Writes.recordDepth(depth);
        Writes.recursion();
        this.slowSort(A, i, j - 1, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.slowSort(array, 0, currentLength - 1, 0);
    }
}
