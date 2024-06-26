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
@SortMeta(name = "MSD Radix", bucketSort = true, radixSort = true)
public final class MSDRadixSort extends Sort {
    public MSDRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void radixMSD(int[] array, int length, int min, int max, int radix, int pow) {
        if (min >= max || pow < 0)
            return;

        Highlights.markArray(2, max - 1);
        Highlights.markArray(3, min);

        ArrayVList[] registers = new ArrayVList[radix];

        for (int i = 0; i < radix; i++)
            registers[i] = new ArrayVList();

        for (int i = min; i < max; i++) {
            Highlights.markArray(1, i);

            int digit = Reads.getDigit(array[i], pow, radix);
            Writes.arrayListAdd(registers[digit], array[i]);
            Delays.sleep(1);
        }

        Highlights.clearMark(2);
        Highlights.clearMark(3);

        Writes.transcribeMSD(array, registers, 0, min, 0.8, true, false);
        for (int j = 0; j < registers.length; j++) {
            registers[j].unwatch();
            Writes.changeAllocAmount(-registers[j].size());
        }
        int sum = 0;
        for (int i = 0; i < registers.length; i++) {
            this.radixMSD(array, length, sum + min, sum + min + registers[i].size(), radix, pow - 1);

            sum += registers[i].size();
            Writes.arrayListClear(registers[i]);
            Writes.changeAuxWrites(registers[i].size());
        }

        Writes.deleteExternalArray(registers);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int highestpower = Reads.analyzeMaxLog(array, sortLength, bucketCount, 0.5, true);

        this.setRunSortName("Most Significant Digit Radix Sort, Base " + bucketCount);
        this.setRunSortName("Most Significant Digit Radix Sort, Base " + bucketCount);
        radixMSD(array, sortLength, 0, sortLength, bucketCount, highestpower);
    }
}
