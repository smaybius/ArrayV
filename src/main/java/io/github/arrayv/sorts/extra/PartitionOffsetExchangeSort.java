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
@SortMeta(name = "Partition Offset Exchange")
final public class PartitionOffsetExchangeSort extends Sort {
    public PartitionOffsetExchangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void innerSort(int[] array, int from, int to, double sleep, boolean auxwrite) {
        if (from == to) {
            return;
        }
        if (from + 1 == to) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
            }
            return;
        }

        int i = from;
        final int middle = ((to + 1 - from) >> 1) + from;
        int j = middle;
        int offset = 0;
        while (offset <= middle) {
            if (Reads.compareIndices(array, i, j, sleep, true) == 1) {
                Writes.swap(array, i, j, sleep, true, auxwrite);
            }
            i = (i + 1) % (middle);
            if (i == 0) {
                i = from;
                offset++;
                j = middle + offset;
            } else {
                j++;
            }
            if (j > to) {
                j = middle;
            }
        }

        innerSort(array, from, middle, sleep, auxwrite);
        innerSort(array, middle, to, sleep, auxwrite);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        innerSort(array, 0, length - 1, 0.01, false);
    }
}