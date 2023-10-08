package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.Sort;

//
@SortMeta(name = "Base-N Weave", question = "Set N Part", defaultAnswer = 16)
final public class BaseNWeaveSort extends Sort {
    public BaseNWeaveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Deprecated
    public void innerSort(int[] array, int from, int to, final int range, double sleep) throws Exception {
        if (from == to) {
            return;
        }
        if (from + range >= to) {
            insertionSort(array, from, to, sleep, false);
            return;
        }

        int middle = (to - from >> 1) + from;

        innerSort(array, from, middle, range, sleep);
        innerSort(array, middle, to, range, sleep);
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

    @Override
    public void runSort(int[] array, int length, int part) throws Exception {
        int[] externalArray = Writes.createExternalArray(length);
        int range = (int) Math.ceil(length * 1.0 / part);
        double sleep = 0.01;

        int left = 0, right = range;
        BinaryInsertionSort binaryInsertionSort = new BinaryInsertionSort(arrayVisualizer);
        while (right < length) {
            binaryInsertionSort.customBinaryInsert(array, left, right + 1, sleep);
            // insertionSort(array, left, right, sleep, false);
            if (right == length - 1)
                break;
            left = right;
            right += range;
            if (right >= length) {
                right = length - 1;
            }
        }

        int readIndex = 0, writeIndex = 0;
        int times = range;
        while (times > 0) {
            while (readIndex < length) {
                Writes.write(externalArray, writeIndex++, array[readIndex], sleep, true, true);
                readIndex += range;
            }
            times--;
            readIndex = range - times;
        }

        Writes.arraycopy(externalArray, 0, array, 0, length, 0.1, true, true);

        binaryInsertionSort.customBinaryInsert(array, 0, length, sleep);
        // insertionSort(array, 0, length - 1, sleep, false);
    }
}
