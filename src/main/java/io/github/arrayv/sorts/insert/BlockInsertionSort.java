package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;
import io.github.arrayv.utils.Searches;

public final class BlockInsertionSort extends Sort {
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

    private void rotate(int[] array, int a, int m, int b) {
        Highlights.clearMark(2);
        IndexedRotations.adaptable(array, a, m, b, 0.5, true, false);
    }

    private void inPlaceMerge(int[] array, int a, int m, int b) {
        int i = a;
        int j = m;
        int k;

        while (i < j && j < b) {
            if (Reads.compareIndices(array, i, j, 0.2, true) > 0) {
                k = Searches.leftExpSearch(array, j + 1, b, array[i], 0);
                this.rotate(array, i, j, k);

                i += k - j;
                j = k;
            } else
                i++;
        }
    }

    private void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m - 1;
        int j = b - 1;
        int k;

        while (j > i && i >= a) {
            if (Reads.compareIndices(array, i, j, 0.2, true) > 0) {
                k = Searches.rightExpSearch(array, a, i, array[j], 0);
                this.rotate(array, k, i + 1, j + 1);

                j -= (i + 1) - k;
                i = k - 1;
            } else
                j--;
        }
    }

    private void mergeWithoutBuf(int[] array, int a, int m, int b) {
        if (m - a > b - m)
            this.inPlaceMergeBW(array, a, m, b);
        else
            this.inPlaceMerge(array, a, m, b);
    }

    private void insert2(int[] array, int a, int l, int r, double delay) { // credit to aphitorite for making it binary
                                                                           // instead of
        // linear
        int t1 = array[l];
        int t2 = array[r];

        int m1 = Searches.rightExpSearch(array, a, l, t2, delay);
        Writes.arraycopy(array, m1, array, m1 + 2, l - m1, delay / 2, true, false);
        Writes.write(array, m1 + 1, t2, delay, true, false);

        int m2 = Searches.rightExpSearch(array, a, m1, t1, delay);
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
        int i;
        int j;
        int len;
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
                    Searches.insertTo(array, i, Searches.rightExpSearch(array, a, i, array[i], delay), delay, auxwrite); // taken
                    // from
                    // Laziest Stable
                }

            } else {
                mergeWithoutBuf(array, a, i - a, j);
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
