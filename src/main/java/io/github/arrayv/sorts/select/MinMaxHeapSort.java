package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

// Min-Max Heaps translated from https://github.com/BartMassey/minmaxheap
public final class MinMaxHeapSort extends Sort {

    public MinMaxHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        setSortListName("Min-Max Heap");
        setRunAllSortsName("Min-Max Heap Sort");
        setRunSortName("Min-max Heapsort");
        setCategory("Selection Sorts");
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    protected boolean compare(int x, int y, boolean isGt) {
        if (isGt) {
            int z = x;
            x = y;
            y = z;
        }
        return Reads.compareValues(x, y) < 0;
    }

    public boolean is_min_level(int[] arr, int start, int index) {
        index = index - start + 1;
        return ((32 - Integer.numberOfLeadingZeros(index)) & 1) == 1;
    }

    public void downheap(int[] arr, int start, int end, int i) {
        boolean cf;
        if (is_min_level(arr, start, i)) {
            cf = false;
        } else {
            cf = true;
        }
        int left = 2 * i + 1;
        while (left < end) {
            int right = left + 1;
            int nexti = left;
            for (int c : new int[] { right, 2 * left + 1, 2 * left + 2, 2 * right + 1, 2 * right + 2 }) {
                if (c >= end) {
                    break;
                }
                if (compare(arr[c], arr[nexti], cf)) {
                    nexti = c;
                }
            }
            if (nexti <= right) {
                if (compare(arr[nexti], arr[i], cf)) {
                    Writes.swap(arr, nexti, i, 1, true, false);
                }
                return;
            } else {
                if (compare(arr[nexti], arr[i], cf)) {
                    Writes.swap(arr, nexti, i, 1, true, false);
                    int parent = (nexti - 1) / 2;
                    if (compare(arr[parent], arr[nexti], cf)) {
                        Writes.swap(arr, nexti, parent, 1, true, false);
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
        for (int i = (end - 1) / 2; i >= start; i--) {
            downheap(arr, start, end, i);
        }
    }

    // public void store_min() {
    // end--;
    // if (end <= start) {
    // return;
    // }
    // swap(start, end);
    // downheap(start);
    // }

    public void store_max(int[] arr, int start, int end) {
        if (end <= start + 1) {
            return;
        }
        int imax = start + 1;
        if (end > imax + 1 && compare(arr[imax], arr[imax + 1], false)) {
            imax++;
        }
        end--;
        Writes.swap(arr, imax, end, 1, true, false);
        if (imax < end) {
            downheap(arr, start, end, imax);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        heapify(array, 0, currentLength);
        for (int i = currentLength - 1; i > 0; i--) {
            store_max(array, 0, currentLength);
        }
    }
}
