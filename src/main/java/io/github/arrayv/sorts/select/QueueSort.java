package io.github.arrayv.sorts.select;

import java.util.PriorityQueue;

import io.github.arrayv.main.ArrayVisualizer;
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

final public class QueueSort extends Sort {
    public QueueSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Queue");
        this.setRunAllSortsName("Queue Sort");
        this.setRunSortName("Queuesort");
        this.setCategory("Selection Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void seeHeap(int[] array, PriorityQueue<LinkedList<Integer>> registers, int start) {
        int total = start;

        LinkedList<Integer>[] flatRegs = new LinkedList[registers.size()];
        LinkedList<Integer>[] regs = registers.toArray(flatRegs);
        for (int index = 0; index < regs.length; index++) {
            for (int i = 0; i < regs[index].size(); i++) {
                array[total++] = regs[index].get(i);
            }
            array[total - 1] = 0;
        }
    }

    /*
     * public <T extends ArrayVList> void fakeTranscribe(int[] array, T[] registers,
     * int start) {
     * int total = start;
     * 
     * for (int index = 0; index < registers.length; index++) {
     * for (int i = 0; i < registers[index].size(); i++) {
     * this.write(array, total++, registers[index].get(i), 0, false, false);
     * this.writes--;
     * }
     * }
     * }
     */

    private void queueAdd(LinkedList<Integer> q, int val) {
        Writes.startLap();
        q.add(val);
        Writes.stopLap();
    }

    int max;

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int[] flatHeap = Writes.createExternalArray(sortLength);
        max = sortLength;
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

        int currentValue = array[0];
        LinkedList<Integer> currentQueue = new LinkedList<>();
        this.queueAdd(currentQueue, currentValue);
        heap.add(currentQueue);
        seeHeap(flatHeap, heap, 0);
        Writes.changeAllocAmount(1);

        int qCount = 1;

        for (int i = 1; i < sortLength; i++) {
            Highlights.markArray(2, qCount);
            if (Reads.compareIndices(array, i, i - 1, 1, true) >= 0) {
                this.queueAdd(currentQueue, array[i]);
            } else {
                currentValue = array[i];
                currentQueue = new LinkedList<>();
                this.queueAdd(currentQueue, array[i]);
                heap.add(currentQueue);
                qCount++;
            }
            seeHeap(flatHeap, heap, 0);
            Writes.changeAuxWrites(1);
            Writes.changeAllocAmount(1);
        }

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
        Writes.deleteExternalArray(flatHeap);
    }
}