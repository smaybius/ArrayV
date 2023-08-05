package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author Meme Man
 *
 */
@SortMeta(name = "Log(N)-ary Heap")
public final class LogNaryHeapSort extends Sort {

    public LogNaryHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public static int log2(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }

    private void siftDown(int[] array, int val, int i, int p, int n, int base) {
        while (base * i + 1 < n) {
            int max = val;
            int next = i, child = base * i + 1;
            for (int j = child; j < Math.min(child + base, n); j++) {
                if (Reads.compareValues(array[p + j], max) > 0) {
                    max = array[p + j];
                    next = j;
                }
            }
            if (next == i)
                break;
            Writes.write(array, p + i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p + i, val, 1, true, false);
    }

    public void buildHeap(int[] array, int a, int b) {
        int n = b - a;
        int base = Math.max(log2(n), 2);
        for (int i = (n - 1) / base; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n, base);
    }

    public void heapSort(int[] array, int a, int b) {
        int n = b - a;
        int base = Math.max(log2(n), 2);
        for (int i = (n - 1) / base; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n, base);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i, base);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        heapSort(array, 0, sortLength);

    }

}
