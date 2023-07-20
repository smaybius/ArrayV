package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;
import io.github.arrayv.sorts.insert.InsertionSort;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

/**
 * Bogosort randomly shuffles the array until it is sorted.
 */
public final class ReadSort extends BogoSorting {
    public ReadSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Read");
        this.setRunAllSortsName("Read Sort");
        this.setRunSortName("Readsort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(true);
    }

    public void bogoSwapTSoS(int[] array, int start, int end, int[] aux, double sleep) {
        for (int k = 0; k < (end - start); k += 2) {
            if (Reads.compareIndices(array, k, k + 1, 0.1, true) > 0) {
                Writes.write(aux, k, array[k + 1], sleep, false, true);
                Writes.write(aux, k + 1, array[k], sleep, false, true);
            } else {
                Writes.write(aux, k, array[k], sleep, false, true);
                Writes.write(aux, k + 1, array[k + 1], sleep, false, true);
            }
        }
        for (int k = 0; k < (end - start); k += 2) {
            if (Reads.compareIndices(array, k, k + 1, 0.1, true) < 0) {
                Writes.swap(array, aux[k], aux[k + 1], sleep, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] tSoS = Writes.createExternalArray(length);
        double sleep = 1;
        int okwecanstopnowwewentwaywaytoofar = 0;
        while (!this.isArraySorted(array, length) && okwecanstopnowwewentwaywaytoofar < length * length) {
            this.bogoSwapTSoS(array, 0, length, tSoS, sleep);
            okwecanstopnowwewentwaywaytoofar++;
        }
        Writes.deleteExternalArray(tSoS);

        InsertionSort insert = new InsertionSort(arrayVisualizer);
        insert.customInsertSort(array, 0, length, 1, false);
    }
}