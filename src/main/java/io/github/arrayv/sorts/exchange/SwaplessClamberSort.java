package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*

Coded for ArrayV by Kiriko-chan

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 *
 */
@SortMeta(name = "Swapless Clamber")
public final class SwaplessClamberSort extends Sort {

    public SwaplessClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void clamber(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            int t = array[i];
            boolean change = false;
            for (int j = a; j < i; j++) {
                if (Reads.compareValueIndex(array, t, j, 0.125, true) < 0) {
                    int t2 = array[j];
                    Writes.write(array, j, t, 0.25, true, false);
                    t = t2;
                    change = true;
                }
            }
            if (change) {
                Writes.write(array, i, t, 0.25, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        clamber(array, 0, sortLength);

    }

}
