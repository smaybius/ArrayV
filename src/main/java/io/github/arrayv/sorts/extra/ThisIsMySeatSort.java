package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2021 Lu

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
@SortMeta(name = "This is my Seat")
final public class ThisIsMySeatSort extends Sort {
    public ThisIsMySeatSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void thisIsMySeatSort(int[] array, int length, double sleep, boolean auxwrite) {
        int minIndex = 0, maxIndex = 0;
        for (int i = 0; i < length; i++) {
            if (Reads.compareIndices(array, minIndex, i, sleep, true) == 1) {
                minIndex = i;
            }
            if (Reads.compareIndices(array, maxIndex, i, sleep, true) == -1) {
                maxIndex = i;
            }
        }
        Writes.swap(array, 0, minIndex, sleep, true, false);
        Writes.swap(array, length - 1, maxIndex, sleep, true, false);

        int findingSeatIndex = 1;
        double k = (array[0] - array[length - 1]) / (1.0 - length);
        int b = array[0];
        while (findingSeatIndex < length - 1) {
            int assumeIndex = (int) ((array[findingSeatIndex] - b * 1.0) / k);
            if (findingSeatIndex == assumeIndex) {
                findingSeatIndex++;
            } else {
                Writes.swap(array, findingSeatIndex, assumeIndex, sleep, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        thisIsMySeatSort(array, length, 0.5, false);
    }
}