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

final public class FinalGarbageSort extends Sort {
    public FinalGarbageSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Final Garbage");
        this.setRunAllSortsName("Final Garbage Sort");
        this.setRunSortName("Final Garbage Sort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(3);
        this.setBogoSort(false);
    }

    public void garbageMerge(int[] a, int start, int end, int depth) {
        if (start < end) {
            if (Reads.compareIndices(a, start, end, 0.001, true) == 1) {
                Writes.swap(a, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                Writes.recordDepth(depth);
                Writes.recursion();
                garbageMerge(a, start, end - 1, depth + 1);
                Writes.recursion();
                garbageMerge(a, start + 1, end, depth + 1);
            }
        }
    }

    public void garbageMergeSort(int[] array, int start, int end, int depth) {
        if (start < end) {
            if (Reads.compareIndices(array, start, end, 0.001, true) == 1) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                Writes.recordDepth(depth);
                Writes.recursion();
                garbageMergeSort(array, start, mid, depth + 1);
                Writes.recursion();
                garbageMergeSort(array, mid + 1, end, depth + 1);
                garbageMerge(array, start, end, depth);
            }
        }
    }

    public void garbageMergePlusPlusPlusPlusPlus(int[] array, int start, int end, int x, int depth) {
        if (start < end && x <= end) {
            if (Reads.compareIndices(array, start, end, 0.001, true) == 1) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                Writes.recordDepth(depth);
                Writes.recursion();
                garbageMergePlusPlusPlusPlusPlus(array, start, mid, 1, depth + 1);
                Writes.recursion();
                garbageMergePlusPlusPlusPlusPlus(array, mid + 1, end, 1, depth + 1);

                x++;
                Writes.recursion();
                garbageMergePlusPlusPlusPlusPlus(array, start, end - 1, x, depth + 1);
                Writes.recursion();
                garbageMergePlusPlusPlusPlusPlus(array, start + 1, end, x, depth + 1);
                garbageMergeSort(array, start, end, depth);
            }
        }
    }

    public void ultimateGarbage(int[] array, int start, int end, int x, int depth) {
        if (start < end && x <= end) {
            if (Reads.compareIndices(array, start, end, 0.001, true) == 1) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                Writes.recordDepth(depth);
                Writes.recursion();
                ultimateGarbage(array, start, mid, 1, depth + 1);
                Writes.recursion();
                ultimateGarbage(array, mid + 1, end, 1, depth + 1);

                for (int i = 1; i <= end - start; i++) {
                    for (int j = 1; j <= i; j++) {
                        Writes.recordDepth(depth);
                        Writes.recursion();
                        ultimateGarbage(array, start, end - x, x + j, depth + 1);
                        Writes.recursion();
                        ultimateGarbage(array, start + x, end, x + j, depth + 1);
                    }
                }
                garbageMergePlusPlusPlusPlusPlus(array, start, end, 0, depth);
            }
        }
    }

    public void finalGarbage(int[] array, int start, int end, double x, int dir, int depth) {
        if (x < 1) {
            ultimateGarbage(array, start, end, 0, depth);
        } else {
            if (Reads.compareIndices(array, start, end, 0.001, true) == dir) {
                Writes.swap(array, start, end, 0.001, true, false);
            }

            if (end - start + 1 >= 3) {
                int mid = (start + end) / 2;
                Writes.recordDepth(depth);
                Writes.recursion();
                finalGarbage(array, start, mid, 1, -dir, depth + 1);
                Writes.recursion();
                finalGarbage(array, mid + 1, end, 1, dir, depth + 1);

                for (int i = 1; i <= end - start; i++) {
                    Writes.recordDepth(depth);
                    for (int j = 1; j <= i; j++) {
                        for (int k = 1; k <= i; k++) {
                            Writes.recursion();
                            finalGarbage(array, start, (int) end - k, x - 1, -dir, depth + 1);
                            Writes.recursion();
                            finalGarbage(array, (int) start + k, end, x - 1, dir, depth + 1);
                        }
                        Writes.recursion();
                        finalGarbage(array, start, end, x - 1, 1, depth + 1);
                    }
                    Writes.recursion();
                    finalGarbage(array, start, end, x - 1, 1, depth + 1);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.finalGarbage(array, 0, currentLength - 1, currentLength, 1, 0);
    }
}