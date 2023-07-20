/**
 *
 */
package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author McDude_73
 * @author aphitorite
 * @author EilrahcF
 *
 */
public final class WiggleSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public WiggleSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Wiggle");
		setRunAllSortsName("Wiggle Sort");
		setRunSortName("Wigglesort");
		setCategory("Exchange Sorts");
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(0);
		setBogoSort(false);

	}

	private void wiggleSort(int[] array, int arrLen, int start, int end) {
		if (end - start < 2)
			return;
		int leftPoint = start;
		int rightPoint = end;

		int midPoint = (leftPoint + rightPoint) / 2;

		boolean startLeft = true;
		int j = midPoint;

		for (int i = leftPoint; i < midPoint; i++) {
			for (int k = midPoint; k < end; k++) {
				if (this.Reads.compareIndices(array, i, j, 0.1, true) >= 0) {
					this.Writes.swap(array, i, j, 1, true, false);
				}

				if (startLeft) {
					j++;
				} else {
					j--;
				}

			}
			if (startLeft) {
				j--;
				startLeft = false;
			} else {

				j++;
				startLeft = true;
			}
		}

		wiggleSort(array, arrLen, start, midPoint);
		wiggleSort(array, arrLen, midPoint, end);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) throws Exception {
		wiggleSort(array, length, 0, length);

	}

}
