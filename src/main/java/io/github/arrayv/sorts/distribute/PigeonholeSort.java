package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS
 * LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 *
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE.
 * TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN
 * CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 */

// Code refactored from the Python implementation found here: https://en.wikipedia.org/wiki/Pigeonhole_sort
@SortMeta(name = "Pigeonhole")
public final class PigeonholeSort extends Sort {
    public PigeonholeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < sortLength; i++) {
            if (array[i] < min) {
                min = array[i];
            }
            if (array[i] > max) {
                max = array[i];
            }
        }

        int mi = min;
        int size = max - mi + 1;
        int[] holes = Writes.createExternalArray(size);

        for (int x = 0; x < sortLength; x++) {
            Writes.write(holes, array[x] - mi, holes[array[x] - mi] + 1, 1, false, true);
            Highlights.markArray(1, x);
        }

        int j = 0;

        for (int count = 0; count < size; count++) {
            while (holes[count] > 0) {
                Writes.write(holes, count, holes[count] - 1, 0, false, true);
                Writes.write(array, j, count + mi, 1, false, false);

                Highlights.markArray(1, j);
                j++;
            }
        }

        Writes.deleteExternalArray(holes);
    }
}
