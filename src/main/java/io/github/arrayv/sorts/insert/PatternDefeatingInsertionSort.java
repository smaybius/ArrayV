package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

//
@SortMeta(name = "Pattern-Defeating Insertion")
final public class PatternDefeatingInsertionSort extends Sort {
    public PatternDefeatingInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if (Reads.compareIndices(array, i - 1, i++, sleep, true) == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, sleep, true) == 1)
                i++;
            Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, sleep, true) <= 0)
                i++;

        Highlights.clearMark(2);

        while (i < b) {
            int current = array[i];
            int pos = i - 1;
            while (pos >= a && Reads.compareValues(array[pos], current) > 0) {
                Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                pos--;
            }
            Writes.write(array, pos + 1, current, sleep, true, auxwrite);

            i++;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength, 1, false);
    }
}