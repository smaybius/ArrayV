package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.merge.NaturalRotateMergeSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LoopSort extends Sort {
    public LoopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Loop");
        this.setRunAllSortsName("Loop Sort");
        this.setRunSortName("Loop Sort");
        this.setCategory("Exchange Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int loot(int[] array, int start, int end) {
        int collect = 0;
        int i = start;
        for (; i + collect < end; i++) {
            if (i > start) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) < 0) {
                    i--;
                    collect++;
                }
            }
            if (Reads.compareIndices(array, i + collect, i + collect + 1, 0.1, true) <= 0) {
                i--;
                collect++;
            } else
                Writes.insert(array, i + collect + 1, i, 0.1, true, false);
        }
        IndexedRotations.adaptable(array, start, i, end, 0.1, true, false);
        return start + collect;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int looping = 0;
        while (looping < currentLength - 1)
            looping = loot(array, looping, currentLength);
        NaturalRotateMergeSort merge = new NaturalRotateMergeSort(arrayVisualizer);
        merge.runSort(array, currentLength, 0);
    }
}