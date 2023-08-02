package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

final public class PaintSort extends Sort {
    public PaintSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Paint");
        this.setRunAllSortsName("Paint Sort");
        this.setRunSortName("Paintsort");
        this.setCategory("Exchange Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // doesn't work on Reverse Sorting for some reason

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length; i++) {
            for (int j = i; j < length; j++) {
                if (Reads.compareIndices(array, j, j + 1, 0.5, true) == 1) {
                    Writes.multiSwap(array, j, length - 1, 1, true, false);
                    j = i;
                } else if (j < length - 1)
                    Writes.swap(array, j, j + 1, 1, true, false);
                if (Reads.compareIndices(array, j, i, 0.5, true) == 1) {
                    Writes.swap(array, j, i, 1, true, false);
                }
            }
        }
        Writes.reversal(array, 0, length - 1, 1, true, false);
    }
}