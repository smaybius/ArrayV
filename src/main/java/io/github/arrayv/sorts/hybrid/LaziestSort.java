package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;
import io.github.arrayv.utils.Searches;

/*
 *
MIT License

Copyright (c) 2020 aphitorite

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

public final class LaziestSort extends Sort {
	public LaziestSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		this.setSortListName("Laziest Stable");
		this.setRunAllSortsName("Laziest Stable Sort");
		this.setRunSortName("Laziest Sort");
		this.setCategory("Hybrid Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private static void rotate(int[] array, int a, int m, int b) {
		IndexedRotations.cycleReverse(array, a, m, b, 0.5, true, false);
	}

	private static void binaryInsertion(int[] array, int a, int b) {
		for (int i = a + 1; i < b; i++)
			Searches.insertTo(array, i, Searches.rightBinSearch(array, a, i, array[i], 0.5), 0.5, false);
	}

	private void inPlaceMerge(int[] array, int start, int mid, int end) {
		int i = start;
		int j = mid;
		int k;

		while (i < j && j < end) {
			if (Reads.compareIndices(array, i, j, 0.2, true) == 1) {
				k = Searches.leftExpSearch(array, j + 1, end, array[i], 0.5);
				rotate(array, i, j, k);

				i += k - j;
				j = k;
			} else
				i++;
		}
	}

	void laziestStableSort(int[] array, int start, int end) {
		int len = end - start;
		if (len <= 16) {
			binaryInsertion(array, start, end);
			return;
		}

		int i;
		int blockLen = Math.max(16, (int) Math.sqrt(len));
		for (i = start; i + 2 * blockLen < end; i += blockLen) {
			binaryInsertion(array, i, i + blockLen);
		}
		binaryInsertion(array, i, end);

		while (i - blockLen >= start) {
			this.inPlaceMerge(array, i - blockLen, i, end);
			i -= blockLen;
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.laziestStableSort(array, 0, currentLength);
	}
}
