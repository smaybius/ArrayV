package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BestForNSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Best-for-N Stooge", question = "Enter the run length for this sort:\\n" + //
        "(Non-2*n will be rounded up)", defaultAnswer = 64, category = "Best-for-N Sorts")
final public class BestForNStoogeSort extends BestForNSorting {

    int maxtouse;

    public BestForNStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void bestNStooge(int[] array, int s, int l, int depth) {
        Writes.recordDepth(depth);
        if (l <= 2 * maxtouse) {
            initNetwork(array, s, l / 2);
            initNetwork(array, s + l / 2, l / 2);
            initNetwork(array, s + l / 4, l / 2);
            initNetwork(array, s, l / 2);
            initNetwork(array, s + l / 2, l / 2);
            initNetwork(array, s + l / 4, l / 2);
        } else {
            Writes.recursion();
            bestNStooge(array, s, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 2, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 4, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 2, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 4, l / 2, depth + 1);
        }
    }

    public void bestNStoogeMain(int[] array, int start, int length, int base, int depth, double del) {
        delay = del;
        Writes.recordDepth(depth);
        if (length <= maxtouse) {
            initNetwork(array, start, length);
        } else {
            int effectivelen = base;
            while (effectivelen <= length)
                effectivelen *= 2;
            effectivelen /= 2;
            bestNStooge(array, start, effectivelen, depth);
            if (effectivelen != length) {
                Writes.recursion();
                bestNStoogeMain(array, start + effectivelen, length - effectivelen, base, depth + 1, del);
                bestNStooge(array, start + (length - effectivelen) / 2, effectivelen, depth);
                bestNStooge(array, start, effectivelen, depth);
                bestNStooge(array, start + length - effectivelen, effectivelen, depth);
                bestNStooge(array, start + (length - effectivelen) / 2, effectivelen, depth);
            }
        }
    }

    public static int validateAnswer(int answer) {
        if (answer % 2 == 1)
            answer++;
        if (answer < 2)
            return 2;
        if (answer > 64)
            return 64;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        maxtouse = base;
        bestNStoogeMain(array, 0, currentLength, base, 0, 0.125);
    }
}