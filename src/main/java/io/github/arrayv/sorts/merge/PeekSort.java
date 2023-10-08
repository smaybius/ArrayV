package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * Implementation of peeksort as described in the paper.
 *
 * If the subproblem has size at most insertionsortThreshold,
 * it is sorted by straight insertion sort instead of merging.
 * If onlyIncreasingRuns is true, we only find weakly increasing runs
 * while peeking into the middle. That simplifies run detection a bit,
 * but it does not detect descending runs.
 *
 * @author Sebastian Wild (wild@uwaterloo.ca)
 * @license: MIT
 */
@SortMeta(name = "Peek", question = "Look for only increasing runs? (0: no, 1 (default): yes)", defaultAnswer = 1)
public class PeekSort extends Sort {
    private final PowerSort powerSort;
    private boolean onlyIncreasingRuns;

    public PeekSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        powerSort = new PowerSort(arrayVisualizer);
    }

    @Override
    public void runSort(int[] A, int right, int bucketCount) throws Exception {
        if (bucketCount == 0)
            onlyIncreasingRuns = false;
        else
            onlyIncreasingRuns = true;

        if (onlyIncreasingRuns)
            peeksortOnlyIncreasing(A, 0, right - 1);
        else
            peeksort(A, 0, right - 1);
    }

    public void peeksort(final int[] a, final int l, final int r) {
        int n = r - l + 1;
        int[] aux = Writes.createExternalArray(n);
        peeksort(a, l, r, l, r, aux);
        Writes.deleteExternalArray(aux);
    }

    public void peeksortOnlyIncreasing(final int[] a, final int l, final int r) {
        int n = r - l + 1;
        int[] aux = Writes.createExternalArray(n);
        peeksortOnlyIncreasing(a, l, r, l, r, aux);
        Writes.deleteExternalArray(aux);
    }

    private static int insertionSortThreshold = 10;

    public void peeksort(int[] A, int left, int right, int leftRunEnd, int rightRunStart, final int[] B) {
        if (leftRunEnd == right || rightRunStart == left)
            return;
        if (right - left + 1 <= insertionSortThreshold) {
            // Possible optimization: use insertionsortRight if right run longer.
            powerSort.insertionsort.insertionsort(A, left, right, leftRunEnd - left + 1);
            return;
        }
        int mid = left + ((right - left) >> 1);
        if (mid <= leftRunEnd) {
            // |XXXXXXXX|XX X|
            peeksort(A, leftRunEnd + 1, right, leftRunEnd + 1, rightRunStart, B);
            powerSort.mergesAndRuns.mergeRuns(A, left, leftRunEnd + 1, right, B);
        } else if (mid >= rightRunStart) {
            // |XX X|XXXXXXXX|
            peeksort(A, left, rightRunStart - 1, leftRunEnd, rightRunStart - 1, B);
            powerSort.mergesAndRuns.mergeRuns(A, left, rightRunStart, right, B);
        } else {
            // find middle run
            final int i, j;
            if (Reads.compareIndices(A, mid, mid + 1, 0.5, true) <= 0) {
                i = powerSort.mergesAndRuns.extendWeaklyIncreasingRunLeft(A, mid,
                        left == leftRunEnd ? left : leftRunEnd + 1);
                j = mid + 1 == rightRunStart ? mid
                        : powerSort.mergesAndRuns.extendWeaklyIncreasingRunRight(A, mid + 1,
                                right == rightRunStart ? right : rightRunStart - 1);
            } else {
                i = powerSort.mergesAndRuns.extendStrictlyDecreasingRunLeft(A, mid,
                        left == leftRunEnd ? left : leftRunEnd + 1);
                j = mid + 1 == rightRunStart ? mid
                        : powerSort.mergesAndRuns.extendStrictlyDecreasingRunRight(A, mid + 1,
                                right == rightRunStart ? right : rightRunStart - 1);
                powerSort.mergesAndRuns.reverseRange(A, i, j);
            }
            if (i == left && j == right)
                return;
            if (mid - i < j - mid) {
                // |XX x|xxxx X|
                peeksort(A, left, i - 1, leftRunEnd, i - 1, B);
                peeksort(A, i, right, j, rightRunStart, B);
                powerSort.mergesAndRuns.mergeRuns(A, left, i, right, B);
            } else {
                // |XX xxx|x X|
                peeksort(A, left, j, leftRunEnd, i, B);
                peeksort(A, j + 1, right, j + 1, rightRunStart, B);
                powerSort.mergesAndRuns.mergeRuns(A, left, j + 1, right, B);
            }
        }
    }

    public void peeksortOnlyIncreasing(int[] A, int left, int right, int leftRunEnd, int rightRunStart,
            final int[] B) {
        if (leftRunEnd == right || rightRunStart == left)
            return;
        if (right - left + 1 <= insertionSortThreshold) {
            powerSort.insertionsort.insertionsort(A, left, right);
            return;
        }
        int mid = left + ((right - left) >> 1);
        if (mid <= leftRunEnd) {
            // |XXXXXXXX|XX X|
            peeksortOnlyIncreasing(A, leftRunEnd + 1, right, leftRunEnd + 1, rightRunStart, B);
            powerSort.mergesAndRuns.mergeRuns(A, left, leftRunEnd + 1, right, B);
        } else if (mid >= rightRunStart) {
            // |XX X|XXXXXXXX|
            peeksortOnlyIncreasing(A, left, rightRunStart - 1, leftRunEnd, rightRunStart - 1, B);
            powerSort.mergesAndRuns.mergeRuns(A, left, rightRunStart, right, B);
        } else {
            // find middle run
            int i = powerSort.mergesAndRuns.extendWeaklyIncreasingRunLeft(A, mid,
                    left == leftRunEnd ? left : leftRunEnd + 1);
            int j = powerSort.mergesAndRuns.extendWeaklyIncreasingRunRight(A, mid,
                    right == rightRunStart ? right : rightRunStart - 1);
            if (i == left && j == right)
                return;
            if (mid - i < j - mid) {
                // |XX x|xxxx X|
                peeksortOnlyIncreasing(A, left, i - 1, leftRunEnd, i - 1, B);
                peeksortOnlyIncreasing(A, i, right, j, rightRunStart, B);
                powerSort.mergesAndRuns.mergeRuns(A, left, i, right, B);
            } else {
                // |XX xxx|x X|
                peeksortOnlyIncreasing(A, left, j, leftRunEnd, i, B);
                peeksortOnlyIncreasing(A, j + 1, right, j + 1, rightRunStart, B);
                powerSort.mergesAndRuns.mergeRuns(A, left, j + 1, right, B);
            }
        }
    }
}
