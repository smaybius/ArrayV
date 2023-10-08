package io.github.arrayv.sorts.merge;
/*
	Copyright (C) 2014-2022 Igor van den Hoven ivdhoven@gmail.com
*/

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
	CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
	TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
	SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

/*
	piposort 1.1.5.4
*/
@SortMeta(name = "Pipo")
public class PipoSort extends Sort {

    public PipoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    void branchless_oddeven_sort(int[] array, int start, int nmemb) {
        int swap;
        int pta, pte;
        int w = 1, x, y, z = 1;

        switch (nmemb) {
            default:
                pte = nmemb - 3;
                do {
                    pta = pte + (z = z == 0 ? 1 : 0);

                    do {
                        x = Reads.compareValues(array[pta], array[pta + 1]) > 0 ? 1 : 0;
                        y = x == 0 ? 1 : 0;
                        swap = array[pta + y];
                        Writes.write(array, pta, pta + x, 1, true, false);
                        Writes.write(array, pta + 1, swap, 1, true, false);
                        pta -= 2;
                        w |= x;
                    } while (pta >= start);
                } while (w-- > 0 && --nmemb > 0);
                return;
            case 3:
                pta = start;
                x = Reads.compareValues(array[pta], array[pta + 1]) > 0 ? 1 : 0;
                y = x == 0 ? 1 : 0;
                swap = array[pta + y];
                Writes.write(array, pta, array[pta + x], 1, true, false);
                Writes.write(array, pta + 1, swap, 1, true, false);
                pta++;
                x = Reads.compareValues(array[pta], array[pta + 1]) > 0 ? 1 : 0;
                y = x == 0 ? 1 : 0;
                swap = array[pta + y];
                Writes.write(array, pta, array[pta + x], 1, true, false);
                Writes.write(array, pta + 1, swap, 1, true, false);
                if (x == 0)
                    return;
            case 2:
                pta = start;
                x = Reads.compareValues(array[pta], array[pta + 1]) > 0 ? 1 : 0;
                y = x == 0 ? 1 : 0;
                swap = array[pta + y];
                Writes.write(array, pta, array[pta + x], 1, true, false);
                Writes.write(array, pta + 1, swap, 1, true, false);
            case 1:
            case 0:
                return;
        }
    }

    void oddeven_parity_merge(int[] from, int start, int[] dest, int deststart, int left, int right) {
        int ptl, ptr, tpl, tpr, tpd, ptd;
        int x;

        ptl = start;
        ptr = start + left;
        ptd = deststart;
        tpl = start + left - 1;
        tpr = start + left + right - 1;
        tpd = deststart + left + right - 1;

        if (left < right) {
            dest[ptd++] = Reads.compareValues(from[ptl], from[ptr]) <= 0 ? from[ptl++] : from[ptr++];
        }

        while (--left > 0) {
            x = Reads.compareValues(from[ptl], from[ptr]) <= 0 ? 1 : 0;
            dest[ptd] = from[ptl];
            ptl += x;
            dest[ptd + x] = from[ptr];
            ptr += x == 0 ? 1 : 0;
            ptd++;
            x = Reads.compareValues(from[tpl], from[tpr]) <= 0 ? 1 : 0;
            dest[tpd] = from[tpl];
            tpl -= x == 0 ? 1 : 0;
            tpd--;
            dest[tpd + x] = from[tpr];
            tpr -= x;
        }
        dest[tpd] = Reads.compareValues(from[tpl], from[tpr]) > 0 ? from[tpl] : from[tpr];
        dest[ptd] = Reads.compareValues(from[ptl], from[ptr]) <= 0 ? from[ptl] : from[ptr];
    }

    public void auxiliary_rotation(int[] array, int[] swap, int left, int right) {
        Writes.arraycopy(array, 0, swap, 0, left, 1, true, true);

        Writes.arraycopy(array, left, array, 0, right, 1, true, false);

        Writes.arraycopy(swap, 0, array, right, left, 1, true, false);
    }

    void ping_pong_merge(int[] array, int start, int[] swap, int swapstart, int nmemb) {
        int quad1, quad2, quad3, quad4, half1, half2;

        if (nmemb <= 7) {
            branchless_oddeven_sort(array, start, nmemb);
            return;
        }
        half1 = nmemb / 2;
        quad1 = half1 / 2;
        quad2 = half1 - quad1;
        half2 = nmemb - half1;
        quad3 = half2 / 2;
        quad4 = half2 - quad3;

        ping_pong_merge(array, start, swap, swapstart, quad1);
        ping_pong_merge(array, start + quad1, swap, swapstart, quad2);
        ping_pong_merge(array, start + half1, swap, swapstart, quad3);
        ping_pong_merge(array, start + half1 + quad3, swap, swapstart, quad4);

        if (Reads.compareValues(array[quad1 - 1], array[quad1]) <= 0
                && Reads.compareValues(array[half1 - 1], array[half1]) <= 0
                && Reads.compareValues(array[half1 + quad3 - 1], array[half1 + quad3]) <= 0) {
            return;
        }

        if (Reads.compareValues(array[start], array[half1 - 1]) > 0
                && Reads.compareValues(array[quad1], array[half1 + quad3 - 1]) > 0
                && Reads.compareValues(array[half1], array[nmemb - 1]) > 0) {
            auxiliary_rotation(array, swap, quad1, quad2 + half2);
            auxiliary_rotation(array, swap, quad2, half2);
            auxiliary_rotation(array, swap, quad3, quad4);
            return;
        }

        oddeven_parity_merge(array, start, swap, swapstart, quad1, quad2);
        oddeven_parity_merge(array, start + half1, swap, swapstart + half1, quad3, quad4);
        oddeven_parity_merge(swap, swapstart, array, start, half1, half2);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int[] swap = Writes.createExternalArray(sortLength);
        ping_pong_merge(array, 0, swap, 0, sortLength);
        Writes.deleteExternalArray(swap);
    }

}
