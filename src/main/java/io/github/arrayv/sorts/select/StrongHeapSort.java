package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

//
@SortMeta(name = "Strong Heap")
public class StrongHeapSort extends Sort {

    public StrongHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int boolToInt(boolean what) {
        return what == true ? 1 : 0;
    }

    private int leftChild(int i) {
        return (2 * i) + 1;
    }

    private int sibling(int i) {
        if (i == 0)
            return 0;
        return i + boolToInt(i % 2 != 0) - boolToInt(i % 2 == 0);
    }

    private boolean isLeaf(int i, int n) {
        if (i % 2 != 0)
            return sibling(i) >= n;
        return leftChild(i) >= n;
    }

    private void strengtheningSiftDown(int[] array, int i, int n) {
        int x = array[i];
        while (isLeaf(i, n) == false) {
            int j = sibling(i);
            if (i % 2 == 0) {
                j = leftChild(i);
            } else if (j < n && leftChild(i) < n && Reads.compareIndices(array, leftChild(i), j, 1, true) <= 0) {
                j = leftChild(i);
            }
            Highlights.markArray(0, j);
            Delays.sleep(1);
            if (Reads.compareValues(x, array[j]) <= 0)
                break;
            Writes.write(array, i, array[j], 0.25, true, false);
            i = j;
        }
        Writes.write(array, i, x, 0.25, true, false);
    }

    private int parent(int i) {
        if (i == 0)
            return 0;
        return (i - 1) / 2;
    }

    private int bottomUpSearch(int[] array, int i, int j) {
        while (j > i && Reads.compareIndices(array, j, i, 1, true) >= 0)
            j = parent(j);
        return j;
    }

    private void swapSubtrees(int[] array, int u, int v, int n) {
        int j = 1;
        while (v < n) {
            for (int i = 0; i < j; i++) {
                Writes.swap(array, u + i, v + i, 1, true, false);
            }
            u = leftChild(u);
            v = leftChild(v);
            j *= 2;
        }
    }

    private int leftmostLeaf(int i, int n) {
        while (leftChild(i) < n) {
            i = leftChild(i);
        }
        return i;
    }

    private void liftUp(int[] array, int i, int j, int n) {
        int x = array[j];
        Writes.write(array, j, array[i], 0.25, true, false);
        while (j > i) {
            Writes.swap(array, array[parent(j)], x, 0.25, true, false);
            if (Reads.compareIndices(array, sibling(j), j, 1, true) < 0)
                swapSubtrees(array, j, sibling(j), n);
            j = parent(j);
        }
    }

    private void swappingSiftDown(int[] array, int i, int n) {
        int k = leftmostLeaf(i, n);
        k = bottomUpSearch(array, i, k);
        liftUp(array, i, k, n);
    }

    private void siftDown(int[] array, int i, int n) {
        int x = array[i];
        while (leftChild(i) < n) {
            int j = leftChild(i);
            if (sibling(j) < n && Reads.compareIndices(array, sibling(j), j, 1, true) < 0)
                j = sibling(j);
            if (Reads.compareValues(x, array[j]) <= 0)
                break;
            Writes.write(array, i, array[j], 0.25, true, false);
            i = j;
        }
        Writes.write(array, i, x, 0.25, true, false);
    }

    private void submersion(int[] array, int nn, int n) {
        int r = n - 1;
        int l = Math.max(nn, parent(r) + 1);
        while (r != 0) {
            l = parent(l);
            r = parent(r);
            for (int i = r; i < l; i += r - 1) {
                siftDown(array, i, n);
            }
        }
    }

    private int ancestor(int i, int d) {
        return (int) (((i + 1) / Math.pow(2, d)) - 1);
    }

    private void rotate(int[] array, int i, int k, int h) {
        int x = array[i];
        for (int d = h - 1; d > 0; d -= 2) {
            Writes.write(array, ancestor(k, d + 1), array[ancestor(k, d)], 1, true, false);
        }
        Writes.write(array, k, x, 1, true, false);
    }

    private int correctPlace(int[] array, int i, int k, int h, int d) { // d is the second return value
        d = h;
        while (i != k) {
            int hh = (h + 1) / 2;
            int j = ancestor(k, hh);
            h -= hh;
            if (Reads.compareIndices(array, i, j, 1, true) <= 0) {
                k = j;
                d -= hh;
            } else {
                i = ancestor(k, h);
            }
        }
        return i;
    }

    private void binarySearchSiftUp(int[] array, int i, int k, int h) {
        int d = 0;
        int j = correctPlace(array, i, k, h, d);
        rotate(array, i, j, d);
    }

    private void combinedSiftDown(int[] array, int i, int n, int h) {
        int j = i;
        for (int t = 0; t < h; t++) {
            int k = leftChild(j);
            if (Reads.compareIndices(array, sibling(k), k, 1, true) < 0)
                binarySearchSiftUp(array, i, parent(j), h - 1);
            else {
                rotate(array, i, j, h);
                swappingSiftDown(array, j, n);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int i = sortLength; i > 0; i--) {
            strengtheningSiftDown(array, i, sortLength);
        }
        for (int i = sortLength; i > 0; i--) {
            int tmp2 = array[0];
            Writes.write(array, 0, array[i], 0.25, true, false);
            strengtheningSiftDown(array, 0, i);
            Writes.write(array, i, tmp2, 0.25, true, false);
        }
        Writes.reversal(array, 0, sortLength, 1, true, false);
    }
}
