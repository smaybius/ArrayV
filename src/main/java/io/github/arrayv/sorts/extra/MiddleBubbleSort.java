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
@SortMeta(name = "Middle Bubble")
final public class MiddleBubbleSort extends Sort {
    public MiddleBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        double sleep = 0.01;
        int start = 0, end = length - 1;
        double percent = 0.9;
        int middle = (int) (start + (end - start) * percent);
        while (end > 0) {
            while (middle >= 0) {
                for (int i = middle; i < end; i++) {
                    if (Reads.compareIndices(array, i, i + 1, sleep, true) == 1) {
                        Writes.swap(array, i, i + 1, sleep, true, false);
                    }
                }
                middle--;
            }
            end = (int) (start + (end - start) * (1 - percent));
            middle = (int) (start + (end - start) * percent);
        }
    }
}