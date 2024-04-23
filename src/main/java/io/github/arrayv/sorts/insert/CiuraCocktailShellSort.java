package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author fungamer2
 *
 */
@SortMeta(name = "Cocktail Shell (Ciura Gaps)")
public final class CiuraCocktailShellSort extends Sort {

    /**
     * @param arrayVisualizer
     */
    public CiuraCocktailShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    int[] gaps = { 1, 4, 10, 23, 57, 132, 301, 701 };

    private int ciura(int n) {
        if (n <= gaps.length) {
            return gaps[n - 1];
        }
        return (int) Math.pow(2.25, n);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int gap = 1;
        int k;
        for (k = 1; gap < sortLength; k++) {
            gap = ciura(k);
        }

        boolean dir = true;
        while (--k >= 1) {
            gap = ciura(k);
            if (dir) {
                for (int i = gap; i < sortLength; i++) {
                    int tmp = array[i];
                    int j = i;
                    while (j >= gap && Reads.compareValues(array[j - gap], tmp) == 1) {
                        Highlights.markArray(2, j - gap);
                        Writes.write(array, j, array[j - gap], 0.7, true, false);
                        j -= gap;
                    }

                    if (j - gap >= 0) {
                        Highlights.markArray(2, j - gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            } else {
                for (int i = sortLength - gap; i >= 0; i--) {
                    int tmp = array[i];
                    int j = i;
                    while (j < sortLength - gap && Reads.compareValues(array[j + gap], tmp) == -1) {
                        Highlights.markArray(2, j + gap);
                        Writes.write(array, j, array[j + gap], 0.7, true, false);
                        j += gap;
                    }

                    if (j + gap < sortLength) {
                        Highlights.markArray(2, j + gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            }
            dir = !dir;
        }

    }

}
