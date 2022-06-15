package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class ProxmapSort extends Sort {
    public ProxmapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Proxmap");
        setRunAllSortsName("Proxmap Sort");
        setRunSortName("Proxmap Sort");
        setCategory("Distribution Sorts");
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    public void runSort(int[] array, int length, int bucketCount) {
        int start = 0, end = length;
        int[] a2 = Writes.createExternalArray(length);
        int[] mapKey = Writes.createExternalArray(length);
        int[] hitCount = Writes.createExternalArray(length);
        for (int x = start; x < end;) {
            a2[x] = -1;
            Writes.changeAuxWrites(1);
            x++;
        }
        int min = array[start], max = array[start];
        for (int i = start + 1; i < end; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareValues(array[i], min) < 0) { //
                min = array[i];
            } else if (Reads.compareValues(array[i], max) > 0) {
                max = array[i];
            }
        }
        Highlights.clearMark(2);
        for (int j = start; j < end; j++) {
            this.Writes.write(mapKey, j, (int) Math.floor(((array[j] - min) / (max - min) * (end - 1))),
                    1.0D, true, true);
            hitCount[mapKey[j]] = hitCount[mapKey[j]] + 1;
            this.Writes.changeAuxWrites(1);
        }
        hitCount[end - 1] = end - hitCount[end - 1];
        this.Writes.changeAuxWrites(1);
        for (int k = end - 1; k > start; k--)
            this.Writes.write(hitCount, k - 1, hitCount[k] - hitCount[k - 1], 1.0D, true, true);
        int insertIndex = 0, insertStart = 0;
        for (int l = start; l < end; l++) {
            insertIndex = hitCount[mapKey[l]];
            insertStart = insertIndex;
            for (; a2[insertIndex] != -1; insertIndex++) {
                Highlights.markArray(2, insertIndex);
            }
            while (insertIndex > insertStart && Reads.compareValues(array[l], a2[insertIndex - 1]) < 0) { //
                this.Writes.write(a2, insertIndex, a2[insertIndex - 1], 0.01D, true, true);
                insertIndex--;
            }
            this.Writes.write(a2, insertIndex, array[l], 0.01D, true, true);
        }
        for (int m = start; m < end;) {
            this.Writes.write(array, m, a2[m], 1.0D, true, false);
            m++;
        }
        Writes.deleteExternalArray(a2);
        Writes.deleteExternalArray(mapKey);
        Writes.deleteExternalArray(hitCount);
    }
}

/*
 * Location: E:\sort\CustomSorts
 * (1)\110CustomSorts.jar!\sorts\distribute\ProxmapSort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version: 1.1.3
 */