/**
 *
 */
package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author yuji
 * @author McDude_73
 *
 */
public final class ChinottoSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public ChinottoSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Chinotto");
		setRunAllSortsName("Chinotto Sort");
		setRunSortName("Chinottosort");
		setCategory("Exchange Sorts");
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(0);
		setBogoSort(false);

	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) throws Exception {
		boolean done = false;
		int gap = 1;

		while (!done) {
			int i = 0;
			done = true;
			while (i + gap < length) {
				if (this.Reads.compareIndices(array, i, i + gap, 0.5, true) == 1) {
					done = false;
					this.Writes.multiSwap(array, i, i + gap, 0.2D, true, false);
					gap++;
				} else if (gap >= 2) {
					gap--;
				}

				i++;
			}
			while (i - gap > 0) {
				if (this.Reads.compareIndices(array, i - gap, i, 0.5, true) == 1) {
					done = false;
					this.Writes.multiSwap(array, i, i - gap, 0.2D, true, false);
					gap++;
				} else if (gap >= 2) {
					gap--;
				}

				i--;
			}
		}

	}

}
