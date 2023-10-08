package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

//
@SortMeta(name = "Binary Bidirectional Conditional Insertion")
final public class BinaryBidirectionalConditionalInsertionSort extends Sort {
    public BinaryBidirectionalConditionalInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void insertionSort(int[] array, int from, int to, double sleep, boolean auxwrite) throws Exception {
        for (int i = from + 1; i <= to; i++) {
            int moving = i - 1;
            while (moving >= from) {
                if (Reads.compareIndices(array, moving, i, sleep, true) < 1) {
                    break;
                }
                moving--;
            }
            int insertIndex = moving + 1;
            Writes.multiSwap(array, i, insertIndex, sleep, true, auxwrite);
        }
    }

    public void binaryInsertOneElement(int[] array, int unsortedIndex, int end, double sleep, boolean auxwrite)
            throws Exception {
        if (unsortedIndex > end) {
            // 左
            int left = end, right = unsortedIndex;
            while (left < right) {
                int middle = (left + right) >> 1;
                if (Reads.compareIndices(array, middle, unsortedIndex, sleep, true) < 0) {
                    left = middle + 1;
                } else {
                    right = middle;
                }
            }
            int insertIndex = left;
            Writes.multiSwap(array, unsortedIndex, insertIndex, sleep, true, auxwrite);
        } else {
            // 右
            int left = unsortedIndex, right = end;
            while (left < right) {
                int middle = (left + right) >> 1;
                if (Reads.compareIndices(array, middle, unsortedIndex, sleep, true) < 0) {
                    left = middle + 1;
                } else {
                    right = middle;
                }
            }
            int insertIndex = left - 1;
            Writes.multiSwap(array, unsortedIndex, insertIndex, sleep, true, auxwrite);
            // int moving = unsortedIndex + 1;
            // while (moving < end) {
            // if (Reads.compareIndices(array, moving, unsortedIndex, sleep, true) > 0) {
            // break;
            // }
            // moving++;
            // }
            // int insertIndex = moving - 1;
            // Writes.multiSwap(array, unsortedIndex, insertIndex, sleep, true, auxwrite);
        }
    }

    @Override
    public void runSort(int[] array, int length, int part) throws Exception {
        double sleep = 0.01;
        int left = 0, right = length - 1;
        int preLeft = left, preRight = right + 1;
        while (left < right) {
            int readIndex = left + 1;
            if (Reads.compareIndices(array, left, right, sleep, true) == 1) {
                Writes.swap(array, left, right, sleep, true, false);
            }
            while (readIndex < right) {
                if (Reads.compareIndices(array, readIndex, left, sleep, true) <= 0) {
                    Writes.swap(array, readIndex, left + 1, sleep, true, false);
                    binaryInsertOneElement(array, left + 1, preLeft, sleep, false);
                    left++;
                    readIndex++;
                } else if (Reads.compareIndices(array, readIndex, right, sleep, true) >= 0) {
                    Writes.swap(array, readIndex, right - 1, sleep, true, false);
                    binaryInsertOneElement(array, right - 1, preRight, sleep, false);
                    right--;
                } else {
                    readIndex++;
                }
            }
            left++;
            right--;
            preLeft = left;
            preRight = right + 1;
        }
    }
}
