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
@SortMeta(name = "Mean Partition")
public final class MeanPartitionSort extends Sort {

    boolean inlow;
    boolean inhigh;

    public MeanPartitionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected int partition(int[] array, int start, int end) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (array[i] < min) {
                min = array[i];
            }
            if (array[i] > max) {
                max = array[i];
            }
        }
        int middle = min + ((max - min) / 2) + 1;
        arrayVisualizer.setExtraHeading(" / Assumption: " + middle);
        int itemslow = 0;
        inlow = false;
        inhigh = false;
        for (int i = start; i < end; i++) {
            Highlights.markArray(1, i);
            Highlights.markArray(2, i);
            Delays.sleep(0.25);
            int cmp = Reads.compareValues(array[i], middle);
            if (cmp < 0) {
                if (start + itemslow != i)
                    Writes.swap(array, i, start + itemslow, 0.25, true, false);
                itemslow++;
                inlow = true;
            } else {
                Delays.sleep(0.25);
                inhigh = true;
            }
        }
        return itemslow;
    }

    public void presumepartitions(int[] array, int start, int end, int depth) {
        if (end - start >= 2) {
            int m = partition(array, start, end);
            if (inlow && inhigh) {
                presumepartitions(array, start, start + m, depth + 1);
                presumepartitions(array, start + m, end, depth + 1);
            }
        }
        arrayVisualizer.setExtraHeading("");
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        presumepartitions(array, 0, currentLength, 0);
    }
}
