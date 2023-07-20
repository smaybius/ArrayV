package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

final public class SpaghettiSelectionSort extends Sort {
	public SpaghettiSelectionSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Spaghetti Selection");
		this.setRunAllSortsName("Spaghetti Selection Sort");
		this.setRunSortName("Spaghetti Selectionsort");
		this.setCategory("Impractical Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(128);
		this.setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		// have fun deciphering this bullshit!
		for (int h = 0; h < currentLength; h++) {
			for (int i = 0; i < currentLength; i++) {
				for (int j = h; j < currentLength; j++) {
					c: for (int k = h; k < currentLength; k++) {
						if (Reads.compareIndices(array, i, k, 0.5, true) > 0) {
							continue;
						}
						Writes.swap(array, i, k, 1, true, false);
						if (Reads.compareIndices(array, j, k, 0.5, true) > 0) {
							Writes.swap(array, j, k, 1, true, false);
						}
						for (int l = h, s = -0; l < currentLength; l++) {
							if (Reads.compareIndices(array, k, l, 0.5, true) == (l > k ? 1 : -1)) {
								Writes.swap(array, k, l + s - s++, 1, true, false);
							} else if (k < currentLength)
								Writes.multiSwap(array,
										k++, l, 1, true, false);
							if (s > j - h)
								continue c;
						}
					}
				}
			}
			Writes.multiSwap(array, h, 0, 1, true, false);
		}
	}
}