package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

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
@SortMeta(listName = "Optimized Weave Merge", runName = "Optimized Weave Merge Sort")
public final class OptimizedWeaveMergeSort extends Sort {
	public OptimizedWeaveMergeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private void insertTo(int[] array, int a, int b) {
		int temp = array[a];
		while (a > b)
			Writes.write(array, a, array[--a], 0.25, true, false);
		Writes.write(array, b, temp, 0.25, true, false);
	}

	private void rotate(int[] array, int a, int m, int b) {
		IndexedRotations.adaptable(array, a, m, b, 0.5, true, false);
	}

	// pow of 2 only (O(n))
	private void bitReversal(int[] array, int a, int b) {
		int len = b - a, m = 0;
		int d1 = len >> 1, d2 = d1 + (d1 >> 1);

		for (int i = 1; i < len - 1; i++) {
			int j = d1;

			for (int k = i, n = d2; (k & 1) == 0; j -= n, k >>= 1, n >>= 1)
				;
			m += j;
			if (m > i)
				Writes.swap(array, a + i, a + m, 1, true, false);
		}
	}

	private void weaveInsert(int[] array, int a, int b, boolean right) {
		int i = a, j = i + 1;

		while (j < b) {
			while (i < j && Reads.compareIndices(array, i, j, 0, true) < (right ? 1 : 0))
				i++;

			if (i == j) {
				right = !right;
				j++;
			} else {
				this.insertTo(array, j, i++);
				j += 2;
			}
		}
		Highlights.clearMark(3);
	}

	// 000111 -> 010101 T
	// 00011 -> 01010 T
	// 00111 -> 10101 F
	private void weaveMerge(int[] array, int a, int m, int b) {
		if (b - a < 2)
			return;

		int a1 = a, b1 = b;
		boolean right = true;

		if ((b - a) % 2 == 1) {
			if (m - a < b - m) {
				a1--;
				right = false;
			} else
				b1++;
		}

		for (int e = b1, f; e - a1 > 2; e = f) {
			m = (a1 + e) / 2;
			int p = 1 << (int) (Math.log(m - a1) / Math.log(2));

			this.rotate(array, m - p, m, e - p);
			m = e - p;
			f = m - p;

			this.bitReversal(array, f, m);
			this.bitReversal(array, m, e);
			this.bitReversal(array, f, e);
		}
		Highlights.clearMark(2);
		this.weaveInsert(array, a, b, right);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int n = currentLength, d = 1 << (int) (Math.log(n - 1) / Math.log(2) + 1);

		while (d > 1) {
			int i = 0, dec = 0;

			while (i < n) {
				int j = i;
				dec += n;
				while (dec >= d) {
					dec -= d;
					j++;
				}
				int k = j;
				dec += n;
				while (dec >= d) {
					dec -= d;
					k++;
				}
				this.weaveMerge(array, i, j, k);
				i = k;
			}
			d /= 2;
		}
	}
}
