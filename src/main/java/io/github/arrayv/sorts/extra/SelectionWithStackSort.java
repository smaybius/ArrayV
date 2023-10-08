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
@SortMeta(name = "Selection w/ Stack")
final public class SelectionWithStackSort extends Sort {
    public SelectionWithStackSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        double sleep = 0.01;
        int[] bigNumIndices = Writes.createExternalArray(length);
        bigNumIndices[0] = 0;
        int index = 0;
        int end = length - 1;
        while (end > 0) {
            int start = bigNumIndices[index];
            index = Math.max(index - 1, 0);
            for (int i = start; i <= end; i++) {
                if (Reads.compareIndices(array, i, bigNumIndices[index], sleep, true) >= 0) {
                    Writes.write(bigNumIndices, ++index, i, sleep, true, true);
                }
            }
            if (end != bigNumIndices[index]) {
                Writes.swap(array, end, bigNumIndices[index], sleep, true, false);
            }
            end--;
        }
    }
}