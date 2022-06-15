package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.GrailSorting;

public final class BlockInsertionSort extends GrailSorting {
    public BlockInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Block Insertion");
        this.setRunAllSortsName("Block Insertion Sort");
        this.setRunSortName("Block Insertsort");
        this.setCategory("Insertion Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) {
            i *= 2;
        }
        return rightBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    private void insertTo(int[] array, int a, int b, double delay, boolean auxwrite) {
        Highlights.clearMark(2);
        int temp = array[a];
        while (a > b)
            Writes.write(array, a, array[--a], delay / 2, true, false);
        Writes.write(array, b, temp, delay / 2, true, false);
    }

    private void insert2(int[] array, int a, int l, int r, double delay) { // credit to aphitorite for making it binary
                                                                           // instead of
        // linear
        int t1 = array[l], t2 = array[r];

        int m1 = rightExpSearch(array, a, l, t2);
        Writes.arraycopy(array, m1, array, m1 + 2, l - m1, delay / 2, true, false);
        Writes.write(array, m1 + 1, t2, delay, true, false);

        int m2 = rightExpSearch(array, a, m1, t1);
        Writes.arraycopy(array, m2, array, m2 + 1, m1 - m2, delay / 2, true, false);
        Writes.write(array, m2, t1, delay / 2, true, false);
    }

    private int findRun(int[] array, int a, int b, double delay, boolean auxwrite) {
        int i = a + 1;
        if (i == b)
            return i;
        if (Reads.compareIndices(array, i - 1, i++, delay / 4, true) == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, delay / 4, true) == 1)
                i++;
            Writes.reversal(array, a, i - 1, delay / 2, true, auxwrite);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, delay / 4, true) <= 0)
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
            if (len < 3) {
                if (len == 2) {
                    insert2(array, a, i, i + 1, delay);
                    // Could replace it with the grailLazyMerge, but keeping this to make sure it's
                    // perfectly swapless regardless of rotation used
                } else {
                    insertTo(array, i, this.rightExpSearch(array, a, i, array[i]), delay, auxwrite); // taken from
                                                                                                     // Laziest Stable
                }

            } else {
                grailMergeWithoutBuffer(array, a, i - a, len);
            }
            i = j;
        }
    }

    public void customInsertSort(int[] array, int start, int end, double delay, boolean auxwrite) {
        insertionSort(array, start, end, delay, false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength, 1, false);
    }
}
