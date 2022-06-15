/**
 *
 */
package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author McDude_73
 *
 */
public final class IterativeQuickSort extends Sort {

    /**
     * @param arrayVisualizer
     */
    public IterativeQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Iterative Quick (alt)");
        setRunAllSortsName("Iterative Quick Sort");
        setRunSortName("Iterative Quicksort");
        setCategory("Quick Sorts");
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);

    }

    /*
     * private int partition(int[] arr, int lo, int hi) { // Alexandrescu partition
     * int pivot_ind = lo;
     * int pivot = arr[lo];
     * int old_arr_hi = arr[hi];
     * Writes.write(arr, hi, pivot + 1, 1, true, false);
     * 
     * while (true) {
     * lo++;
     * while (Reads.compareValues(arr[lo], pivot) <= 0) {
     * lo++;
     * }
     * Writes.write(arr, hi, arr[lo], 1, true, false);
     * 
     * hi--;
     * while (Reads.compareValues(arr[hi], pivot) > 0) {
     * hi--;
     * }
     * if (lo >= hi)
     * break;
     * Writes.write(arr, lo, arr[hi], 1, true, false);
     * }
     * if (lo == hi + 2) {
     * Writes.write(arr, lo, arr[hi + 1], 1, true, false);
     * lo--;
     * }
     * 
     * Writes.write(arr, lo, old_arr_hi, 1, true, false);
     * if (pivot < old_arr_hi)
     * lo--;
     * 
     * Writes.write(arr, pivot_ind, arr[lo], 1, true, false);
     * Writes.write(arr, lo, arr[pivot_ind], 1, true, false);
     * return lo;
     * }
     */
    private int partition(int[] a, int start, int stop) {
        int up = start, down = stop - 1, part = a[stop];
        if (stop <= start)
            return start;
        while (true) {
            while (Reads.compareValues(a[up], part) < 0) {
                up++;
            }
            while (Reads.compareValues(part, a[down]) < 0 && up < down) {
                down--;
            }
            if (up >= down)
                break;
            Writes.swap(a, up, down, 1, true, false);
            up++;
            down--;
        }
        Writes.swap(a, up, stop, 1, true, false);
        return up;
    }

    private void quickSort(int[] array, int startIndex, int endIndex) {
        int[] stack = Writes.createExternalArray(2);
        int top = -1;

        this.Writes.write(stack, ++top, startIndex, 0.0D, false, true);
        this.Writes.write(stack, ++top, endIndex, 0.0D, false, true);

        while (top >= 0) {

            endIndex = stack[top--];
            startIndex = stack[top--];

            int p = partition(array, startIndex, endIndex);

            if (this.Reads.compareValues(p - 1, startIndex) == 1) {
                this.Writes.write(stack, ++top, startIndex, 0.0D, false, true);
                if (top >= stack.length - 1) {
                    int[] newArr = Writes.copyOfArray(stack, stack.length + 1);
                    Writes.deleteExternalArray(stack);
                    stack = newArr;
                }
                this.Writes.write(stack, ++top, p - 1, 0.0D, false, true);
                if (top >= stack.length - 1) {
                    int[] newArr = Writes.copyOfArray(stack, stack.length + 1);
                    Writes.deleteExternalArray(stack);
                    stack = newArr;
                }
            }

            if (this.Reads.compareValues(p + 1, endIndex) == -1) {
                this.Writes.write(stack, ++top, p + 1, 0.0D, false, true);
                if (top >= stack.length - 1) {
                    int[] newArr = Writes.copyOfArray(stack, stack.length + 1);
                    Writes.deleteExternalArray(stack);
                    stack = newArr;
                }
                this.Writes.write(stack, ++top, endIndex, 0.0D, false, true);
                if (top >= stack.length - 1) {
                    int[] newArr = Writes.copyOfArray(stack, stack.length + 1);
                    Writes.deleteExternalArray(stack);
                    stack = newArr;
                }
            }

        }
        Writes.deleteExternalArray(stack);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        quickSort(array, 0, sortLength - 1);

    }

}