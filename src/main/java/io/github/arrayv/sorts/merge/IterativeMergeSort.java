package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class IterativeMergeSort extends Sort {

    public IterativeMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Iterative Merge");
        this.setRunAllSortsName("Iterative Merge Sort");
        this.setRunSortName("Iterative Mergesort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /*
     * Iterative mergesort function to sor
     * t arr[0...n-1]
     */
    void mergeSort(int arr[], int n) {

        // For current size of subarrays to
        // be merged curr_size varies from
        // 1 to n/2
        int curr_size;

        // For picking starting index of
        // left subarray to be merged
        int left_start;

        // Merge subarrays in bottom up
        // manner. First merge subarrays
        // of size 1 to create sorted
        // subarrays of size 2, then merge
        // subarrays of size 2 to create
        // sorted subarrays of size 4, and
        // so on.
        for (curr_size = 1; curr_size <= n - 1; curr_size = 2 * curr_size) {

            // Pick starting point of different
            // subarrays of current size
            for (left_start = 0; left_start < n - 1; left_start += 2 * curr_size) {
                // Find ending point of left
                // subarray. mid+1 is starting
                // point of right
                int mid = Math.min(left_start + curr_size - 1, n - 1);

                int right_end = Math.min(left_start
                        + 2 * curr_size - 1, n - 1);

                // Merge Subarrays arr[left_start...mid]
                // & arr[mid+1...right_end]
                merge(arr, left_start, mid, right_end);
            }
        }
    }

    /*
     * Function to merge the two haves arr[l..m] and
     * arr[m+1..r] of array arr[]
     */
    void merge(int arr[], int l, int m, int r) {
        int i, j, k;
        int n1 = m - l + 1;
        int n2 = r - m;

        /* create temp arrays */
        int L[] = Writes.createExternalArray(n1);
        int R[] = Writes.createExternalArray(n2);

        /*
         * Copy data to temp arrays L[]
         * and R[]
         */
        for (i = 0; i < n1; i++)
            Writes.write(L, i, arr[l + i], 1, false, true);
        for (j = 0; j < n2; j++)
            Writes.write(R, j, arr[m + 1 + j], 1, false, true);

        /*
         * Merge the temp arrays back into
         * arr[l..r]
         */
        i = 0;
        j = 0;
        k = l;
        while (i < n1 && j < n2) {
            if (Reads.compareValues(L[i], R[j]) <= 0) { //
                Writes.write(arr, k, L[i], 1, true, false);
                i++;
            } else {
                Writes.write(arr, k, R[j], 1, true, false);
                j++;
            }
            k++;
        }

        /*
         * Copy the remaining elements of
         * L[], if there are any
         */
        while (i < n1) {
            Writes.write(arr, k, L[i], 1, true, false);
            i++;
            k++;
        }

        /*
         * Copy the remaining elements of
         * R[], if there are any
         */
        while (j < n2) {
            Writes.write(arr, k, R[j], 1, true, false);
            j++;
            k++;
        }
        Writes.deleteExternalArray(L);
        Writes.deleteExternalArray(R);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        mergeSort(array, currentLength);
    }
}
