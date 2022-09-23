package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Searches;

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

public final class SearchInsertionSort extends Sort {
    public SearchInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Search-based Insertions");
        this.setRunAllSortsName("Insertion Sort");
        this.setRunSortName("Insertsort");
        this.setCategory("Insertion Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion(
                "Enter search algorithm (0 (default) for binary, 1 for exponential, 2 for interpolation, 3 for ternary, 4 for trisearch, 5 for jump):",
                0);
    }

    private static void interpolationInsertSort(int[] array, int start, int end, double compSleep,
            double writeSleep) {
        for (int i = start + 1; i < end; i++) {
            Searches.insertTo(array, i, Searches.rightIntpSearch(array, start, i, array[i], compSleep), writeSleep,
                    false);
        }
    }

    private static void binaryInsertSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        for (int i = start + 1; i < end; i++)
            Searches.insertTo(array, i, Searches.rightBinSearch(array, start, i, array[i], compSleep), writeSleep,
                    false);
    }

    private static void exponentialInsertSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        for (int i = start + 1; i < end; i++)
            Searches.insertTo(array, i, Searches.rightExpSearch(array, start, i, array[i], compSleep), writeSleep,
                    false);
    }

    private static void jumpInsertSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        for (int i = start + 1; i < end; i++)
            Searches.insertTo(array, i, Searches.rightExpSearch(array, start, i, array[i], compSleep), writeSleep,
                    false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        switch (bucketCount) {
            case 1:
                exponentialInsertSort(array, 0, currentLength, 2, 0.05);
                break;
            case 2:
                interpolationInsertSort(array, 0, currentLength, 2, 0.05);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                jumpInsertSort(array, 0, currentLength, 2, 0.05);
                break;
            default:
                binaryInsertSort(array, 0, currentLength, 2, 0.05);
                break;
        }

    }
}
