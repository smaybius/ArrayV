package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BestForNSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BestForNRoomSort extends BestForNSorting {
    public BestForNRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Best For N Room");
        this.setRunAllSortsName("Best For N Room Sort");
        this.setRunSortName("Best For N Roomsort");
        this.setCategory("Impractical Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the run length for this sort:", 64);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2)
            return 2;
        if (answer > 64)
            return 64;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        for (int i = currentLength; i >= 0; i -= base - 1) {
            for (int j = 0; j + base <= i; j++) {

                initNetwork(array, j, base);
            }
        }

        initNetwork(array, 0, base);
    }
}