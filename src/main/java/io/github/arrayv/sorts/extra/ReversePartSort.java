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
@SortMeta(name = "Reverse Part")

final public class ReversePartSort extends Sort {
    public ReversePartSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void reversePartSort(int[] array, int length, double sleep, boolean auxwrite) {
        boolean isContinue = true;
        while (isContinue) {
            isContinue = false;
            boolean isReversedSubArray = false;
            int start = 0;
            for (int i = 0; i <= length - 1; i++) {
                if (i == length - 1) {
                    if (isReversedSubArray) {
                        Writes.reversal(array, start, i, 0.3, true, false);
                        isContinue = true;
                    }
                    continue;
                }

                // 前比后小的情况
                if (Reads.compareIndices(array, i, i + 1, sleep, true) == -1) {
                    if (isReversedSubArray) {
                        Writes.reversal(array, start, i, 0.3, true, false);
                        start = i + 1;
                        isContinue = true;
                    } else {
                        start++;
                    }
                } else {
                    isReversedSubArray = true;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        reversePartSort(array, length, 0.01, false);
    }
}