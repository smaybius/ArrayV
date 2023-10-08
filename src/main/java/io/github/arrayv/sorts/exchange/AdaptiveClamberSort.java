package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Adaptive Clamber")
final public class AdaptiveClamberSort extends Sort {
    public AdaptiveClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0;
        int right = 1;
        while (right < currentLength) {
            if (Reads.compareIndices(array, right - 1, right, 0.5, true) > 0) {
                left = 0;
                while (left < right) {
                    if (Reads.compareIndices(array, left, right, 0.5, true) > 0) {
                        while (left < right) {
                            Writes.swap(array, left, right, 0.2, true, false);
                            left++;
                        }
                    }
                    left++;
                }
            }
            right++;
        }
    }
}