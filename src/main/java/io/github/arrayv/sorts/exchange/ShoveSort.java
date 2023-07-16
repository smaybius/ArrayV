package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author frankblob
 * @author PiotrGrochowski
 *
 */
public final class ShoveSort extends Sort {

    public ShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Shove");
        setRunAllSortsName("Shove Sort");
        setRunSortName("Shove Sort");
        setCategory("Impractical Sorts");
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(512);
        setBogoSort(false);

    }

    private void shovesort(int[] array, int start, int end, double sleep) {
        int i = start;
        while (i < end - 1) {
            if (this.Reads.compareIndices(array, i, i + 1, sleep, true) == 1) {
                for (int f = i; f < end - 1; f++) {
                    this.Writes.swap(array, f, f + 1, sleep, true, false);
                }
                if (i > start) {
                    i--;
                }
                continue;
            }
            i++;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        shovesort(array, 0, length, 0.125D);

    }

}
