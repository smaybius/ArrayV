package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class UncookieSort extends CookieSort {

    public UncookieSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Uncookie");
        this.setRunAllSortsName("Uncookie Sort");
        this.setRunSortName("Uncookie Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        milkSortLen(array, 0, currentLength, 2);
    }
}