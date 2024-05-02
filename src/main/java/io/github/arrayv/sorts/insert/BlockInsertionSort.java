package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.GrailSorting;
import io.github.arrayv.utils.Searches;

//
@SortMeta(listName = "Block Insertion", runName = "Block Insertion Sort")
public final class BlockInsertionSort extends GrailSorting {
    public BlockInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int findRun(int[] array, int a, int b, double delay, boolean auxwrite) {
        int i = a + 1;
        if (i == b)
            return i;
        if (Reads.compareIndices(array, i - 1, i++, delay, true) == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, delay, true) == 1)
                i++;
            Writes.reversal(array, a, i - 1, delay, true, auxwrite);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, delay, true) <= 0)
                i++;
        Highlights.clearMark(2);
        return i;
    }

    public void insertionSort(int[] array, int a, int b, double delay, boolean auxwrite) {
        int i, j, len;
        i = findRun(array, a, b, delay, auxwrite);
        while (i < b) {
            j = findRun(array, i, b, delay, auxwrite);
            len = j - i;
            if (len == 1) {
                Searches.insertTo(array, i, Searches.rightExpSearch(array, a, i, array[i], delay), delay, auxwrite); // taken
                // from
                // Laziest Stable

            } else {
                grailMergeWithoutBuffer(array, a, i - a, len);
            }
            i = j;
        }
    }

    public void insertionSort(int[] array, int start, int end) {
        insertionSort(array, start, end, 1, false);
    }

    public void customInsertSort(int[] array, int start, int end, double delay, boolean auxwrite) {
        insertionSort(array, start, end, delay, auxwrite);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength, 1, false);
    }
}
