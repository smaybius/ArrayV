package io.github.arrayv.sorts.templates;

import java.util.ArrayList;

import io.github.arrayv.main.ArrayVisualizer;
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

public abstract class ShatterSorting extends Sort {
    // TODO: The "sort time" for both shatter sorts is *wildly* too generous.
    protected ShatterSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void transcribe(int[] array, ArrayVList[] registers, int start) {
        int total = start;

        for (int index = 0; index < registers.length; index++) {
            for (int i = 0; i < registers[index].size(); i++) {
                array[total++] = registers[index].get(i);
            }
        }
    }

    protected void shatterPartition(int[] array, int length, int num) {
        int shatters = (int) Math.ceil(length / (double) num);

        ArrayVList[] registers = new ArrayVList[shatters];
        int[] flatRegs = Writes.createExternalArray(length);

        for (int i = 0; i < shatters; i++) {
            registers[i] = new ArrayVList(false);
        }

        for (int i = 0; i < length; i++) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, array[i] / num);
            registers[array[i] / num].add(array[i], 1, true);
            transcribe(flatRegs, registers, 0);
        }
        Highlights.clearAllMarks();
        Writes.transcribe(array, registers, 0, true, false);
        Writes.deleteExternalArray(flatRegs);
        Writes.deleteExternalArray(registers);
    }

    protected void shatterSort(int[] array, int length, int num) {
        int shatters = (int) Math.ceil(length / (double) num);

        shatterPartition(array, length, num);

        int[] tmp = Writes.createExternalArray(num);
        for (int i = 0; i < shatters; i++) {
            for (int j = 0; j < num; j++) {
                if (i * num + j >= length)
                    Writes.write(tmp, j, -1, 0.5, false, true);
                else
                    Writes.write(tmp, j, array[i * num + j], 0.5, false, true);

                Highlights.markArray(2, i * num + j);
            }

            Highlights.clearMark(2);

            for (int j = 0; j < tmp.length; j++) {
                int tmpj = tmp[j];

                if (i * num + (tmpj % num) >= length || tmpj == -1) {
                    break;
                }

                Writes.write(array, i * num + (tmpj % num), tmpj, 1, false, false);
                Highlights.markArray(1, i * num + (tmpj % num));
            }

            Highlights.clearMark(1);
        }

        Writes.deleteExternalArray(tmp);
    }

    protected void simpleShatterSort(int[] array, int length, int num, int rate) {
        for (int i = num; i > 1; i = i / rate) {
            shatterPartition(array, length, i);
        }
        shatterPartition(array, length, 1);
    }
}
