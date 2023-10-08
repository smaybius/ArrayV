package io.github.arrayv.sorts.exchange;

import io.github.arrayv.sorts.templates.Sort;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;

/* _________________________
  /                         \
 | As Seen On PCBoy's Oh God |
 |       and Sort Grid       |
 |(AKA: MapleMagicShovySeas) |
  \_________________________/ */
@SortMeta(name = "Maple")
final public class MapleSort extends Sort {
	public MapleSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		boolean pull = true;
		for (; pull;) {
			pull = false;
			int p = 0;
			for (int j = 0; j < length; j += 2) {
				if (j != length - 1 && Reads.compareIndices(array, j, j + 1, 1, true) > 0) {
					if (j + 1 != p) {
						pull = true;
					}
					Writes.multiSwap(array, j + 1, p, 1, true, false);
				} else {
					if (j != p) {
						pull = true;
					}
					Writes.multiSwap(array, j, p++, 1, true, false);
					if (p >= j) {
						j--;
						continue;
					}
				}
				if (p <= j)
					p++;
			}
		}
	}
}