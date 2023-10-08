package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Cocktail Sandpaper")
final public class CocktailSandpaperSort extends Sort {
    public CocktailSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength - 1; i++) {
            for (int j = i + 1; j < currentLength; j++)
                if (Reads.compareIndices(array, i, j, 0.05, true) > 0)
                    Writes.swap(array, i, j, 0.05, true, false);
            i++;
            for (int j = currentLength - 1; j > i; j--)
                if (Reads.compareIndices(array, i, j, 0.05, true) > 0)
                    Writes.swap(array, i, j, 0.05, true, false);
        }
    }
}
