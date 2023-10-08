package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

//
@SortMeta(name = "Cocktail 3-Part")
final public class CocktailThreePartSort extends Sort {
    public CocktailThreePartSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void iterativeOneThirdSort(int[] array, int length, double sleep, boolean auxwrite) throws Exception {
        int max = length - 1;
        int min = 0;
        while (true) {
            int newMax = innerSort(array, min, max, sleep, true);
            if (newMax <= min) {
                break;
            }
            max = newMax;
            int newMin = innerSortBackward(array, min, max, sleep, true);
            if (newMin >= max) {
                break;
            }
            min = newMin;
        }
    }

    public int innerSort(int[] array, int from, int to, double sleep, boolean backward) throws Exception {
        if (to - from == 1) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
                return to;
            }
            return -1;
        } else if (to - from == 2) {
            boolean swapped = false;
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                swapped = true;
            }
            if (Reads.compareIndices(array, from + 1, to, sleep, true) == 1) {
                Writes.swap(array, from + 1, to, sleep, true, false);
                swapped = true;
            }
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                swapped = true;
            }
            return swapped ? from : -1;
        } else {
            int left = (to - from) / 3 + from;
            int right = (to - from) / 3 * 2 + from;
            // int middle = (to + from) >> 1;
            // if (left == right) {
            // right++;
            // }
            if (Reads.compareIndices(array, left, right, sleep, true) == 1) {
                Writes.swap(array, left, right, sleep, true, false);
            }
            int a, b;
            // 1
            a = innerSort(array, from, right, sleep, true);
            b = innerSort(array, left, to, sleep, true);

            // 2
            // if (backward) {
            // a = innerSort(array, from, right, sleep, false);
            // b = innerSort(array, left, to, sleep, false);
            // } else {
            // a = innerSort(array, left, to, sleep, true);
            // b = innerSort(array, from, right, sleep, true);
            // }

            // 3
            // boolean a = innerSort(array, from, middle, sleep);
            // boolean b = innerSort(array, middle, to, sleep);
            return Math.max(a, b);
        }
    }

    public int innerSortBackward(int[] array, int from, int to, double sleep, boolean backward) throws Exception {
        if (to - from == 1) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
                return from;
            }
            return Integer.MAX_VALUE;
        } else if (to - from == 2) {
            boolean swapped = false;
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                swapped = true;
            }
            if (Reads.compareIndices(array, from + 1, to, sleep, true) == 1) {
                Writes.swap(array, from + 1, to, sleep, true, false);
                swapped = true;
            }
            if (Reads.compareIndices(array, from, to - 1, sleep, true) == 1) {
                Writes.swap(array, from, to - 1, sleep, true, false);
                swapped = true;
            }
            return swapped ? to : Integer.MAX_VALUE;
        } else {
            int left = (to - from) / 3 + from;
            int right = (to - from) / 3 * 2 + from;
            // int middle = (to + from) >> 1;
            // if (left == right) {
            // right++;
            // }
            if (Reads.compareIndices(array, left, right, sleep, true) == 1) {
                Writes.swap(array, left, right, sleep, true, false);
            }
            int a, b;
            // 1
            b = innerSortBackward(array, left, to, sleep, true);
            a = innerSortBackward(array, from, right, sleep, true);

            // 2
            // if (backward) {
            // a = innerSortBackward(array, from, right, sleep, false);
            // b = innerSortBackward(array, left, to, sleep, false);
            // } else {
            // a = innerSortBackward(array, left, to, sleep, true);
            // b = innerSortBackward(array, from, right, sleep, true);
            // }

            // 3
            // a = innerSortBackward(array, from, middle, sleep, true);
            // b = innerSortBackward(array, middle, to, sleep, true);
            return Math.min(a, b);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
        iterativeOneThirdSort(array, length, 0.001, true);
    }
}
