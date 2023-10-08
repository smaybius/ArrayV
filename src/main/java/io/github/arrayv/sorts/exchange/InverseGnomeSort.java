package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Inverse Gnome")
final public class InverseGnomeSort extends Sort {
    public InverseGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 1; i < length; i++) {
            for (int g = 0; g < i; g++) {
                if (Reads.compareIndices(array, g, i, 0.5, true) == 1) {
                    Writes.multiSwap(array, g, i, 0.5, true, false);
                    g = -1;
                }
            }
        }
    }
}