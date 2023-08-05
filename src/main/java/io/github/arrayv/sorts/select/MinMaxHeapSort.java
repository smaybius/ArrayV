package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

// Min-Max Heaps translated from https://github.com/BartMassey/minmaxheap
@SortMeta(name = "Min-Max Heap")
public final class MinMaxHeapSort extends Sort {
    int start, end;

    public MinMaxHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected boolean compare(int x, int y, boolean isGt) {
        if (isGt) {
            int z = x;
            x = y;
            y = z;
        }
        return Reads.compareValues(x, y) < 0;
    }

    public boolean is_min_level(int index) {
        index = index - this.start + 1;
        return ((32 - Integer.numberOfLeadingZeros(index)) & 1) == 1;
    }

    public void downheap(int[] a, int start, int end, int i) {
        boolean cf;
        if (this.is_min_level(i)) {
            cf = false;
        } else {
            cf = true;
        }
        int left = 2 * i + 1;
        while (left < this.end) {
            int right = left + 1;
            int nexti = left;
            for (int c : new int[] { right, 2 * left + 1, 2 * left + 2, 2 * right + 1, 2 * right + 2 }) {
                if (c >= this.end) {
                    break;
                }
                if (compare(a[c], a[nexti], cf)) {
                    nexti = c;
                }
            }
            if (nexti <= right) {
                if (compare(a[nexti], a[i], cf)) {
                    Writes.swap(a, nexti, i, 0.5, true, false);
                }
                return;
            } else {
                if (compare(a[nexti], a[i], cf)) {
                    Writes.swap(a, nexti, i, 0.5, true, false);
                    int parent = (nexti - 1) / 2;
                    if (compare(a[parent], a[nexti], cf)) {
                        Writes.swap(a, nexti, parent, 0.5, true, false);
                    }
                } else {
                    return;
                }
            }
            i = nexti;
            left = 2 * i + 1;
        }
    }

    public void heapify(int[] arr, int start, int end) {
        for (int i = (this.end - 1) / 2; i >= this.start; i--) {
            this.downheap(arr, start, end, i);
        }
    }

    // public void store_min() {
    // this.end--;
    // if (this.end <= this.start) {
    // return;
    // }
    // Writes.swap(this.start, this.end);
    // this.downheap(this.start);
    // }

    public void store_max(int[] a, int start, int end) {
        if (this.end <= this.start + 1) {
            return;
        }
        int imax = this.start + 1;
        if (this.end > imax + 1 && compare(a[imax], a[imax + 1], false)) {
            imax++;
        }
        this.end--;
        Writes.swap(a, imax, this.end, 0.5, true, false);
        if (imax < this.end) {
            this.downheap(a, start, end, imax);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.start = 0;
        this.end = currentLength;
        this.heapify(array, start, end);
        for (int i = this.end - 1; i > this.start; i--) {
            this.store_max(array, start, end);
        }
    }
}
