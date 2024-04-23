package io.github.arrayv.sorts.distribute;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.ArrayVList;

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
@SortMeta(name = "LSD Radix", bucketSort = true, radixSort = true)
public final class LSDRadixSort extends Sort {
    public LSDRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.setRunAllSortsName("Least Significant Digit Radix Sort, Base " + bucketCount);
        this.setRunSortName("Least Significant Digit Radix Sort, Base " + bucketCount);

        int highestpower = Reads.analyzeMaxLog(array, sortLength, bucketCount, 0.5, true);

        ArrayVList[] registers = new ArrayVList[bucketCount];

        for (int i = 0; i < bucketCount; i++)
            registers[i] = new ArrayVList();

        for (int p = 0; p <= highestpower; p++) {
            for (int i = 0; i < sortLength; i++) {
                Highlights.markArray(1, i);

                int digit = Reads.getDigit(array[i], p, bucketCount);
                Writes.arrayListAdd(registers[digit], array[i]);

                Writes.mockWrite(sortLength, digit, array[i], 1);
            }

            Writes.fancyTranscribe(array, sortLength, registers, bucketCount * 0.8);
        }

        Writes.deleteExternalArray(registers);
    }
}
