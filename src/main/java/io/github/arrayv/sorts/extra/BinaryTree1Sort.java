package io.github.arrayv.sorts.extra;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Tree;
import io.github.arrayv.utils.TreeUtil;

/*
 * 
MIT License

Copyright (c) 2021 Lu

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
 *
 */
@SortMeta(name = "Binary Tree 1")
final public class BinaryTree1Sort extends Sort {
    public BinaryTree1Sort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void innerSort(int[] array, final int from, final int to, double sleep) {
        if (from == to) {
            return;
        }
        if (from + 1 == to) {
            if (Reads.compareIndices(array, from, to, sleep, true) == 1) {
                Writes.swap(array, from, to, sleep, true, false);
            }
            return;
        }

        Tree tree = new Tree();
        TreeUtil treeUtil = new TreeUtil(Reads, Writes, Delays, sleep);
        // find middle value
        for (int i = from; i <= to; i++) {
            Highlights.markArray(1, i);
            treeUtil.addNodeToTree(tree, tree.getRoot(), new Tree.Node(array[i], i));
        }
        // Delays.sleep(2000);
        Highlights.clearAllMarks();
        if (tree.getLeftCount() > tree.getRightCount()) {
            treeUtil.reconstruct(tree, true);
        }
        Integer middleValue = tree.getRoot().getValue();
        int index = tree.getRoot().getIndex();

        int middle = (from + to) >> 1;

        if (index != middle) {
            Writes.swap(array, index, middle, sleep, true, false);
        }

        int leftOffset = 0, rightOffset = 0;
        while (true) {
            int bigIndex = -1, smallIndex = -1;
            for (int left = from + leftOffset; left < middle; left++) {
                leftOffset++;
                if (Reads.compareIndexValue(array, left, middleValue, sleep, true) > -1) {
                    bigIndex = left;
                    break;
                }
            }
            for (int right = middle + 1 + rightOffset; right <= to; right++) {
                rightOffset++;
                if (Reads.compareIndexValue(array, right, middleValue, sleep, true) < 1) {
                    smallIndex = right;
                    break;
                }
            }
            if (bigIndex == -1 || smallIndex == -1) {
                break;
            }
            Writes.swap(array, bigIndex, smallIndex, sleep, true, false);
        }
        innerSort(array, from, middle - 1, sleep);
        innerSort(array, middle + 1, to, sleep);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        innerSort(array, 0, length - 1, 0.1);
    }
}