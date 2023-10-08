package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Partition Bubble")
final public class PartitionBubbleSort extends Sort {
    public PartitionBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int innerSort(int[] array, final int from, final int to, int maxSwapIndex, double sleep) {
        if (from == to) {
            return -1;
        }
        if (to - from == 1) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
            }
            return from;
        }
        Highlights.markArray(900, from);
        Highlights.markArray(901, to);
        boolean isContinue = false;
        int currentMaxSwapIndex = -1;
        for (int moving = from; moving < maxSwapIndex && moving < to; moving++) {
            if (Reads.compareIndices(array, moving, moving + 1, sleep, true) == 1) {
                Writes.swap(array, moving, moving + 1, sleep, true, false);
                currentMaxSwapIndex = moving;
                isContinue = true;
            }
        }
        if (!isContinue) {
            return from;
        }

        final int middle = (from + to) >> 1;
        innerSort(array, from, middle, currentMaxSwapIndex, sleep);
        innerSort(array, middle, to, currentMaxSwapIndex, sleep);
        // innerSort(array, from, middle, middle, sleep);
        // innerSort(array, middle, to - 1, to-1, sleep);
        return currentMaxSwapIndex;
    }

    @Override
    public void runSort(int[] array, int length, int range) throws Exception {
        int left = 0, right = length - 1;
        int currentMaxSwapIndex = right;
        do {
            currentMaxSwapIndex = innerSort(array, left, right, currentMaxSwapIndex, 0.1);
            // System.out.println("currentMaxSwapIndex = " + currentMaxSwapIndex);
            // Highlights.markArray(999, currentMaxSwapIndex);
        } while (currentMaxSwapIndex > 0);
    }
}
