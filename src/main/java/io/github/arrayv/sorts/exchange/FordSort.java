package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

@SortMeta(name = "Ford", unreasonableLimit = 8192, slowSort = true, category = "Impractical Sorts")
public final class FordSort extends BogoSorting {

    public FordSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int q = 0;
        while (!isArraySorted(array, length)) {
            for (int k = length / 2; k > 0; k--) {
                for (int i = q; i < length; i += 2 * k) {
                    int l = i;
                    boolean change = false;
                    while (l > 0 && Reads.compareIndices(array, l, l - 1, 0.5, true) == -1) {
                        Writes.swap(array, l, l - 1, 1, true, false);
                        l -= 2;
                        if (i > 0)
                            i--;
                        change = true;
                    }
                    if (change)
                        i++;
                    else
                        i--;
                }
            }
            q = (q + 1) & 1;
        }
    }

}
