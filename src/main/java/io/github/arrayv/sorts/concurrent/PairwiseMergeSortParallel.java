package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

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

public final class PairwiseMergeSortParallel extends Sort {
	public PairwiseMergeSortParallel(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Pairwise Merge (Parallel)");
		this.setRunAllSortsName("Parallel Pairwise Merge Sort");
		this.setRunSortName("Parallel Pairwise Mergesort");
		this.setCategory("Concurrent Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(4096);
		this.setBogoSort(false);
	}

	private int end;

	private int[] array;

	private class MergeThread extends Thread {
		private int a, b;

		MergeThread(int a, int b) {
			this.a = a;
			this.b = b;
		}

		public void run() {
			PairwiseMergeSortParallel.this.pairwiseMerge(a, b);
		}
	}

	private class SortThread extends Thread {
		private int a, b;

		SortThread(int a, int b) {
			this.a = a;
			this.b = b;
		}

		public void run() {
			PairwiseMergeSortParallel.this.pairwiseMergeSort(a, b);
		}
	}

	private void compSwap(int a, int b) {
		if (b < this.end && Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}

	private void pairwiseMerge(int a, int b) {
		int m = (a + b) / 2, m1 = (a + m) / 2, g = m - m1;

		for (int i = 0; m1 + i < m; i++)
			for (int j = m1, k = g; k > 0; k >>= 1, j -= k - (i & k))
				this.compSwap(j + i, j + i + k);

		if (b - a > 4) {
			MergeThread mt = new MergeThread(m, b);
			mt.start();

			try {
				mt.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}

	private void pairwiseMergeSort(int a, int b) {
		int m = (a + b) / 2;

		for (int i = a, j = m; i < m; i++, j++)
			this.compSwap(i, j);

		if (b - a > 2) {
			SortThread left = new SortThread(a, m);
			SortThread right = new SortThread(m, b);
			left.start();
			right.start();

			try {
				left.join();
				right.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			this.pairwiseMerge(a, b);
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) throws Exception {
		this.end = length;
		this.array = array;
		int n = 1;
		for (; n < length; n <<= 1)
			;

		this.pairwiseMergeSort(0, n);
	}
}
