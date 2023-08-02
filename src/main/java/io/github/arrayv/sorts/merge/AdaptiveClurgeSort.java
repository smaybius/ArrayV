package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveClurgeSort extends Sort {
    public AdaptiveClurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Clurge");
        this.setRunAllSortsName("Adaptive Clurge Sort");
        this.setRunSortName("Adaptive Clurge Sort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void method(int[] array, int start, int len) {
        int left = start;
        int right = start + (len / 2);
        int searchbound = left;
        boolean broken = false;
        while (right < start + len && !broken) {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            if (Reads.compareIndices(array, right - 1, right, 0.5, true) > 0) {
                Highlights.markArray(3, searchbound);
                Delays.sleep(1);
                while (Reads.compareIndices(array, searchbound, right, 0.5, true) <= 0) {
                    searchbound++;
                    Highlights.markArray(3, searchbound);
                    Delays.sleep(1);
                }
                left = searchbound;
                Highlights.markArray(3, searchbound);
                while (left < right) {
                    Writes.swap(array, left, right, 0.2, true, false);
                    left++;
                }
                Highlights.clearMark(1);
                searchbound++;
            } else {
                Delays.sleep(2);
                Highlights.clearMark(3);
                broken = true;
            }
            right++;
        }
    }

    protected void nonpow2(int[] array, int start, int len, int mid) {
        int left = start;
        int right = mid;
        int searchbound = left;
        while (right < start + len) {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            if (Reads.compareIndices(array, right - 1, right, 0.5, true) > 0) {
                Highlights.markArray(3, searchbound);
                Delays.sleep(1);
                int set = searchbound != start ? searchbound - 1 : start;
                if (Reads.compareIndices(array, set, right, 0.5, true) <= 0) {
                    while (Reads.compareIndices(array, searchbound, right, 0.5, true) <= 0) {
                        searchbound++;
                        Highlights.markArray(3, searchbound);
                        Delays.sleep(1);
                    }
                } else {
                    searchbound = start;
                    while (Reads.compareIndices(array, searchbound, right, 0.5, true) <= 0) {
                        searchbound++;
                        Highlights.markArray(3, searchbound);
                        Delays.sleep(1);
                    }
                }
                left = searchbound;
                Highlights.markArray(3, searchbound);
                while (left < right) {
                    Writes.swap(array, left, right, 0.2, true, false);
                    left++;
                }
                Highlights.clearMark(1);
                searchbound++;
            } else {
                Delays.sleep(2);
                Highlights.clearMark(3);
            }
            right++;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = 2;
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len - 1 < currentLength) {
                method(array, index, len);
                index += len;
            }
            len *= 2;
        }
        if (len == currentLength)
            method(array, 0, currentLength);
        else
            nonpow2(array, 0, currentLength, len / 2);
    }
}