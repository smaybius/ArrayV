package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public final class ShellMetznerSort extends Sort {
    public ShellMetznerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Shell-Metzner Sort");
        this.setRunAllSortsName("Shell-Metzner Sort");
        this.setRunSortName("Shell-Metzner Sort");
        this.setCategory("Insertion Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Function to sort arr[] using Shell Metzner sort
    private void sort_shell_metzner(int arr[], int n) {

        // Declare variables
        int i, j, k, l, m;

        // Set initial step size to
        // the size of the array
        m = n;

        while (m > 0) {

            // Step size decreases by half each time
            m /= 2;

            // k is the upper limit for j
            k = n - m;

            // j is the starting point
            j = 0;

            do {

                // i equals to smaller value
                i = j;

                do {

                    // l equals to larger value
                    l = i + m;

                    // Compare and swap arr[i] with arr[l]
                    if (l < n && Reads.compareIndices(arr, i, l, 0.25, true) > 0) //
                    {
                        Writes.swap(arr, i, l, 1, true, false);

                        // Decrease smaller value by step size
                        i -= m;
                    } else {
                        break;
                    }
                } while (i >= 0);

                // Increment the lower limit of i
                j++;

            } while (j <= k);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        sort_shell_metzner(array, currentLength);
    }
}
