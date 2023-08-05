package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

/*
 *
MIT License

Copyright (c) 2020-2021 aphitorite

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
@SortMeta(listName = "Rotate LSD Radix", showcaseName = "Rotate LSD Radix Sort", runName = "Rotate LSD Radix Sort", question = "Enter base width (default: 4):", defaultAnswer = 4)
public final class RotateLSDRadixSort extends Sort {
	public RotateLSDRadixSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int base;

	private void rotate(int[] array, int a, int m, int b) {
		IndexedRotations.adaptable(array, a, m, b, 0.5, true, false);
	}

	private int binSearch(int[] array, int a, int b, int d, int p) {
		while (a < b) {
			int m = (a + b) / 2;

			if (Reads.getDigit(array[m], p, this.base) >= d)
				b = m;

			else
				a = m + 1;
		}
		return a;
	}

	private void merge(int[] array, int a, int m, int b, int da, int db, int p) {
		if (b - a < 2 || db - da < 2)
			return;

		int dm = (da + db) / 2;
		int m1 = this.binSearch(array, a, m, dm, p);
		int m2 = this.binSearch(array, m, b, dm, p);

		this.rotate(array, m1, m, m2);
		m = m1 + (m2 - m);

		this.merge(array, m, m2, b, dm, db, p);
		this.merge(array, a, m1, m, da, dm, p);
	}

	private void mergeSort(int[] array, int a, int b, int p) {
		if (b - a < 2)
			return;

		int m = (a + b) / 2;

		this.mergeSort(array, a, m, p);
		this.mergeSort(array, m, b, p);

		this.merge(array, a, m, b, 0, this.base, p);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.base = bucketCount;
		int max = Reads.analyzeMaxLog(array, length, this.base, 0.5, true);

		for (int i = 0; i <= max; i++)
			this.mergeSort(array, 0, length, i);
	}
}
