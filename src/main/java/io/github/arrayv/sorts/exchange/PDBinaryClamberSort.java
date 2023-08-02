package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PDBinaryClamberSort extends Sort {
    public PDBinaryClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Binary Clamber");
        this.setRunAllSortsName("Pattern-Defeating Binary Clamber Sort");
        this.setRunSortName("Pattern-Defeating Binary Clambersort");
        this.setCategory("Exchange Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(1);
            if (Reads.compareValues(value, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int reverse = 0;
        boolean pd = true;
        while (reverse + 1 < currentLength) {
            Highlights.markArray(1, reverse);
            Highlights.markArray(2, reverse + 1);
            Delays.sleep(1);
            if (Reads.compareIndices(array, reverse, reverse + 1, 0.5, true) > 0)
                reverse++;
            else
                break;
        }
        if (reverse > 2)
            Writes.reversal(array, 0, reverse, 0.2, true, false);
        else {
            if (reverse > 0)
                Writes.swap(array, 0, reverse, 0.2, true, false);
            else
                pd = false;
        }
        int left = 0;
        int right = pd ? reverse + 1 : 2;
        while (right < currentLength) {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            Delays.sleep(1);
            if (Reads.compareIndices(array, right - 1, right, 0.5, true) > 0) {
                left = binarySearch(array, 0, right - 1, array[right]);
                while (left < right) {
                    Writes.swap(array, left, right, 0.2, true, false);
                    left++;
                }
            }
            right++;
        }
    }
}