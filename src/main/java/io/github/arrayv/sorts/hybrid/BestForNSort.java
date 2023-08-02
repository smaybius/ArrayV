package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.merge.QuadSort;
import io.github.arrayv.sorts.templates.BestForNSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BestForNSort extends BestForNSorting {

    QuadSort quad = new QuadSort(arrayVisualizer);

    public BestForNSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Best For N");
        this.setRunAllSortsName("Best For N Sort");
        this.setRunSortName("Best For N Sort");
        this.setCategory("Hybrid Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the run length for this sort:", 64);
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

    @Override
    public int validateAnswer(int answer) {
        if (answer > 64)
            return 64;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        bestN(array, 0, currentLength, base);
    }
}