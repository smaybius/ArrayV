package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
Idea made by Control#2866 in The Studio Discord Server (https://discord.com/invite/2xGkKC2)
*/
@SortMeta(name = "Matrix (Real)", runName = "Real Matrix Sort")
final public class MatrixSortReal extends Sort {

    public MatrixSortReal(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void bitonicSort(int[] array, int p, int g, int n, int cmp) {
        for (int k = n; k > 1; k /= 2)
            for (int j = 0; j < n; j += k)
                for (int i = 0; i < k / 2; i++)
                    if (Reads.compareIndices(array, p + (j + i) * g, p + (j + i + k / 2) * g, 0.125, true) == cmp)
                        Writes.swap(array, p + (j + i) * g, p + (j + i + k / 2) * g, 0.125, false, false);
    }

    private void sort(int[] array, int p, int g, int n, int cmp, int log) {
        if (n == 2) {
            if (Reads.compareIndices(array, p, p + g, 0.125, true) == cmp)
                Writes.swap(array, p, p + g, 0.125, false, false);
            return;
        }
        int sqrtLogRow = (log + 1) / 2;
        int sqrtLogCol = log / 2;

        int iter = sqrtLogCol;
        int sqrtRow = 1 << sqrtLogRow;
        int sqrtCol = 1 << sqrtLogCol;

        while (iter-- > 0) {
            for (int i = 0; i < sqrtCol; i++)
                this.sort(array, p + (i * sqrtRow) * g, g, sqrtRow, (2 * (i & 1) - 1) * cmp, sqrtLogRow);
            for (int i = 0; i < sqrtRow; i++)
                this.sort(array, p + i * g, sqrtRow * g, sqrtCol, cmp, sqrtLogCol);
        }
        for (int i = 0; i < sqrtCol; i++)
            this.bitonicSort(array, p + (i * sqrtRow) * g, g, sqrtRow, cmp);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
        this.sort(array, 0, 1, length, 1, 31 - Integer.numberOfLeadingZeros(length));
    }
}