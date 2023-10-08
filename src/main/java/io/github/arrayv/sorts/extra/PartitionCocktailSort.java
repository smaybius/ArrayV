package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

//
@SortMeta(name = "Partition Cocktail")
final public class PartitionCocktailSort extends Sort {
    public PartitionCocktailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private boolean innerSort(int[] array, final int from, final int to, double sleep) {
        if (from == to) {
            return false;
        }
        boolean isContinue = false;
        for (int moving = from; moving < to; moving++) {
            if (Reads.compareIndices(array, moving, moving + 1, sleep, true) == 1) {
                Writes.swap(array, moving, moving + 1, sleep, true, false);
                isContinue = true;
            }
        }
        if (!isContinue) {
            return false;
        }
        isContinue = false;
        for (int moving = to; moving > from; moving--) {
            if (Reads.compareIndices(array, moving, moving - 1, sleep, true) == -1) {
                Writes.swap(array, moving, moving - 1, sleep, true, false);
                isContinue = true;
            }
        }
        if (!isContinue) {
            return false;
        }
        int middle = (from + to) >> 1;
        innerSort(array, from + 1, middle, sleep);
        innerSort(array, middle, to - 1, sleep);
        return isContinue;
    }

    @Override
    public void runSort(int[] array, int length, int range) throws Exception {
        int left = 0, right = length - 1;
        boolean isContinue;
        do {
            isContinue = innerSort(array, left, right, 0.01);
            // int offset = (int) (Math.log(right - left) / Math.log(2))/3;
            // left += offset;
            // right -= offset;
            left++;
            right -= 2;
            // right--;
            Highlights.markArray(998, left);
            Highlights.markArray(999, right);
        } while (isContinue);
    }
}
