package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Timer;

@SortMeta(name = "Branchless Bubble")
final public class BranchlessBubbleSort extends Sort {
    private Timer Timer;

    public BranchlessBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private boolean compSwap(int[] a, int l, int r) {
        int c;

        Reads.addComparison();
        Timer.startLap("Compare");
        c = (a[r] - a[l]) >> 31;
        Timer.stopLap();

        Writes.write(a, l, a[l] ^ (c & a[r]), 0.025, true, false);
        Writes.write(a, r, a[r] ^ (c & a[l]), 0.025, true, false);
        Writes.write(a, l, a[l] ^ (c & a[r]), 0.025, true, false);

        return c < 0;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean sorted = false;
        Timer = arrayVisualizer.getTimer();
        for (int i = length - 1; i > 0 && !sorted; i--) {
            sorted = true;
            for (int j = 0; j < i; j++) {
                sorted = !compSwap(array, j, j + 1) && sorted;
            }
        }
    }
}