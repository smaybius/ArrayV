package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Reflection Sort 2", category = "Impractical Sorts")
final public class ReflectionSort2 extends Sort {
    public ReflectionSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int dir = 1;
        int i = 0;
        boolean setup = false;
        boolean sorted = false;
        while (!sorted) {
            i = dir == 1 || !setup ? 0 : currentLength - 2;
            setup = true;
            sorted = true;
            while (i >= 0 && i < currentLength - 1) {
                if (Reads.compareIndices(array, i, i + 1, 0.125, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.125, true, false);
                    dir *= -1;
                    i = (currentLength - 1) - i;
                    sorted = false;
                }
                i += dir;
            }
            dir *= -1;
        }
    }
}