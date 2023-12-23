package io.github.arrayv.sorts.select;

import java.util.PriorityQueue;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.ArrayVList;

import java.util.Comparator;
import java.util.LinkedList;

/*
MIT License

Copyright (c) 2020 Gaming32

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
*/
@SortMeta(name = "Dequeue")
final public class DequeueSort extends Sort {
    public DequeueSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void seeHeap(ArrayVList array, PriorityQueue<LinkedList<Integer>> registers, int start) {
        array.mockReset();
        LinkedList<Integer>[] flatRegs = new LinkedList[registers.size()];
        LinkedList<Integer>[] regs = registers.toArray(flatRegs);
        for (int index = 0; index < regs.length; index++) {
            for (int i = 0; i < regs[index].size(); i++) {
                array.mockAdd(regs[index].get(i));
            }
        }
    }

    private void queueAddLeft(LinkedList<Integer> q, int val) {
        Writes.startLap();
        q.addFirst(val);
        Writes.stopLap();
    }

    private void queueAddRight(LinkedList<Integer> q, int val) {
        Writes.startLap();
        q.addLast(val);
        Writes.stopLap();
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        ArrayVList flatHeap = Writes.createMockExternalArray(sortLength);
        PriorityQueue<LinkedList<Integer>> heap = new PriorityQueue<>(sortLength / 2,
                new Comparator<LinkedList<Integer>>() {
                    @Override
                    public int compare(LinkedList<Integer> q1, LinkedList<Integer> q2) {
                        Highlights.markArray(0, q1.peek());
                        Highlights.markArray(1, q2.peek());
                        Delays.sleep(0.3);
                        return Reads.compareValues(q1.peek(), q2.peek());
                    }
                });

        int currentValueRight = array[0];
        int currentValueLeft = array[0];
        LinkedList<Integer> currentQueue = new LinkedList<>();
        this.queueAddRight(currentQueue, currentValueRight);
        Writes.changeAllocAmount(1);

        int qCount = 1;

        for (int i = 1; i < sortLength; i++) {
            Highlights.markArray(2, qCount);
            if (Reads.compareValues(array[i], currentValueRight) >= 0) {
                currentValueRight = array[i];
                this.queueAddRight(currentQueue, array[i]);
            } else if (Reads.compareValues(array[i], currentValueLeft) <= 0) {
                currentValueLeft = array[i];
                this.queueAddLeft(currentQueue, array[i]);
            } else {
                currentValueRight = array[i];
                currentValueLeft = array[i];
                heap.add(currentQueue);
                currentQueue = new LinkedList<>();
                this.queueAddRight(currentQueue, array[i]);
                qCount++;
            }
            Writes.changeAuxWrites(1);
            Writes.changeAllocAmount(1);
            seeHeap(flatHeap, heap, 0);
            Delays.sleep(1);
        }
        heap.add(currentQueue);

        int j = 0;
        while (qCount > 0) {
            Highlights.markArray(2, qCount);
            LinkedList<Integer> first = heap.poll();
            Writes.write(array, j++, first.pop(), 1, true, false);
            Writes.changeAllocAmount(-1);
            if (first.size() > 0) {
                heap.add(first);
            } else {
                qCount--;
            }
            seeHeap(flatHeap, heap, 0);
        }
        Writes.deleteMockExternalArray(flatHeap);
    }
}