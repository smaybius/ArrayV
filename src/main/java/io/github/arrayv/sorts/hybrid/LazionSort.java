package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BlockInsertionSort;
import io.github.arrayv.sorts.insert.PDBinaryInsertionSort;
import io.github.arrayv.sorts.templates.GrailSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LazionSort extends GrailSorting {

    BlockInsertionSort blocksert = new BlockInsertionSort(arrayVisualizer);
    PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);

    public LazionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazion Stable");
        this.setRunAllSortsName("Lazion Stable Sort");
        this.setRunSortName("Lazion Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    protected int powlte(int value, int base) {
        int val;
        for (val = 1; val <= value; val *= base)
            ;
        return val / base;
    }

    protected void merge(int[] array, int start, int end, int base) {
        int blockLen = (end - start) / base;
        for (int i = start; i + blockLen < end; i += blockLen) {
            grailLazyMerge(array, start, i - start + blockLen, blockLen);
        }
    }

    protected void nonBn(int[] array, int start, int end) {
        blocksert.insertionSort(array, start, end, 0.5, false);
    }

    protected void mergesLen(int[] array, int start, int end, int lengthstart, int base) {
        int len = lengthstart;
        int index = start;
        while (len < end - start) {
            index = start;
            while (index + len <= end) {
                merge(array, index, index + len, base);
                index += len;
            }
            if (index != end)
                nonBn(array, index, end);
            len *= base;
        }
        if (len == end - start)
            merge(array, start, end, base);
        else
            nonBn(array, start, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int blockLen = powlte((int) Math.sqrt(currentLength), base);
        int i;
        for (i = 0; i + blockLen <= currentLength; i += blockLen) {
            binsert.pdbinsert(array, i, i + blockLen, 0.5, false);
        }
        if (i < currentLength)
            binsert.pdbinsert(array, i, currentLength, 0.5, false);
        mergesLen(array, 0, currentLength, blockLen, base);
    }
}