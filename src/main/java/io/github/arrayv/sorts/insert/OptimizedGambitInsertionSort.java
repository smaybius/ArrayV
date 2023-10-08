package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Optimized Gambit Insertion")
final public class OptimizedGambitInsertionSort extends Sort {

    BinaryInsertionSort binsert = new BinaryInsertionSort(arrayVisualizer);

    public OptimizedGambitInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3)
            Writes.swap(array, start, end, 0.075, true, false);
        else
            Writes.reversal(array, start, end, 0.075, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end)
                i++;
            right = i;
            if (left != right) {
                if (right - left < 3)
                    Writes.swap(array, left, right, 0.75, true, false);
                else
                    Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    protected int pd(int[] array, int currentLength) {
        int reverse = 0;
        boolean lessunique = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < currentLength) {
            if (cmp == 0)
                lessunique = true;
            reverse++;
            cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > 0) {
            if (lessunique)
                stableSegmentReversal(array, 0, reverse);
            else if (reverse < 3)
                Writes.swap(array, 0, reverse, 0.75, true, false);
            else
                Writes.reversal(array, 0, reverse, 0.75, true, false);
        }
        return reverse;
    }

    protected int gambitSearch(int[] array, int begin, int end, int target) {
        while (true) {
            int delta = end - begin;
            if (delta <= 0)
                break;
            int p = begin + delta / 2;
            if (Reads.compareIndices(array, p, target, 0.5, true) == 0)
                return p;

            if (Reads.compareIndices(array, p, target, 0.5, true) > 0) {
                end = p;
                continue;
            }
            begin = p + 1;
        }
        return end;
    }

    protected void gambitInsert(int[] array, int len, int start, int end) {
        int offset = 1;
        for (; offset * offset < len; offset *= 2)
            ;
        for (int bStart = 0, bEnd = end, i = start + offset; i < end; i++) {
            if (Reads.compareIndices(array, i - 1, i, 0.25, true) > 0) {
                int target = gambitSearch(array, bStart, bEnd, i);
                int tmp = array[i];
                int j = i - 1;
                while (j >= target && array[j] > tmp) {
                    Writes.write(array, j + 1, array[j], 0.125, true, false);
                    j--;
                }
                array[j + 1] = tmp;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int truestart = pd(array, currentLength);
        if (truestart + 1 < currentLength) {
            gambitInsert(array, currentLength, truestart, currentLength);
            Highlights.clearAllMarks();
            binsert.customBinaryInsert(array, 0, currentLength, 0.25);
        }
    }
}