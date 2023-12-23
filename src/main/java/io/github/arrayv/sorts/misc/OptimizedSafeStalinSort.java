package io.github.arrayv.sorts.misc;

import java.util.ArrayList;
import java.util.Stack;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.BlockInsertionSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.ArrayVList;
import io.github.arrayv.utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES
IN COLLABORATION WITH STENTOR AND DISTRAY

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
@SortMeta(name = "Optimized Safe Stalin", category = "Impractical Sorts", slowSort = true, unreasonableLimit = 8192)
final public class OptimizedSafeStalinSort extends Sort {

    int firstlen;
    int secondlen;

    public OptimizedSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected ArrayList<ArrayVList> buildStacks(int[] array, int start, int end) {
        ArrayList<ArrayVList> stacksBuilt = new ArrayList<>();
        int zero = Integer.MIN_VALUE, zeroed = 0;
        while (zeroed < end - start) {
            ArrayVList currentStack = new ArrayVList();
            for (int j = start; j < end; j++) {
                if (array[j] != zero) {
                    if (currentStack.size() == 0 || Reads.compareValues(currentStack.peek(), array[j]) <= 0) {
                        currentStack.add(array[j]);
                        Writes.changeAllocAmount(1);
                        Writes.changeAuxWrites(1);
                        Highlights.markArray(1, j);
                        Delays.sleep(1);
                        Writes.write(array, j, zero, 0, false, false);
                        zeroed++;
                    }
                }
            }
            currentStack.unwatch();
            stacksBuilt.add(currentStack);
            ArrayVList[] arrayStacks = new ArrayVList[stacksBuilt.size()];
            Writes.fakeTranscribe(flatStacks, stacksBuilt.toArray(arrayStacks), 0);
        }
        return stacksBuilt;
    }

    protected void reciteStacks(int[] array, int start, int end, ArrayList<ArrayVList> stacks) {
        int ptr = start;
        int stackdone = 0;
        while (stacks.size() > 0) {
            ArrayVList first = stacks.remove(0);
            if (stackdone == 0)
                firstlen = first.size();
            if (stackdone == 1)
                secondlen = first.size();
            int n = ptr + first.size() - 1;
            ArrayVList[] arrayStacks = new ArrayVList[stacks.size()];
            Writes.fakeTranscribe(flatStacks, stacks.toArray(arrayStacks), 0);
            first.watch();
            while (first.size() != 0) {
                Writes.write(array, n--, first.pop(), 1, true, false);
                ptr++;
            }
            first.unwatch();
            stackdone++;
        }
    }

    protected int stepDown(int[] array, int end) {
        int steps = 1;
        int finals = end - 2;
        while (Reads.compareIndices(array, secondlen - 1, finals, 1, true) <= 0) {
            finals--;
            steps++;
        }
        Highlights.clearAllMarks();
        return steps;
    }

    ArrayVList flatStacks;

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        BlockInsertionSort two = new BlockInsertionSort(arrayVisualizer);
        flatStacks = new ArrayVList();
        int workinglength = currentLength;
        boolean check = false;
        while (!check && workinglength > 1) {
            ArrayList<ArrayVList> stacks = buildStacks(array, 0, workinglength);
            int size = stacks.size();
            reciteStacks(array, 0, workinglength, stacks);
            if (size > 2) {
                IndexedRotations.adaptable(array, 0, firstlen, workinglength, 1, true, false);
                if (!check)
                    workinglength -= stepDown(array, workinglength);
            } else {
                if (size == 2)
                    two.insertionSort(array, 0, workinglength);
                check = true;
            }
        }
        Writes.deleteMockExternalArray(flatStacks);
    }
}