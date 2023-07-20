package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES
FROM A VARIANT OF STUPID FIRE SORT BY NAOAN1201

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SimpleStupidFireSort extends Sort {
    public SimpleStupidFireSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Simple Stupid Fire");
        this.setRunAllSortsName("Simple Stupid Fire Sort");
        this.setRunSortName("Simple Stupid Fire Sort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int twist = 1;
        int limit = currentLength;
        int i = 0;
        int moves = 0;
        while (i < currentLength || twist == -1) {
            if (i == 0 && twist == -1) {
                twist *= -1;
                moves = 0;
            }
            for (int j = i; (twist == -1 ? j < currentLength : j > 0)
                    && Reads.compareIndices(array, j - 1, j, 0.5, true) == twist; j -= twist) {
                moves++;
                Writes.swap(array, j, j - 1, 0.001, true, false);
            }
            i += twist;
            if (moves > limit) {
                limit++;
                moves = 0;
                twist *= -1;
            }
        }
    }
}