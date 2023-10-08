package io.github.arrayv.sorts.misc;

import java.util.ArrayList;
import java.util.BitSet;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.BlockInsertionSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.ArrayVList;
import io.github.arrayv.utils.IndexedRotations;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Distray, PCBoy and stentor

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author Distray
 * @author PCBoy
 * @author stentor
 *
 */
@SortMeta(name = "Pattern-Defeating Safe Stalin", category = "Impractical Sorts", slowSort = true, unreasonableLimit = 8192)
public final class PDSafeStalinSort extends Sort {

    public PDSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    int firstlen;
    int secondlen;

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if (i < b)
            dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else
            dir = true;
        if (dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) >= 0)
                i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public boolean patternDefeat(int[] array, int a, int b) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b)
                noSort = false;
            j = i++;
        }
        return noSort;
    }

    protected ArrayList<ArrayVList> buildStacks(int[] array, BitSet bits, int a, int b) {
        ArrayList<ArrayVList> stacksBuilt = new ArrayList<>();
        int zeroed = 0;
        while (zeroed < b - a) {
            ArrayVList currentStack = new ArrayVList();
            for (int j = a; j < b; j++)
                if (!bits.get(j - a))
                    if (currentStack.size() == 0 || Reads.compareValues(currentStack.peek(), array[j]) <= 0) {
                        currentStack.add(array[j]);
                        Highlights.markArray(1, j);
                        Delays.sleep(1);
                        bits.set(j - a);
                        zeroed++;
                    }
            currentStack.unwatch();
            stacksBuilt.add(currentStack);
            ArrayVList[] arrayStacks = new ArrayVList[stacksBuilt.size()];
            Writes.fakeTranscribe(flatstacks, stacksBuilt.toArray(arrayStacks), 0);
        }
        bits.clear();
        return stacksBuilt;
    }

    protected void reciteStacks(int[] array, int start, int end, ArrayList<ArrayVList> stacks) {
        int ptr = start;
        int stackdone = 0;
        while (stacks.size() > 0) {
            ArrayVList first = stacks.remove(0);
            ArrayVList[] arrayStacks = new ArrayVList[stacks.size()];
            Writes.fakeTranscribe(flatstacks, stacks.toArray(arrayStacks), 0);
            if (stackdone == 0)
                firstlen = first.size();
            if (stackdone == 1)
                secondlen = first.size();
            int n = ptr + first.size() - 1;
            while (first.size() > 0) {
                Writes.write(array, n--, first.pop(), 1, true, false);
                ptr++;
            }
            first.unwatch();
            stackdone++;
        }
    }

    protected int stepDown(int[] array, int n) {
        int steps = 1;
        int finals = n - 2;
        while (Reads.compareIndices(array, secondlen - 1, finals, 1, true) <= 0) {
            finals--;
            steps++;
        }
        Highlights.clearAllMarks();
        return steps;
    }

    private int[] flatstacks;

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        flatstacks = Writes.createMockExternalArray(length);
        if (patternDefeat(array, 0, length))
            return;
        BlockInsertionSort two = new BlockInsertionSort(arrayVisualizer);
        int n = length;
        boolean check = false;
        BitSet bits = new BitSet(length);
        while (!check && n > 1) {
            ArrayList<ArrayVList> stacks = buildStacks(array, bits, 0, n);
            int size = stacks.size();
            reciteStacks(array, 0, n, stacks);
            if (size > 2) {
                IndexedRotations.adaptable(array, 0, firstlen, n, 1, true, false);
                if (!check)
                    n -= stepDown(array, n);
            } else {
                if (size == 2)
                    two.insertionSort(array, 0, n);
                check = true;
            }
        }
        Writes.deleteMockExternalArray(flatstacks);
    }
}