package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class BinomialSmoothSort extends Sort {
    public BinomialSmoothSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Binomial Smooth");
        this.setRunAllSortsName("Binomial Smooth Sort");
        this.setRunSortName("Binomial Smoothsort");
        this.setCategory("Selection Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int height(int node) {
        int count = 0;
        while ((node >> count) % 2 == 1)
            count++;
        return count;
    }

    public void thrift(int[] array, int node, boolean parent, boolean root, int depth) {
        root = root && (node >= (1 << height(node)));
        if (!root && !parent)
            return;
        int choice = height(node) - (root ? 0 : 1);
        if (parent)
            for (int child = choice - 1; child >= 0; child--)
                if (Reads.compareIndices(array, node - (1 << choice), node - (1 << child), 0.2, true) != 1)
                    choice = child;
        if (Reads.compareIndices(array, node - (1 << choice), node, 0.2, true) != 1)
            return;
        Writes.swap(array, node, node - (1 << choice), .65, true, false);
        Writes.recordDepth(depth);
        Writes.recursion();
        thrift(array, node - (1 << choice), (node - (1 << choice)) % 2 == 1, choice == height(node), depth + 1);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // heapify from 0 to length - 1
        int Node;
        for (Node = 1; Node < length; Node++)
            thrift(array, Node, Node % 2 == 1, (Node + (1 << height(Node)) >= length), 0);
        for (Node -= (Node - 1) % 2; Node > 2; Node -= 2)
            // extract largest and second largest(already at end), then heapify from 0 to
            // Node - 1
            for (int Child = height(Node) - 1; Child >= 0; Child--)
                thrift(array, Node - (1 << Child), false, true, 0);
    }
}
