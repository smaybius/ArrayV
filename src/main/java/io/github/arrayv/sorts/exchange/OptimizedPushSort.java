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
@SortMeta(name = "Optimized Push")
final public class OptimizedPushSort extends Sort {
    public OptimizedPushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean anyswaps = true;
        int i = 1;
        int first = 1;
        while (anyswaps) {
            anyswaps = false;
            if (first > 1)
                i = first - 1;
            else
                i = 1;
            int gap = 1;
            while (i + gap <= currentLength) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, (i - 1) + gap);
                Delays.sleep(0.01);
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.5, true) > 0) {
                    for (int j = 1; j <= gap; j++)
                        Writes.swap(array, i - 1, (i - 1) + j, 0.01, true, false);
                    if (!anyswaps)
                        first = i;
                    anyswaps = true;
                    gap++;
                } else
                    i++;
            }
        }
    }
}