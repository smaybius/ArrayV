package io.github.arrayv.sorts.distribute;

import java.math.BigInteger;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**************************************
 * More Optimized Index Sort: Whoops! *
 * Optimized too much! (O(1) best & *
 * average aux, O(n/2) worst case *
 * aux, O(k) best, O(n) average and *
 * worst case, O(n+k) worst case *
 * datawrites) *
 * *
 * 
 * @author Distay *
 **************************************/
@SortMeta(name = "More Optimized Index v1.2")
final public class MoreOptimizedIndexSort12 extends Sort {
    public MoreOptimizedIndexSort12(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private BigInteger bitlist;

    private boolean bitIsSet(BigInteger b, int loc) {
        return b.testBit(loc);
    }

    private void setBit(int loc) {
        bitlist = bitlist.setBit(loc);
    }

    private void runMopSort(int[] array, int sortLength, int bucketCount) {
        MoreOptimizedPigeonholeSort mop = new MoreOptimizedPigeonholeSort(arrayVisualizer);
        try {
            mop.runSort(array, sortLength, bucketCount);
        } catch (Exception e) {
            // no
        }
    }

    private int getVal(int[] array, int index) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(array[index]) : array[index];
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int min = Reads.analyzeMin(array, sortLength, 0.5, true);
        this.bitlist = BigInteger.ZERO;
        for (int i = 0; i < sortLength; i++) {
            if (bitIsSet(bitlist, i)) {
                Highlights.markArray(2, i);
                Delays.sleep(0.1);
                continue;
            }
            Highlights.markArray(2, i);
            int current = getVal(array, i), previous = Integer.MIN_VALUE;
            long cmpCount = 0;
            while (Reads.compareValues(i, current - min) != 0 && previous != current && cmpCount < sortLength - i) {
                int tmp = getVal(array, current - min);
                Writes.write(array, current - min, current, 0.5, true, false);
                setBit(current - min);
                previous = current;
                current = tmp;
                cmpCount++;
            }
            if (cmpCount > 0)
                Writes.write(array, i, current, 0.5, true, false);
            if (cmpCount >= sortLength - i - 1) {
                break;
            }
            if (previous == current) {
                this.runMopSort(array, sortLength, bucketCount);
                break;
            }
            Delays.sleep(0.5);
            Highlights.clearMark(1);
        }
    }
}