package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.merge.QuadSort;
import io.github.arrayv.sorts.templates.BestForNSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Best-for-N Sort", question = "Enter the run length for this sort:", defaultAnswer = 64, category = "Best-for-N Sorts")
final public class BestForNSort extends BestForNSorting {

    QuadSort quad = new QuadSort(arrayVisualizer);

    public BestForNSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void bestN(int[] array, int s, int l, int b) {
        int j;
        for (j = s; j + b <= s + l; j += b) {
            initNetwork(array, j, b);
        }
        int ends = l - j;
        initNetwork(array, j, ends);
        if (l > b)
            quad.runSort(array, l, 0);
    }

    public static int validateAnswer(int answer) {
        if (answer > 64)
            return 64;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        bestN(array, 0, currentLength, base);
    }
}