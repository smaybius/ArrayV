package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class DiamondSortParallel extends Sort {
    private final double DELAY = 0.05;

    public DiamondSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Diamond (Parallel)");
        this.setRunAllSortsName("Parallel Diamond Sort");
        this.setRunSortName("Parallel Diamondsort");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] arr;

    private class Diamond extends Thread {
        private int start, stop;
        private boolean merge;

        Diamond(int start, int stop, boolean merge) {
            this.start = start;
            this.stop = stop;
            this.merge = merge;
        }

        public void run() {
            DiamondSortParallel.this.sort(start, stop, merge);
        }
    }

    private void sort(int start, int stop, boolean merge) {
        if (stop - start == 2) {
            if (Reads.compareIndices(arr, start, stop - 1, DELAY, true) == 1)
                Writes.swap(arr, start, stop - 1, DELAY, true, false);
        } else if (stop - start >= 3) {
            double div = (stop - start) / 4d;
            int mid = (stop - start) / 2 + start;
            if (merge) {
                Diamond left = new Diamond(start, mid, true);
                Diamond right = new Diamond(mid, stop, true);
                left.start();
                right.start();

                try {
                    left.join();
                    right.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            Diamond a1 = new Diamond((int) div + start, (int) (div * 3) + start, false);
            Diamond a2 = new Diamond(start, mid, false);
            Diamond a3 = new Diamond(mid, stop, false);
            Diamond a4 = new Diamond((int) div + start, (int) (div * 3) + start, false);
            a1.start();
            a2.start();
            a3.start();
            a4.start();

            try {
                a1.join();
                a2.join();
                a3.join();
                a4.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void runSort(int[] arr, int length, int buckets) {
        this.arr = arr;
        this.sort(0, length, true);
    }
}
