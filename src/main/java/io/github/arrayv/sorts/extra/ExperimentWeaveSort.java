package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

//
@SortMeta(name = "Experiment Weave", question = "set threshold:", defaultAnswer = 50)
final public class ExperimentWeaveSort extends Sort {
    public ExperimentWeaveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    int threshold;
    int totalLength;
    int[] externalArray;

    public void innerSort(int[] array, int from, int to, final double percent, double sleep) throws Exception {
        if (from == to) {
            return;
        }

        final int middle = (int) ((to - from) * percent + from);

        if (from + threshold <= to) {
            innerSort(array, from, middle, percent, sleep);
            innerSort(array, middle, to, percent, sleep);
        }

        final int length = to - from + 1;

        // double sleep = 0.01;

        // final int left = 0, right = length - 1;
        // final int middle = (int) (length * percent);
        // BinaryInsertionSort binaryInsertionSort = new
        // BinaryInsertionSort(arrayVisualizer);
        // binaryInsertionSort.customBinaryInsert(array, from, middle, sleep);
        // binaryInsertionSort.customBinaryInsert(array, middle, to + 1, sleep);
        insertionSort(array, from, middle - 1, sleep, false);
        insertionSort(array, middle, to, sleep, false);
        int readLeftIndex = from, readRightIndex = middle, writeIndex = 0;

        while (readLeftIndex <= middle - 1 && readRightIndex <= to) {
            Writes.write(externalArray, writeIndex++, array[readLeftIndex++], 0.1, true, true);
            Writes.write(externalArray, writeIndex++, array[readRightIndex++], 0.1, true, true);
        }
        if (readLeftIndex <= middle - 1) {
            Writes.reversearraycopy(array, readLeftIndex, array, to + 1 - (middle - readLeftIndex),
                    middle - readLeftIndex, 0.1, true, false);
            Writes.arraycopy(externalArray, 0, array, from, to + 1 - (middle - readLeftIndex) - from, 0.1, true, true);
        } else if (readRightIndex <= to) {
            Writes.arraycopy(externalArray, 0, array, from, readRightIndex - from, 0.1, true, true);
        } else {
            Writes.arraycopy(externalArray, 0, array, from, length, 0.1, true, true);
        }
        // BinaryInsertionSort binaryInsertionSort = new
        // BinaryInsertionSort(arrayVisualizer);
        // binaryInsertionSort.customBinaryInsert(array, 0, length, sleep);
        // insertionSort(array, 0, length - 1, sleep, false);
        // Writes.deleteExternalArray(externalArray);

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
    public void runSort(int[] array, int length, int threshold) throws Exception {
        this.threshold = threshold;
        this.totalLength = length;
        externalArray = Writes.createExternalArray(totalLength);

        double percent = 0.5;
        double sleep = 0.01;

        innerSort(array, 0, length - 1, percent, sleep);

        insertionSort(array, 0, length - 1, sleep, false);
    }
}
