package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.CircleSorting;

//
@SortMeta(name = "Apollyon")
final public class ApollyonSort extends CircleSorting {
    private boolean direction = true;

    public ApollyonSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private static int greatestPowerOfTwoLessThan(int n) {
        int k = 1;
        while (k < n) {
            k = k << 1;
        }
        return k >> 1;
    }

    private void compare(int[] A, int i, int j, boolean dir) {
        int cmp = Reads.compareIndices(A, i, j, 0.5, true);

        if (dir == (cmp == 1))
            Writes.swap(A, i, j, 0.5, true, false);
    }

    private void apollyonMerge(int[] A, int lo, int n, boolean dir) {
        if (n > 1) {
            int m = greatestPowerOfTwoLessThan(n);

            for (int i = lo; i < lo + n - m; i++) {
                this.compare(A, i, i + m, dir);
            }

            this.apollyonMerge(A, lo, m, dir);
            this.apollyonMerge(A, lo + m, n - m, dir);
        }
    }

    private void apollyonSort(int[] A, int lo, int n, boolean dir) {
        if (n > 1) {
            int m = n / 2;
            this.apollyonSort(A, lo, m, !dir);
            this.apollyonMerge(A, lo, n, dir);
        }
    }

    public void changeDirection(String choice) throws Exception {
        if (choice.equals("forward"))
            this.direction = true;
        else if (choice.equals("backward"))
            this.direction = false;
        else
            throw new Exception("Invalid direction for Apollyon Sort!");
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.end = sortLength;
        int threshold = 0, n = 1;
        for (; n < sortLength; n *= 2, threshold++)
            ;

        threshold /= 2;
        int iterations = 0;

        this.apollyonSort(array, 0, sortLength, this.direction);

        while (this.circleSortRoutine(array, 0, sortLength - 1, 0, 1) != 0) {
            iterations++;

            if (iterations >= threshold) {
                InsertionSort Inserter = new InsertionSort(this.arrayVisualizer);
                Inserter.customInsertSort(array, 0, sortLength, 0.1, false);
                break;
            }
        }
    }
}
