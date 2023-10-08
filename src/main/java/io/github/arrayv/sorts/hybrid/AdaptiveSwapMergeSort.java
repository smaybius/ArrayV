package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author Gaming32
 * @author Kiriko-chan
 *
 */
@SortMeta(name = "Adaptive SwapMerge")
public final class AdaptiveSwapMergeSort extends Sort {

    public AdaptiveSwapMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;

        return this.rightBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    private int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;

        return this.rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);

        if (a > b) {
            int temp = array[a];

            do
                Writes.write(array, a, array[--a], 0.25, true, false);
            while (a > b);

            Writes.write(array, b, temp, 0.25, true, false);
        }
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, rightExpSearch(array, a, i, array[i]));
    }

    protected void merge(int[] array, int a, int m, int b) {
        int i = a, j = m;
        while (i < j && j < b) {
            if (Reads.compareIndices(array, i, j, 0.5, true) > 0)
                Writes.multiSwap(array, j++, i, 0.025, true, false);
            i++;
        }
    }

    public void smartMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.125, true) <= 0)
            return;
        a = leftBoundSearch(array, a, m, array[m]);
        merge(array, a, m, b);
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a < 32) {
            insertSort(array, a, b);
            return;
        }
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        smartMerge(array, a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        mergeSort(array, 0, sortLength);

    }

}
