package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * 
MIT License
Copyright (c) 2020 fungamer2 & yuji
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

final public class UltimateGarbageSort extends Sort {
    public UltimateGarbageSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Ultimate Garbage");
        this.setRunAllSortsName("Ultimate Garbage Sort");
        this.setRunSortName("Ultimate Garbage Sort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.setBogoSort(false);
    }

    public void garbageMerge(int[] array, int start, int end) {
        if (start < end) {
            if (Reads.compareIndices(array, start, end, 0.001, true) > 0) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                garbageMerge(array, start, end - 1);
                garbageMerge(array, start + 1, end);
            }
        }
    }

    public void garbageMergeSort(int[] array, int start, int end) {
        if (start < end) {
            if (Reads.compareIndices(array, start, end, 0.001, true) > 0) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                garbageMergeSort(array, start, mid);
                garbageMergeSort(array, mid + 1, end);
                garbageMerge(array, start, end);
            }
        }
    }

    public void garbageMergePlusPlusPlusPlusPlus(int[] array, int start, int end, int x) {
        if (start < end && x <= end) {
            if (Reads.compareIndices(array, start, end, 0.001, true) == 1) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                garbageMergePlusPlusPlusPlusPlus(array, start, mid, 1);
                garbageMergePlusPlusPlusPlusPlus(array, mid + 1, end, 1);

                x++;
                garbageMergePlusPlusPlusPlusPlus(array, start, end - 1, x);
                garbageMergePlusPlusPlusPlusPlus(array, start + 1, end, x);
                garbageMergeSort(array, start, end);
            }
        }
    }

    public void ultimateGarbage(int[] array, int start, int end, int x) {
        if (start < end && x <= end) {
            if (Reads.compareIndices(array, start, end, 0.001, true) > 0) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                ultimateGarbage(array, start, mid, 1);
                ultimateGarbage(array, mid + 1, end, 1);

                for (int i = 1; i <= end - start; i++) {
                    for (int j = 1; j <= i; j++) {
                        ultimateGarbage(array, start, end - x, x + j);
                        ultimateGarbage(array, start + x, end, x + j);
                    }
                }

                garbageMergePlusPlusPlusPlusPlus(array, start, end, 0);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ultimateGarbage(array, 0, currentLength - 1, 1);
    }
}