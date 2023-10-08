package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.hybrid.IntroCircleSortIterative;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.ArrayVList;

/*
 *
MIT License

Copyright (c) 2019 Lancewer

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
@SortMeta(name = "Interweave")
public class InterweaveSort extends Sort {

    public InterweaveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    int average1, average2, pivot;
    int ofs;

    void avgList(int[] list, ArrayVList temp, int len) {
        average1 = 0;
        int i = 0;
        for (int k = 0; k < Math.ceil(len / 2.0); k++) {
            average1 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average1 /= Math.ceil(len / 2.0);
        average2 = 0;
        for (int k = 0; k < Math.floor(len / 2.0); k++) {
            average2 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average2 /= Math.floor(len / 2.0);
        if (Reads.compareValues(average1, average2) > 0) {
            temp.clear();
            i = 0;
            for (int k = 0; k < Math.floor(len / 2.0); k++) {
                temp.add(list[ofs + i]);
                Writes.write(list, ofs + i, list[ofs + i + (int) (Math.floor(len / 2.0))], 1, true, false);
                i++;
            }
            for (int k = 0; k < Math.floor(len / 2.0); k++) {
                Writes.write(list, ofs + i, temp.get(i - (int) (Math.floor(len / 2.0))), 1, true, false);
                i++;
            }
        }
    }

    void avgLeft(int[] list, ArrayVList temp, int len) {
        int i = 0;
        average1 = 0;
        for (int k = 0; k < Math.ceil(len / 4.0); k++) {
            average1 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average1 /= Math.ceil(len / 4.0);
        average2 = 0;
        for (int k = 0; k < Math.floor(len / 4.0); k++) {
            average2 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average2 /= Math.floor(len / 4.0);
        if (Reads.compareValues(average1, average2) > 0) {
            temp.clear();
            i = 0;
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                temp.add(list[ofs + i]);
                Writes.write(list, ofs + i, list[ofs + i + (int) (Math.floor(len / 4.0))], 1, true, false);
                i++;
            }
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                Writes.write(list, ofs + i, temp.get(i - (int) (Math.floor(len / 4.0))), 1, true, false);
                i++;
            }
        }
    }

    void avgRight(int[] list, ArrayVList temp, int len) {
        int i = (int) Math.floor(len / 2.0);
        average1 = 0;
        for (int k = 0; k < Math.ceil(len / 4.0); k++) {
            average1 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average1 /= Math.ceil(len / 4.0);
        average2 = 0;
        for (int k = 0; k < Math.floor(len / 4.0); k++) {
            average2 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average2 /= Math.floor(len / 4.0);
        if (Reads.compareValues(average1, average2) > 0) {
            temp.clear();
            i = (int) Math.floor(len / 2.0);
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                temp.add(list[ofs + i]);
                Writes.write(list, ofs + i, list[ofs + i + (int) (Math.floor(len / 4.0))], 1, true, false);
                i++;
            }
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                Writes.write(list, ofs + i, temp.get(i - (int) (3 * Math.floor(len / 4.0))), 1, true, false);
                i++;
            }
        }
    }

    void weave(int[] list, ArrayVList temp, ArrayVList temp2, int len) {
        temp.clear();
        temp2.clear();
        int i = 0;
        for (int k = 0; k < Math.ceil(len / 2.0); k++) {
            temp.add(list[ofs + i]);
            i++;
        }
        for (int k = 0; k < Math.floor(len / 2.0); k++) {
            temp2.add(list[ofs + i]);
            i++;
        }
        i = 1;
        while (i <= len) {
            Writes.write(list, ofs + i - 1, temp.get((int) Math.ceil(i / 2.0) - 1), 1, true, false);
            if (i < len)
                Writes.write(list, ofs + i, temp2.get((int) Math.ceil(i / 2.0) - 1), 1, true, false);
            i += 2;
        }
    }

    void getMedianPivot(int[] list, ArrayVList temp, ArrayVList temp2, int len) {
        temp.clear();
        pivot = (list[ofs] + list[ofs + len - 1]) / 2;
        int i = 0;
        int j = 0;
        for (int k = 0; k < len; k++) {
            Highlights.markArray(0, ofs + i);
            Highlights.markArray(1, pivot);
            Delays.sleep(1);
            if (Reads.compareValues(list[ofs + i], pivot) > 0) {
                Writes.write(list, ofs + j, list[ofs + i], 1, true, false);
                j++;
            } else {
                temp.add(list[ofs + i]);
            }
            i++;
            Highlights.clearAllMarks();
        }
        i = 0;
        for (int k = 0; k < temp.size(); k++) {
            Writes.write(list, ofs + j, temp.get(i), 1, true, false);
            i++;
            j++;
        }
    }

    public void sort(int[] array, int a, int b) {
        int sortLength = b - a;
        this.ofs = a;
        ArrayVList temp = Writes.createArrayList();
        ArrayVList temp2 = Writes.createArrayList();
        for (int i = 0; i < 2; i++) {
            avgList(array, temp, sortLength);
            avgLeft(array, temp, sortLength);
            avgRight(array, temp, sortLength);
            weave(array, temp, temp2, sortLength);
            weave(array, temp, temp2, sortLength);
            getMedianPivot(array, temp, temp2, sortLength);
        }
        Writes.deleteArrayList(temp);
        Writes.deleteArrayList(temp2);
        IntroCircleSortIterative inserter = new IntroCircleSortIterative(arrayVisualizer);
        inserter.circleSort(array, a, b, 0.5);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
