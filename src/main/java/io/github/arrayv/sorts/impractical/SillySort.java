package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

// Written by Tom Duff, and found here: http://home.tiac.net/~cri_d/cri/2001/badsort.html
// from https://stackoverflow.com/questions/2609857/are-there-any-worse-sorting-algorithms-than-bogosort-a-k-a-monkey-sort/

public final class SillySort extends Sort {
	public SillySort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Silly");
		this.setRunAllSortsName("Silly Sort");
		this.setRunSortName("Sillysort");
		this.setCategory("Impractical Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(150);
		this.setBogoSort(false);
	}

	private void sillySort(int[] array, int i, int j, int depth) {
		int m;

		if (i < j) {
			/* find the middle of the array */
			m = i + ((j - i) / 2);

			/*
			 * use this function (recursively) to find put the minimum elements of
			 * each half into the first elements of each half
			 */
			Writes.recordDepth(depth);
			Writes.recursion();
			this.sillySort(array, i, m, depth + 1);
			Writes.recursion();
			this.sillySort(array, m + 1, j, depth + 1);

			/*
			 * Choose the smallest element of the two halves, and put that element in
			 * the first position
			 */
			if (Reads.compareIndices(array, i, m + 1, 0, true) >= 0) {
				Writes.swap(array, i, m + 1, 1, true, false);
			}

			Highlights.markArray(1, i);
			Highlights.markArray(2, m + 1);
			Writes.recordDepth(depth);
			Writes.recursion();
			this.sillySort(array, i + 1, j, depth + 1);
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.sillySort(array, 0, currentLength - 1, 0);
	}
}
