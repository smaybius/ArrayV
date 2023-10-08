package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
/*
 * 
MIT License

Copyright (c) 2021 Lu

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

@SortMeta(name = "Insertion-Selection", question = "please set selection window size:", defaultAnswer = 42)
final public class InsertionSelectionSort extends Sort {
    public InsertionSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    int runSelectionWindowSize;
    // int comparison = 0;
    // int writes = 0;

    public void insertSelectionSort(final int[] array, int length, double sleep) {
        final int unsortedIndex = reverseDecreasedSubArray(array, 0, length - 1, sleep);

        int skipTime = 0;
        int exactSortedStartIndex = 0;

        final int runSelectionRangeCopy = runSelectionWindowSize;
        int minElementIndexSoFar = Integer.MAX_VALUE;
        boolean isUpdateExactSortedIndex = false;
        double power = 0.5;
        for (int unsortedElementIndex = unsortedIndex; unsortedElementIndex < length; unsortedElementIndex++) {
            // 方法1
            runSelectionWindowSize = (int) ((length - unsortedElementIndex * 1.0) / length * runSelectionRangeCopy);
            // 方法2
            // runSelectionRange = (int) (0.00595529*Math.pow(length -
            // unsortedElementIndex,1.22731)+0.986057);

            boolean needResetSkipTime = false;

            // int rangeStart = unsortedElementIndex + 1 + skipTime *
            // runSelectionRangeMethod3;
            int rangeStart = unsortedElementIndex + skipTime * runSelectionWindowSize;
            // if (rangeStart >= length - 1) {
            // rangeStart = length - 2;
            // needResetSkipTime = true;
            // }
            int minIndex = rangeStart;
            int rangeEnd = rangeStart + runSelectionWindowSize;
            // if (rangeEnd + 1 + runSelectionRangeMethod3 >= length) {
            if (rangeEnd >= length - 1) {
                rangeEnd = length - 1;
                needResetSkipTime = true;
            }
            // 在范围内找到最小值
            for (int i = rangeStart + 1; i <= rangeEnd; i++) {
                if (Reads.compareIndices(array, minIndex, i, sleep, true) == 1) {
                    minIndex = i;
                }
            }
            // 最小值与unsortedElementIndex交换
            if (Reads.compareIndices(array, unsortedElementIndex, minIndex, sleep, true) == 1) {
                Writes.swap(array, unsortedElementIndex, minIndex, sleep, true, false);
            }

            if (needResetSkipTime) {
                skipTime = 0;
                // if (runSelectionRange >= 1) {
                // unsortedElementIndex -= ((length - unsortedElementIndex) / runSelectionRange)
                // / 4;
                // }
            } else {
                if (skipTime == 0 && unsortedElementIndex >= length - (length / Math.pow(2, power))) {
                    isUpdateExactSortedIndex = true;
                }
                skipTime++;
            }

            // binary insertion sort
            // 二分法插入排序一个值
            int minElementIndex = binaryInsertSort1Element(array, exactSortedStartIndex, unsortedElementIndex,
                    10 * sleep, 0);

            if (isUpdateExactSortedIndex) {
                if (Reads.compareValues(minElementIndex, minElementIndexSoFar) == -1) {
                    minElementIndexSoFar = minElementIndex;
                }
            }

            if (needResetSkipTime && isUpdateExactSortedIndex) {
                // 更新二分法插入排序的开始下标
                // exactSortedStartIndex++;
                exactSortedStartIndex = minElementIndexSoFar;
                minElementIndexSoFar = Integer.MAX_VALUE;
                power += 1;
                isUpdateExactSortedIndex = false;
            }

            Highlights.markArray(99, exactSortedStartIndex);
        }
    }

    protected int binaryInsertSort1Element(int[] array, int start, int end, double compSleep, double writeSleep) {
        int num = array[end];
        int lo = start, hi = end;

        while (lo < hi) {
            int mid = lo + ((hi - lo) / 2); // avoid int overflow!
            Highlights.markArray(1, lo);
            Highlights.markArray(2, mid);
            Highlights.markArray(3, hi);

            Delays.sleep(compSleep);

            if (Reads.compareValues(num, array[mid]) < 0) { // do NOT move equal elements to right of inserted element;
                                                            // this maintains stability!
                hi = mid;
            } else {
                lo = mid + 1;
            }
            // comparison++;
        }

        Highlights.clearMark(3);

        // item has to go into position lo

        int j = end - 1;

        while (j >= lo) {
            Writes.write(array, j + 1, array[j], writeSleep, true, false);
            j--;
            // writes++;
        }
        Writes.write(array, lo, num, writeSleep, true, false);
        // writes++;

        return lo;
    }

    private int reverseDecreasedSubArray(int[] array, int from, int to, double sleep) {
        int start = from, end = start + 1;
        int unsortedIndex = -1;
        Boolean isSubArrayDecrease = Reads.compareIndices(array, from, from + 1, sleep, true) == 1;
        while (end <= to) {
            if (end == to) {
                if (Boolean.TRUE.equals(isSubArrayDecrease)) {
                    Writes.reversal(array, start, end, sleep, true, false);
                    unsortedIndex = unsortedIndex == -1 ? to : unsortedIndex;
                }
                break;
            }
            if (isSubArrayDecrease == null) {
                isSubArrayDecrease = Reads.compareIndices(array, end, end + 1, sleep, true) == 1;
            } else {
                int result = Reads.compareIndices(array, end, end + 1, sleep, true);
                if (isSubArrayDecrease) {
                    if (result == -1) {
                        Writes.reversal(array, start, end, sleep, true, false);
                        start = end + 1;
                        unsortedIndex = unsortedIndex == -1 ? end + 1 : unsortedIndex;
                        isSubArrayDecrease = null;
                    }
                } else {
                    if (result == 1) {
                        start = end + 1;
                        unsortedIndex = unsortedIndex == -1 ? end + 1 : unsortedIndex;
                        isSubArrayDecrease = null;
                    }
                }
            }
            end++;
        }
        return unsortedIndex;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.runSelectionWindowSize = bucketCount;
        insertSelectionSort(array, length, 0.3);
    }
}