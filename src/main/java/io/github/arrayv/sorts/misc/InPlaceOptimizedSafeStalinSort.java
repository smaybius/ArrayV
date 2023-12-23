package io.github.arrayv.sorts.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.BlockInsertionSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

// #3 of Distray's Pop The Top Lineup
@SortMeta(name = "In-Place Optimized Safe Stalin", category = "Impractical Sorts", slowSort = true, unreasonableLimit = 2048)
final public class InPlaceOptimizedSafeStalinSort extends Sort {
    public InPlaceOptimizedSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int buildStalinRuns(int[] array, int start, int end) {
        int runs = 0;
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if (Reads.compareValues(array[j], array[i]) >= 0) {
                    if (++i != j)
                        Writes.swap(array, j, i, 1, true, false);
                }
            }
            runs++;
        }
        return runs;
    }

    private int getRun(int[] array, int start, int end) {
        int left = start;
        while (left < end && Reads.compareIndices(array, left, left + 1, 0.1, true) <= 0) {
            left++;
        }
        return left;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int runs;
        do {
            runs = buildStalinRuns(array, 0, currentLength);
            if (runs < 3)
                break;
            int left = getRun(array, 0, currentLength), right = getRun(array, left + 1, currentLength);
            int mid = left;
            while (mid > 0 && Reads.compareIndices(array, mid, right, 1, true) >= 0) {
                mid--;
            }
            IndexedRotations.adaptable(array, 0, left + 1, currentLength, 1, true, false);
            currentLength -= left - mid;
        } while (runs > 2);
        if (runs == 2) {
            BlockInsertionSort neon = new BlockInsertionSort(arrayVisualizer);
            neon.insertionSort(array, 0, currentLength);
        }
    }
}
