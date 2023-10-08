package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.BlockInsertionSort;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.GrailSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Lazion", question = "Enter the base for this sort:", defaultAnswer = 2)
final public class LazionSort extends GrailSorting {

    BlockInsertionSort blocksert = new BlockInsertionSort(arrayVisualizer);
    BinaryInsertionSort binsert = new BinaryInsertionSort(arrayVisualizer);

    public LazionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
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
            grailMergeWithoutBuffer(array, start, i - start + blockLen, blockLen);
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
            binsert.customBinaryInsert(array, i, i + blockLen, 0.5);
        }
        if (i < currentLength)
            binsert.customBinaryInsert(array, i, currentLength, 0.5);
        mergesLen(array, 0, currentLength, blockLen, base);
    }
}