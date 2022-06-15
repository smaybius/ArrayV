package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class QuickSortPartitions extends Sort {
    public QuickSortPartitions(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName(" Quick");
        this.setRunAllSortsName("Quick Sort");
        this.setRunSortName("Quicksort");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter partition (0 for Lomuto, 1 for Hoare, 2 for modified Lomuto, 3 for modified Hoare):",
                0);
    }

    private int partchoice;

    private int LomutoPartition(int[] a, int p, int r) {
        int x = a[r]; // x stores the pivot element
        int i = p - 1;
        int j = p; // j is loop control variable
        while (j <= (r - 1)) {
            Highlights.markArray(2, j);
            Delays.sleep(0.2);
            if (Reads.compareValues(a[j], x) <= 0) // a[p] to a[r-1] elements will be compared with pivot
            {
                i++;
                Writes.swap(a, i, j, 1, true, false);
            }
            j++;
        }
        Writes.swap(a, i + 1, r, 1, true, false);
        Highlights.clearMark(2);
        return i + 1; // return the location of the pivot
    }

    private int HoarePartition(int[] a, int p, int r) {
        if (p >= r)
            return -1; // trivial return for empty array
        int x = a[p]; // x stores the pivot element
        int i = p;
        int j = r + 1;
        while (true) {
            do {
                Highlights.markArray(2, i);
                Delays.sleep(0.2);
                i++;
            } while ((i <= r) && (Reads.compareValues(a[i], x) < 0)); // searches the element larger than
            // pivot from Left portion
            do {
                Highlights.markArray(2, j);
                Delays.sleep(0.2);
                j--;
            } while (Reads.compareValues(a[j], x) > 0); // searches the element smaller than pivot
            // from the Right portion
            if (i > j)
                break;
            Writes.swap(a, i, j, 1, true, false);
        }
        Writes.swap(a, p, j, 1, true, false); // swaps the larger element from the left with the // smaller element from
                                              // the right
        Highlights.clearMark(2);
        return j; // returns the location of the pivot
    }

    private int ModifiedLomuto(int[] a, int p, int r) {
        int x = a[p]; // x stores the pivot element
        int i = p; // location i is vacant
        int j = r;
        while (true) {
            while (Reads.compareValues(a[j], x) > 0) {
                Highlights.markArray(2, j);
                Delays.sleep(0.2);
                j--;
            }
            if (j <= i)
                break; // terminates the outer loop
            Writes.write(a, i, a[j], 0.5, true, false);
            Writes.write(a, j, a[i + 1], 0.5, true, false);
            i++;
        }
        Writes.write(a, i, x, 1, true, false);
        Highlights.clearMark(2);
        return i; // returns the location of the pivot
    }

    private int ModifiedHoare(int[] a, int p, int r) {
        if (Reads.compareIndices(a, p, r, 1, true) > 0)
            Writes.swap(a, p, r, 1, true, false); // Sentinel at both ends
        int x = a[p]; // x stores the pivot and location p is vacant now.
        while (true) {
            do {
                Highlights.markArray(2, r);
                Delays.sleep(0.2);
                r--;
            } while (Reads.compareValues(a[r], x) > 0); // search the smallest element in right portion.
            Writes.write(a, p, a[r], 0.5, true, false); // location r is vacant now.
            do {
                Highlights.markArray(2, p);
                Delays.sleep(0.2);
                p++;
            } while (Reads.compareValues(a[p], x) < 0); // search the larger element in left portion.
            if (p < r)
                Writes.write(a, r, a[p], 0.5, true, false); // location p is vacant now.
            else {
                Highlights.markArray(2, r + 1);
                Delays.sleep(0.2);
                if (Reads.compareValues(a[r + 1], x) <= 0)
                    r++;
                Writes.write(a, r, x, 0.5, true, false);
                Highlights.clearMark(2);
                return r; // return the location of the pivot
            }
        }
    }

    public int partitionFulcrum(int[] array, int nmemb) {
        int head = 0;
        int tail = nmemb - 1;
        int piv = array[nmemb / 2];
        int cnt = nmemb;
        Writes.write(array, nmemb / 2, array[tail], 1, true, false);

        while (true) {
            if (--cnt == 0) {
                Writes.write(array, tail, piv, 1, true, false);
                // array[tail] = piv;

                return head + 1;
            }
            if (Reads.compareValues(array[head], piv) <= 0) {
                head++;
                continue;
            }
            Writes.write(array, tail--, array[head], 1, true, false);
            // array[tail--] = array[head];

            while (true) {
                if (--cnt == 0) {
                    Writes.write(array, head, piv, 1, true, false);
                    // array[head] = piv;
                    return head + 1;
                }
                if (Reads.compareValues(array[tail], piv) <= 0) {
                    Writes.write(array, head++, array[tail], 1, true, false);
                    // array[head++] = array[tail];
                    break;
                }
                tail--;
            }
        }
    }

    private int partition(int[] array, int lo, int hi) {
        switch (partchoice) {
            case 0:
                return LomutoPartition(array, lo, hi);
            case 1:
                return HoarePartition(array, lo, hi);
            case 2:
                return ModifiedLomuto(array, lo, hi);
            case 3:
                return ModifiedHoare(array, lo, hi);
            default:
                return HoarePartition(array, lo, hi);
        }
    }

    private void quickSort(int[] array, int lo, int hi, int depth) {
        if (lo < hi) {
            int p = this.partition(array, lo, hi);
            Writes.recordDepth(depth);
            Writes.recursion();
            this.quickSort(array, lo, p - 1, depth + 1);
            Writes.recursion();
            this.quickSort(array, p + 1, hi, depth + 1);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        partchoice = bucketCount;
        this.quickSort(array, 0, currentLength - 1, 0);
    }
}
