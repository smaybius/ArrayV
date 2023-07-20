package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class XSort extends Sort {
	public XSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		this.setSortListName("X Pattern");
		this.setRunAllSortsName("X Pattern Sort");
		this.setRunSortName("X Pattern Sort");
		this.setCategory("Impractical Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(1024);
		this.setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int gap = currentLength;
		int i = 1;
		int xleft = 1;
		int xright = 1;
		boolean anyswaps = false;
		boolean testpass = false;
		while (!testpass) {
			anyswaps = false;
			i = 1;
			while ((i - 1) + gap < currentLength) {
				if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.5, true) > 0) {
					Writes.swap(array, i - 1, (i - 1) + gap, 0.001, true, false);
					anyswaps = true;
					xleft = i + 1;
					xright = i + gap - 1;
					if (gap != 1) {
						for (int r = 0; r < gap - 1; r++) {
							if (Reads.compareIndices(array, xleft - 1, xright - 1, 0.5, true) > 0)
								Writes.swap(array, xleft - 1, xright - 1, 0.001, true, false);
							xleft++;
							xright--;
						}
					}
				}
				i++;
			}
			if (gap == 1 && !anyswaps)
				testpass = true;
			else if (gap != 1 && !anyswaps)
				gap--;
		}
	}
}