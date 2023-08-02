package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class MoreUnoptimizedBubbleSort extends Sort {
    public MoreUnoptimizedBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("More Unoptimized Bubble");
        this.setRunAllSortsName("More Unoptimized Bubble Sort");
        this.setRunSortName("More Unoptimized Bubblesort");
        this.setCategory("Exchange Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {

        for (int j = 0; j < sortLength - 1; j++) {
            for (int i = 0; i < sortLength - 1; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.5, true) == 1)
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + 1);
                Delays.sleep(0.025);
            }
        }
    }
}