package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StableForwardRunShoveSort extends Sort {
    public StableForwardRunShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Forward Run Shove");
        this.setRunAllSortsName("Stable Forward Run Shove Sort");
        this.setRunSortName("Stable Forward Run Shove Sort");
        this.setCategory("Impractical Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int right = currentLength;
        int pull = 1;
        while (left != currentLength) {
            right = currentLength;
            while (left < right) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.0125);
                if (Reads.compareIndices(array, left - 1, right - 1, 0.5, true) > 0) {
                    pull = left;
                    while (pull < right) {
                        if (Reads.compareIndices(array, pull - 1, pull, 0.5, true) != 0)
                            Writes.swap(array, pull - 1, pull, 0.0125, true, false);
                        pull++;
                    }
                } else
                    right--;
            }
            left++;
        }
    }
}