package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

final public class LuckyGnomeSort extends BogoSorting {
    public LuckyGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lucky Gnome");
        this.setRunAllSortsName("Lucky Gnome Sort");
        this.setRunSortName("Lucky Gnomesort");
        this.setCategory("Bogo Sorts");

        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setQuestion("Set the luck for this sort:", 50);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1)
            return 1;
        if (answer > 99)
            return 100;
        return answer;
    }

    @Override
    public void runSort(int[] array, int length, int luck) {
        for (int i = 1; i < length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (Reads.compareIndices(array, j, j + 1, 0.5, true) == 1) {
                    if (randInt(1, 101) <= luck) {
                        Writes.swap(array, j, j + 1, 1, true, false);
                    } else {
                        if (j > 0)
                            Writes.swap(array, j, j - 1, 1, true, false);
                        i = 1;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        for (int i = 1; i < length; i++) {
            if (Reads.compareIndices(array, i - 1, i, 0.5, true) == 1) {
                Writes.swap(array, i - 1, i, 1, true, false);
            }
        }
    }
}