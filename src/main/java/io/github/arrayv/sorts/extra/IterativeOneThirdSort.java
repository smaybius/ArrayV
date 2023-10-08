package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Iterative One-Third")
final public class IterativeOneThirdSort extends Sort {
    public IterativeOneThirdSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void iterativeOneThirdSort(int[] array, int length, double sleep, boolean auxwrite) throws Exception {
        int decrement = 0;
        while (true) {
            boolean isContinue = innerSort(array, 0, length - 1 - decrement, sleep, true);
            if (!isContinue) {
                break;
            }
            // isContinue = innerSortBackward(array, decrement, length - 1, sleep, true);
            // if (!isContinue) {
            // break;
            // }
            // decrement++;
        }
    }

    public boolean innerSort(int[] array, int from, int to, double sleep, boolean backward) throws Exception {
        if (to - from == 1) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
                return true;
            }
            return false;
        } else if (to - from == 2) {
            boolean b = false;
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                b = true;
            }
            if (Reads.compareIndices(array, from + 1, to, sleep, true) == 1) {
                Writes.swap(array, from + 1, to, sleep, true, false);
                b = true;
            }
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                b = true;
            }
            return b;
        } else {
            int left = (to - from) / 3 + from;
            int right = (to - from) / 3 * 2 + from;
            // if (left == right) {
            // right++;
            // }
            if (Reads.compareIndices(array, left, right, sleep, true) == 1) {
                Writes.swap(array, left, right, sleep, true, false);
            }
            boolean a, b;
            // 1
            // a = innerSort(array, from, right, sleep, true);
            // b = innerSort(array, left, to, sleep, true);

            // 2
            if (backward) {
                a = innerSort(array, from, right, sleep, true);
                b = innerSort(array, left, to, sleep, false);
            } else {
                a = innerSort(array, left, to, sleep, true);
                b = innerSort(array, from, right, sleep, false);
            }

            // 3
            // a = innerSort(array, from, middle, sleep, true);
            // b = innerSort(array, middle, to, sleep, true);

            // 4
            // a = innerSort(array, from, right, sleep, true);
            // b = innerSort(array, left, to, sleep, true);
            // boolean c = innerSort(array, right, to, sleep, true);
            return a || b;
        }
    }

    public boolean innerSortBackward(int[] array, int from, int to, double sleep, boolean backward) throws Exception {
        if (to - from == 1) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
                return true;
            }
            return false;
        } else if (to - from == 2) {
            boolean b = false;
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                b = true;
            }
            if (Reads.compareIndices(array, from + 1, to, sleep, true) == 1) {
                Writes.swap(array, from + 1, to, sleep, true, false);
                b = true;
            }
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                b = true;
            }
            return b;
        } else {
            int left = (to - from) / 3 + from;
            int right = (to - from) / 3 * 2 + from;
            // if (left == right) {
            // right++;
            // }
            if (Reads.compareIndices(array, left, right, sleep, true) == 1) {
                Writes.swap(array, left, right, sleep, true, false);
            }
            boolean a, b;
            // 1
            b = innerSortBackward(array, left, to, sleep, true);
            a = innerSortBackward(array, from, right, sleep, true);

            // 2
            // if (backward) {
            // a = innerSortBackward(array, from, right, sleep, true);
            // b = innerSortBackward(array, left, to, sleep, false);
            // } else {
            // a = innerSortBackward(array, left, to, sleep, true);
            // b = innerSortBackward(array, from, right, sleep, false);
            // }

            // 3
            // a = innerSortBackward(array, from, middle, sleep, true);
            // b = innerSortBackward(array, middle, to, sleep, true);
            return a || b;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
        iterativeOneThirdSort(array, length, 0.002, true);
    }
}
