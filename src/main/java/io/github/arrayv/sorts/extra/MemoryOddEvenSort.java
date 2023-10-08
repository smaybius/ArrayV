package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

import java.util.Arrays;

/*
 * 
MIT License

Copyright (c) 2020-2021 Lu

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
@SortMeta(name = "Memory Odd-Even")
final public class MemoryOddEvenSort extends Sort {
    public MemoryOddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void memoryOddEvenSort(int[] array, int length, double sleep) {
        boolean needSort = true;
        int[] needSortIndexArray = Writes.createExternalArray(length);
        int[] readIndexArray = Writes.createExternalArray(length);
        int size = 0;
        while (needSort) {
            needSort = false;
            int writeIndex = 0;
            int readIndex = 0;
            int right = size == 0 ? 1 : readIndexArray[readIndex++];
            int end = size == 0 ? length : readIndexArray[size - 1];
            while (right < length && right <= end) {
                // find decreasing elements' index
                if (Reads.compareIndices(array, right - 1, right, sleep, true) > 0) {
                    needSort = true;
                    Writes.write(needSortIndexArray, writeIndex++, right, 0, true, true);
                }

                if (size == 0) {
                    right++;
                } else {
                    if (readIndex >= size) {
                        break;
                    }
                    right = readIndexArray[readIndex++];
                }
            }
            final int needSortIndexArraySize = writeIndex;
            writeIndex = 0;
            for (int i = 0; i < needSortIndexArraySize; i++) {
                final int index = needSortIndexArray[i];
                // swap, and record indices of next round
                Writes.swap(array, index - 1, index, sleep, true, false);
                if (index != 1) {
                    if (writeIndex == 0) {
                        Writes.write(readIndexArray, writeIndex++, index - 1, 0, true, true);
                    } else {
                        int insertIndex = Arrays.binarySearch(readIndexArray, Math.max(0, writeIndex - 2), writeIndex,
                                index - 1);
                        if (insertIndex < 0) {
                            insertIndex = -insertIndex - 1;
                            for (int j = writeIndex; j > insertIndex; j--) {
                                Writes.write(readIndexArray, j, readIndexArray[j - 1], 0, true, true);
                            }
                            Writes.write(readIndexArray, insertIndex, index - 1, 0, true, true);
                            writeIndex++;
                        }
                    }
                }
                if (index != length - 1) {
                    Writes.write(readIndexArray, writeIndex++, index + 1, 0, true, true);
                }
            }
            size = writeIndex;
        }
        Writes.deleteExternalArray(needSortIndexArray);
        Writes.deleteExternalArray(readIndexArray);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.memoryOddEvenSort(array, length, 0.01);
    }
}