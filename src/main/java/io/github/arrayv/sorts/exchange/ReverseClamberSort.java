package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Reverse Clamber")
final public class ReverseClamberSort extends Sort {
    public ReverseClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = currentLength - 2;
        int right = currentLength - 1;
        while (left >= 0) {
            right = currentLength - 1;
            while (right > left) {
                if (Reads.compareIndices(array, left, right, 0.1, true) > 0)
                    Writes.swap(array, left, right, 0.1, true, false);
                right--;
            }
            left--;
        }
    }
}