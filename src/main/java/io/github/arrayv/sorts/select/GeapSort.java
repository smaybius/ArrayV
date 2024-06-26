package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.HeapSorting;

//
@SortMeta(name = "Geap")
final public class GeapSort extends HeapSorting {
    public GeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void makeGeap(int[] array, int start, int end, double sleep) {
        for (int i = end - 1; i > start; i--) {
            int j = i;
            while (j >= start) {
                int next = ((j + start - 2) / 2),
                        child = ((j + start) / 2);
                if (child >= start && next >= start && Reads.compareIndices(array, child, next, sleep / 8, true) == 1)
                    Writes.swap(array, child, next, 0, true, false);
                if (next >= start && Reads.compareIndices(array, next, j, sleep / 8, true) == -1)
                    Writes.swap(array, j, child, 0, true, false);
                j = next;
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.makeGeap(array, 0, length, 2);
        for (int i = length - 1; i >= 0; i--) {
            Writes.swap(array, 0, --length, 1, true, false);
            this.makeGeap(array, 0, length, 1);
        }
    }
}