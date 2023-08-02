package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

final public class TrainTrackSort extends Sort {
    public TrainTrackSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Train Track");
        this.setRunAllSortsName("Train Track Sort");
        this.setRunSortName("Train Track Sort");
        this.setCategory("Exchange Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int l, int bucketCount) throws Exception {
        int a = 0;
        for (int b = 0; b < l; b++) {
            a = l - b - 1;
            for (int i = 0; i < l && a < l; i++) {
                for (int j = 0; j < l && a < l; j++) {
                    if ((a > j) ^ Reads.compareIndices(array, a, j, 0.001, true) == 1) {
                        Writes.swap(array, a++, j, 1, true, false);
                    } else if (a > i)
                        a--;
                    else
                        a++;
                }
            }
        }
    }
}