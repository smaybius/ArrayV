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
@SortMeta(name = "Circle (Iterative, Inverse)")
final public class CircleSortInverseIterative extends Sort {
    public CircleSortInverseIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected boolean circle(int[] array, int a, int b, boolean anyswaps) {
        int left = a;
        int right = b;
        boolean swaphere = false;
        while (left < right) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(0.5);
            if (Reads.compareIndices(array, left, right, 0.5, true) > 0) {
                Writes.swap(array, left, right, 0.5, true, false);
                swaphere = true;
            }
            left++;
            right--;
        }
        if (anyswaps)
            return anyswaps;
        else
            return swaphere;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int offset = 0;
        int gap = 2;
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            gap = 2;
            while (gap <= currentLength) {
                offset = 0;
                while (offset + (gap - 1) < currentLength) {
                    anyswaps = circle(array, offset, offset + (gap - 1), anyswaps);
                    offset += gap;
                }
                gap *= 2;
            }
            if (gap / 2 != currentLength && !anyswaps) {
                // TODO: Figure something out to put here.
            }
        }
    }
}