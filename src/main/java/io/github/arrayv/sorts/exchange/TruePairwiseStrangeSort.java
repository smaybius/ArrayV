package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class TruePairwiseStrangeSort extends Sort {
    public TruePairwiseStrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("True Pairwise Strange");
        this.setRunAllSortsName("True Pairwise Strange Sort");
        this.setRunSortName("True Pairwise Strangesort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int end, int bucketCount) {
        int swaps = 0;
        int start = 0;
        int gap = 1;
        do {
            swaps = 0;
            int a = 1;
            int b = start + gap;
            while (b < end) {
                if (Reads.compareIndices(array, b - gap, b, 0.5, true) == 1) {
                    Writes.swap(array, b - gap, b, 0, true, false);
                    swaps++;
                }
                b += (2 * gap);
            }
            while (a < ((end - start) / gap)) {
                a = (a * 2) + 1;
            }
            b = start + gap;
            while (b + gap < end) {
                int c = a;
                while (c > 1) {
                    c /= 2;
                    if (b + (c * gap) < end) {
                        if (Reads.compareIndices(array, b, b + (c * gap), 0.25, true) == 1) {
                            Writes.swap(array, b, b + (c * gap), 0, true, false);
                            swaps++;
                        }
                    }
                }
                b += (2 * gap);
            }
        } while (swaps != 0);
    }
}
