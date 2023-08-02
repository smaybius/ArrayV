package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class NaturalHeadPullSort extends Sort {
    public NaturalHeadPullSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Head Pull");
        this.setRunAllSortsName("Natural Head Pull Sort");
        this.setRunSortName("Natural Head Pull Sort");
        this.setCategory("Impractical Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 1;
        int pull = 1;
        int verifyi = 1;
        boolean currentswap = true;
        boolean verifypass = false;
        while (!verifypass) {
            i = verifyi;
            currentswap = true;
            while (i < currentLength && currentswap) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, i);
                Delays.sleep(0.01);
                if (Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) {
                    pull = i;
                    while (pull >= 1) {
                        Writes.swap(array, pull - 1, pull, 0.01, true, false);
                        pull--;
                    }
                    i++;
                } else
                    currentswap = false;
            }
            verifyi = 1;
            verifypass = true;
            while (verifyi < currentLength && verifypass) {
                Highlights.markArray(1, verifyi - 1);
                Highlights.markArray(2, verifyi);
                Delays.sleep(0.01);
                if (Reads.compareIndices(array, verifyi - 1, verifyi, 0.5, true) <= 0)
                    verifyi++;
                else
                    verifypass = false;
            }
        }
    }
}