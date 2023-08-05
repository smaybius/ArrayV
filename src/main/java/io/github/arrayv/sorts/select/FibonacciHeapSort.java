package io.github.arrayv.sorts.select;

import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Fibonacci Heap")
final public class FibonacciHeapSort extends Sort {
    public FibonacciHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int[] FIB = new int[44]; // fib(47) > Integer.MAX_VALUE

    public void genFib() {
        FIB[0] = FIB[1] = 1;
        for (int i = 2; i < FIB.length; i++) {
            Writes.write(FIB, i, FIB[i - 2] + FIB[i - 1], 0, false, true);
        }
    }

    public void fastsift(int[] array, int a, int b) {
        byte order = (byte) Integer.numberOfLeadingZeros(b & ~a);
        if (order < 2)
            return;
        while (order > 2) {
            int f = FIB[order - 2],
                    j = b - f;
            if (j < a)
                order--;
            else {
                if (Reads.compareIndices(array, b, j, 0.1, true) > 0) {
                    Writes.swap(array, b, j, 1, true, false);
                    order -= 2;
                } else {
                    order--;
                }
            }
        }
    }

    public void sift(int[] array, int a, int b) {
        byte order = 2;
        int n = b - 1;
        if (order < 2)
            return;
        while (order > 1) {
            int f = FIB[order - 1],
                    j = b - f;
            if (j < a)
                return;
            else {
                int l = j,
                        m = b - FIB[order - 2] - f;
                if (m < a)
                    m = a;
                for (int i = 0; i < FIB[order - 2] && m + i < j; i++) {
                    if (Reads.compareIndices(array, l, m + i, 0.01, true) > 0)
                        l = m + i;
                }
                Writes.swap(array, l, n, 1, true, false);
                n = l;
                order++;
            }
        }
    }

    public void fibHeapify(int[] array, int start, int end) {
        genFib();
        int j = 1;
        Reads.addComparison();
        while (FIB[j] <= end - start) {
            Reads.addComparison();
            j++;
        }
        for (int i = start; i < end; i++) {
            fastsift(array, start, i);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        fibHeapify(array, 0, length);
        for (int i = 0; i < length; i++) {
            Writes.swap(array, i, length - 1, 1, true, false);
            sift(array, i + 1, length);
        }
        InsertionSort i = new InsertionSort(arrayVisualizer);
        i.customInsertSort(array, 0, length, 1, false);
        Writes.deleteExternalArray(FIB);
    }
}