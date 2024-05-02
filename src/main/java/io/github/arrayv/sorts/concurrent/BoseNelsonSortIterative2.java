package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2023 aphitorite

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
@SortMeta(listName = "Bose-Nelson (Iterative 2)", runName = "Iterative Bose-Nelson Sort 2")
final public class BoseNelsonSortIterative2 extends Sort {
	public BoseNelsonSortIterative2(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int end;

	private void compSwap(int[] array, int a, int b) {
		if (b >= this.end)
			return;

		if (Reads.compareIndices(array, a, b, 0.25, true) == 1)
			Writes.swap(array, a, b, 0.5, false, false);
	}

	private int weight(int x) { // compute hamming weight of x
		int r = 0;
		while (x > 0) {
			r += x & 1;
			x /= 2;
		}
		return r;
	}

	private void boseNelsonPass(int[] array, int a, int m, int v, int w) {
		int n = 0;
		for (int j = 0; j < (1 << w) - 1; j++) {
			this.compSwap(array, a + n + v, m + n);

			for (int i = 0;; i++) {
				while (((v >> i) & 1) == 1)
					i++;
				if (((n >> i) & 1) == 0) {
					n |= 1 << i;
					break;
				}
				n &= ~(1 << i);
			}
		}
		this.compSwap(array, a + n + v, m + n);
	}

	@Override
	public void runSort(int[] array, int n, int bucketCount) {
		this.end = n;
		n = 1 << (int) (Math.ceil(Math.log(n) / Math.log(2)));

		for (int j = 0, k = 1; k < this.end; j++, k *= 2)
			for (int m = 0; m < k; m++)
				for (int i = 0, w = j - this.weight(m); i + k < this.end; i += 2 * k)
					this.boseNelsonPass(array, i, i + k, m, w);
	}
}